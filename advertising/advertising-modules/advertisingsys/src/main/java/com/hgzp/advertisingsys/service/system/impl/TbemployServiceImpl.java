package com.hgzp.advertisingsys.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.system.dto.EmployDTO;
import com.hgzp.advertisingsys.pagemodel.system.vo.EmployVO;
import com.hgzp.advertisingsys.pagemodel.system.vo.OrgTreeVo;
import com.hgzp.advertisingsys.service.system.*;
import com.hgzp.advertisingsys.utils.DataUtil;
import com.hgzp.annotation.LogModelAddCache;
import com.hgzp.annotation.LogModelRemoveCache;
import com.hgzp.common.flowable.constants.NodeUserTypeEnum;
import com.hgzp.common.flowable.dto.third.DeptDto;
import com.hgzp.common.flowable.dto.third.UserDto;
import com.hgzp.common.flowable.factory.ApiStrategyFactory;
import com.hgzp.core.constant.system.AdminFlagConstants;
import com.hgzp.core.emnus.LogTypes;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.*;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.system.TbemployMapper;
import com.hgzp.mapper.system.TbroleMapper;
import com.hgzp.service.RedisService;
import com.hgzp.service.system.impl.BaseTbemployServiceImpl;
import com.hgzp.utils.SecurityUtils;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import com.hgzp.utils.security.SM2Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.hgzp.core.constant.system.AdminFlagConstants.DEPT_ADMIN;

/**
 * <p>
 * 人员表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbemployServiceImpl extends BaseTbemployServiceImpl implements TbemployServiceI {

    @Autowired
    TbemployMapper tbemployMapper;
    @Autowired
    TbemployroleServiceI tbemployroleService;
    @Autowired
    TbroleServiceI tbroleService;
    @Autowired
    TbdeptServiceI tbdeptService;
    @Autowired
    TwsysoperatelogServiceI twsysoperatelogService;
    @Autowired
    TbroleMapper tbroleMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor logInterceptor;
    @Autowired
    RedisService redisService;

    @Override
    public Page<EmployVO> getEmployPageList(Page<Tbemploy> page, EmployVO query) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Page<EmployVO> reslutPage = new Page<>();

        List<Long> childIdList = new ArrayList<>();
        //普通管理员查询自己部门下的（包括子部门）有权限，以上的父部门皆为导航节点
        if (DEPT_ADMIN == user.getAdminflag()) {
            childIdList = tbdeptService.getChildDeptId(user.getDeptid());
        }
        // [11843] 管理系统，人员管理 点击父节点显示所有子节点内容（显示所属部门）
        if (query.getDeptid() != null) {
            childIdList = tbdeptService.getChildDeptId(query.getDeptid());
        }

        Page<Tbemploy> tbemployPage = this.lambdaQuery()
                .in(childIdList.size() > 0, Tbemploy::getDeptid, childIdList)
//                .eq(query.getDeptid() != null, Tbemploy::getDeptid, query.getDeptid())
                .ne(Tbemploy::getBadminflag, AdminFlagConstants.ADMIN)
                .and(StrUtil.isNotBlank(query.getQueryKey()),
                        i -> i.like(Tbemploy::getSusername, query.getQueryKey())
                                .or()
                                .like(Tbemploy::getSphone, query.getQueryKey()))
                .page(page);
        if (page.getTotal() == 0) {
            return reslutPage;
        }

        List<Long> deptIds = tbemployPage.getRecords()
                .stream()
                .map(Tbemploy::getDeptid)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, List<Tbdept>> deptById = tbdeptService.listByIds(deptIds).stream().collect(Collectors.groupingBy(Tbdept::getId));

        List<Long> empIds = tbemployPage.getRecords()
                .stream()
                .map(Tbemploy::getId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, List<Tbrole>> roleListGroupByUserId = tbroleService.getRoleListGroupByUserId(empIds);

        List<EmployVO> result = new ArrayList<>();
        for (Tbemploy record : tbemployPage.getRecords()) {
            EmployVO employModel = new EmployVO(record);

            List<Tbrole> tbroles = roleListGroupByUserId.get(record.getId());

            String roleNames = tbroles.stream()
                    .map(Tbrole::getSname)
                    .collect(Collectors.joining(","));
            String roleIds = tbroles.stream()
                    .map(Tbrole::getId)
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            employModel.setRoleIds(roleIds);
            employModel.setRoleNames(roleNames);
            employModel.setDeptName(deptById.get(employModel.getDeptid()).get(0).getSdeptname());
            result.add(employModel);
        }

        reslutPage.setRecords(result);
        reslutPage.setTotal(tbemployPage.getTotal());
        return reslutPage;
    }

    @Override
    @LogModelAddCache
    public EmployVO getEmployInfoById(Long id) {
        Tbemploy tbemploy = this.getById(id);
        EmployVO employModel = new EmployVO(tbemploy);
        //部门
        Tbdept tbdept = tbdeptService.getById(tbemploy.getId());
        if (tbdept != null) {
            employModel.setDeptName(tbdept.getSdeptname());
        }

        //角色
        List<Long> empIds = new ArrayList<>();
        empIds.add(tbemploy.getId());
        Map<Long, List<Tbrole>> roleListGroupByUserId = tbroleService.getRoleListGroupByUserId(empIds);
        List<Tbrole> tbroles = roleListGroupByUserId.get(tbemploy.getId());
        String roleNames = tbroles.stream()
                .map(Tbrole::getSname)
                .collect(Collectors.joining(","));
        String roleIds = tbroles.stream()
                .map(Tbrole::getId)
                .map(Object::toString)
                .collect(Collectors.joining(","));
        employModel.setRoleIds(roleIds);
        employModel.setRoleNames(roleNames);

        return employModel;
    }


    @Override
    public void saveEmploy(EmployDTO employ) throws Exception {
        //判断手机号是否重复
        if (phoneExist(employ.getSphone(), employ.getId())) {
            throw new DataExistException("手机号已存在");
        }
        //判断登录名是否重复
        if (loginNameExist(employ.getSphone(), employ.getId())) {
            throw new DataExistException("登录名已存在");
        }
        Long empId = IdUtil.getSnowflakeNextId();
        Tbemploy tbemploy = new Tbemploy();
        BeanUtils.copyProperties(employ, tbemploy);
        tbemploy.setId(empId);
        tbemploy.setSpassword(SecurityUtils.encryptPassword(SM2Utils.decryptByPrivateKey(employ.getSpassword())));

        tbemployMapper.insert(tbemploy);
        //人员关联角色
        saveEmpRole(employ, empId);

        //日志
        String compareResult = twsysoperatelogService.compareTowObject(null, employ);

        Twsysoperatelog log = new Twsysoperatelog();
        log.setSlogtype(LogTypes.LOG_TYPES_INSERT.getTypeStr());
        log.setStablename("Tbemploy");
        log.setSlogname("新增人员");
        log.setSnewvalue(compareResult);
        log.setRecordmasterid(empId);
        twsysoperatelogService.saveAsyncSysoperatelogWithoutUser(log);

    }

    @Override
    @LogModelRemoveCache
    public void updateEmploy(EmployDTO employ) throws Exception {
        //判断手机号是否重复
        if (phoneExist(employ.getSphone(), employ.getId())) {
            throw new DataExistException("手机号已存在");
        }
        //判断登录名是否重复
        if (loginNameExist(employ.getSphone(), employ.getId())) {
            throw new DataExistException("登录名已存在");
        }
        Tbemploy dbEmp = this.getById(employ.getId());

        Tbemploy tbemploy = new Tbemploy();
        BeanUtils.copyProperties(employ, tbemploy);

        if (StrUtil.isNotBlank(employ.getSpassword())
                && !dbEmp.getSpassword().equals(employ.getSpassword())
                && !SecurityUtils.matchesPassword(SM2Utils.decryptByPrivateKey(employ.getSpassword()), dbEmp.getSpassword())) {
            tbemploy.setDpasslastmodtime(new Date());
            tbemploy.setSpassword(SecurityUtils.encryptPassword(SM2Utils.decryptByPrivateKey(employ.getSpassword())));
        } else {
            tbemploy.setSpassword(dbEmp.getSpassword());
        }
        LambdaUpdateWrapper<Tbemploy> lmdw = new LambdaUpdateWrapper<>();
        lmdw.eq(Tbemploy::getId, employ.getId())
                .set(Tbemploy::getSweixin, employ.getSweixin())
                .set(Tbemploy::getParttimedeptid, employ.getParttimedeptid())
                .set(Tbemploy::getSnameparttimedept, employ.getSnameparttimedept())
                .set(Tbemploy::getDutiesid, employ.getDutiesid())
                .set(Tbemploy::getSdutiesname, employ.getSdutiesname());

        tbemployMapper.update(tbemploy, lmdw);

        //人员重新关联角色
        LambdaQueryWrapper<Tbemployrole> delWhere = new LambdaQueryWrapper<>();
        delWhere.eq(Tbemployrole::getEmployid, employ.getId());
        tbemployroleService.remove(delWhere);

        saveEmpRole(employ, employ.getId());

        //日志
        EmployVO empOld = redisService.getCacheObject(employ.getCacheDTOKey());
        String compareTowObject = twsysoperatelogService.compareTowObject(empOld, employ);
        Twsysoperatelog twsysoperatelog = new Twsysoperatelog();
        twsysoperatelog.setSlogtype(LogTypes.LOG_TYPES_UPDATE.getTypeStr());
        twsysoperatelog.setSlogname("人员更新");
        twsysoperatelog.setStablename("tbemploy");
        twsysoperatelog.setRecordmasterid(empOld.getId());
        twsysoperatelog.setSnewvalue(compareTowObject);
        twsysoperatelogService.saveAsyncSysoperatelogWithoutUser(twsysoperatelog);

    }


    @Override
    public void deleteEmp(Long id) throws Exception {
        EmployVO oldEmp = getEmployInfoById(id);

        //TODO  判断是否有业务数据关联
        LambdaQueryWrapper<Tbemployrole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tbemployrole::getEmployid, id);
        tbemployroleService.remove(queryWrapper);

        tbemployMapper.deleteById(id);

        //日志
        String compareTowObject = twsysoperatelogService.compareTowObject(oldEmp, null);
        Twsysoperatelog twsysoperatelog = new Twsysoperatelog();
        twsysoperatelog.setSlogtype(LogTypes.LOG_TYPES_DELETE.getTypeStr());
        twsysoperatelog.setSlogname("人员删除");
        twsysoperatelog.setStablename("tbemploy");
        twsysoperatelog.setRecordmasterid(id);
        twsysoperatelog.setSnewvalue(compareTowObject);
        twsysoperatelogService.saveAsyncSysoperatelogWithoutUser(twsysoperatelog);

    }


    @Override
    public void disableEmps(String ids, boolean buse) {
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        logInterceptor.recoredLog();
        tbemployMapper.updateBuseByIds(idList, buse);
    }

    @Override
    public Long countEmpByRoleId(String roleId) {
        LambdaQueryWrapper<Tbemployrole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tbemployrole::getRoleid, roleId);
        long count = tbemployroleService.count(queryWrapper);
        return count;
    }

    /**
     * saveEmpRole
     * 方法功能: 保存人员关联角色
     *
     * @param employ
     * @param empId
     * @return void
     * @author wangwk
     * @date 2023/8/21 17:28
     */
    private void saveEmpRole(EmployDTO employ, Long empId) {
        String roleIds = employ.getRoleIds();
        if (StrUtil.isNotBlank(roleIds)) {
            List<Tbemployrole> tbemployroleList = Arrays.stream(roleIds.split(","))
                    .map(roleId -> new Tbemployrole(empId, Long.parseLong(roleId)))
                    .collect(Collectors.toList());
            tbemployroleService.saveBatch(tbemployroleList);
        }
    }


    @Override
    public void updateBatchEmpRoles(String empIds, String roleIds) throws Exception {
        List<Long> empIdList = Arrays.stream(empIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<Long> roleIdList = Arrays.stream(roleIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());


        //过去人员身上的角色(记录日志)
        List<EmployVO> newEmployVOList = new ArrayList<>();
        List<EmployVO> oldEmployVOList = getOldEmployVOList(empIdList, newEmployVOList);


        //删除人员角色关联表
        tbemployroleService.deleteByEmpIds(empIdList);

        List<Tbemployrole> list = new ArrayList<>();
        ;
        for (Long empId : empIdList) {
            roleIdList.forEach(roleId -> list.add(new Tbemployrole(empId, roleId)));
        }
        tbemployroleService.saveBatch(list);

        //日志
        Map<Long, List<Tbemployrole>> listMapByEmpId = list.stream().collect(Collectors.groupingBy(Tbemployrole::getEmployid));
        for (EmployVO employVO : newEmployVOList) {
            List<Tbemployrole> tbemployroles = listMapByEmpId.get(employVO.getId());
            String newRoleIds = tbemployroles.stream()
                    .map(Tbemployrole::getRoleid)
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            employVO.setRoleIds(newRoleIds);
        }
        saveEmployRoleLog(newEmployVOList, oldEmployVOList);
    }

    /**
     * saveEmployRoleLog
     * 方法功能:  人员角色更新日志
     *
     * @param newEmployVOList
     * @param oldEmployVOList
     * @return void
     * @author CGD
     * @date 2023/9/16 15:39
     */
    private void saveEmployRoleLog(List<EmployVO> newEmployVOList, List<EmployVO> oldEmployVOList) throws Exception {
        for (EmployVO employVO : oldEmployVOList) {
            String compareTowObject = twsysoperatelogService.compareTowObject(employVO, newEmployVOList.stream().filter(e -> e.getId().equals(employVO.getId())).findFirst().get());
            Twsysoperatelog twsysoperatelog = new Twsysoperatelog();
            twsysoperatelog.setSlogtype(LogTypes.LOG_TYPES_UPDATE.getTypeStr());
            twsysoperatelog.setSlogname("人员角色更新");
            twsysoperatelog.setStablename("tbemploy");
            twsysoperatelog.setRecordmasterid(employVO.getId());
            twsysoperatelog.setSnewvalue(compareTowObject);
            twsysoperatelogService.saveAsyncSysoperatelogWithoutUser(twsysoperatelog);
        }
    }

    /**
     * getOldEmployVOList
     * 方法功能: 生成更新之前的人员角色信息
     *
     * @param empIdList
     * @param newEmployVOList
     * @return java.util.List<com.hgzp.advertising.pagemodel.system.vo.EmployVO>
     * @author CGD
     * @date 2023/9/16 15:40
     */
    private List<EmployVO> getOldEmployVOList(List<Long> empIdList, List<EmployVO> newEmployVOList) {
        List<EmployVO> oldEmployVOList = this.lambdaQuery()
                .select(Tbemploy::getSusername, Tbemploy::getId)
                .in(Tbemploy::getId, empIdList)
                .list()
                .stream().map(EmployVO::new).collect(Collectors.toList());

        for (EmployVO employVO : oldEmployVOList) {
            EmployVO newEmployVO = new EmployVO();
            BeanUtils.copyProperties(employVO, newEmployVO);
            newEmployVOList.add(newEmployVO);

            Map<Long, List<Tbrole>> roleListGroupByUserId = tbroleService.getRoleListGroupByUserId(empIdList);
            List<Tbrole> tbroles = roleListGroupByUserId.get(employVO.getId());
            String oldRoleIds = tbroles.stream()
                    .map(Tbrole::getId)
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            employVO.setRoleIds(oldRoleIds);
        }
        return oldEmployVOList;
    }

    @Override
    public EmployVO getEmployInfo() throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Tbemploy dbEmp = this.getById(user.getUserid());
        if (dbEmp != null) {
            EmployVO employ = new EmployVO();
            BeanUtils.copyProperties(dbEmp, employ);
            Tbdept dept = tbdeptService.getById(dbEmp.getDeptid());
            if (dept != null) {
                employ.setDeptName(dept.getSdeptname());
            }
            List<Long> empIds = new ArrayList<>();
            empIds.add(user.getUserid());
            Map<Long, List<Tbrole>> roleListGroupByUserId = tbroleService.getRoleListGroupByUserId(empIds);
            List<Tbrole> tbroles = roleListGroupByUserId.get(user.getUserid());

            String roleNames = tbroles.stream()
                    .map(Tbrole::getSname)
                    .collect(Collectors.joining(","));
            String roleIds = tbroles.stream()
                    .map(Tbrole::getId)
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            employ.setRoleNames(roleNames);
            return employ;
        }
        return null;
    }

    @Override
    public Page<EmployVO> getEmployPageListByRoleId(Page<Tbemploy> page, EmployVO query) throws Exception {
        Page<EmployVO> reslutPage = new Page<>();
        List<Long> employIdCollect = tbemployroleService.getEmployIdByRoleId(query.getRoleIds());
        if (employIdCollect.size() < 1) {
            return reslutPage;
        }
        LambdaQueryChainWrapper<Tbemploy> lqw = getTbemployLambdaQueryChainWrapper(query);
        lqw.in(employIdCollect.size() > 0, Tbemploy::getId, employIdCollect);
        Page<Tbemploy> tbemployPage = lqw.page(page);
        if (tbemployPage.getTotal() == 0) {
            return reslutPage;
        }
        List<EmployVO> result = getEmployVOS(tbemployPage);
        reslutPage.setRecords(result);
        reslutPage.setTotal(tbemployPage.getTotal());
        return reslutPage;
    }

    @Override
    public Page<EmployVO> getEmployPageListNotByRoleId(Page<Tbemploy> page, EmployVO query) throws Exception {
        List<Long> childIdList = getDeptChildList();
        Page<EmployVO> reslutPage = new Page<>();
        List<Long> employIdCollect = tbemployroleService.getEmployIdByRoleId(query.getRoleIds());
        LambdaQueryChainWrapper<Tbemploy> lqw = getTbemployLambdaQueryChainWrapper(query);
        lqw.in(childIdList.size() > 0, Tbemploy::getId, childIdList);
        lqw.notIn(employIdCollect.size() > 0, Tbemploy::getId, employIdCollect);
        Page<Tbemploy> tbemployPage = lqw.page(page);
        if (tbemployPage.getTotal() == 0) {
            return reslutPage;
        }
        List<EmployVO> result = getEmployVOS(tbemployPage);
        reslutPage.setRecords(result);
        reslutPage.setTotal(tbemployPage.getTotal());
        return reslutPage;
    }

    /**
     * getEmployVOS
     * 方法功能:  根据人员  model 转换成EmployVO
     *
     * @param tbemployPage
     * @return java.util.List<com.hgzp.advertising.pagemodel.system.vo.EmployVO>
     * @author CGD
     * @date 2023/9/16 15:41
     */
    private List<EmployVO> getEmployVOS(Page<Tbemploy> tbemployPage) {
        List<EmployVO> result = new ArrayList<>();
        for (Tbemploy record : tbemployPage.getRecords()) {
            EmployVO employModel = new EmployVO(record);
            Tbdept deptById = tbdeptService.getById(employModel.getDeptid());
            employModel.setDeptName(deptById.getSdeptname());
            result.add(employModel);
        }
        return result;
    }

    /**
     * getTbemployLambdaQueryChainWrapper
     * 方法功能: 根据角色查询人员的统一Wrapper条件
     *
     * @param query
     * @return com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper<com.hgzp.core.model.Tbemploy>
     * @author CGD
     * @date 2023/9/16 15:42
     */
    private LambdaQueryChainWrapper<Tbemploy> getTbemployLambdaQueryChainWrapper(EmployVO query) {
        LambdaQueryChainWrapper<Tbemploy> lqw = this.lambdaQuery()
                .eq(query.getDeptid() != null, Tbemploy::getDeptid, query.getDeptid())
                .ne(Tbemploy::getBadminflag, AdminFlagConstants.ADMIN)
                .and(StrUtil.isNotBlank(query.getQueryKey()),
                        i -> i.like(Tbemploy::getSusername, query.getQueryKey())
                                .or()
                                .like(Tbemploy::getSphone, query.getQueryKey()));
        return lqw;
    }


    /**
     * getDeptChildList
     * 方法功能:    普通管理员查询自己部门下的（包括子部门）有权限，以上的父部门皆为导航节点
     *
     * @param
     * @return java.util.List<java.lang.Long>
     * @author CGD
     * @date 2023/9/16 15:44
     */
    private List<Long> getDeptChildList() throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        List<Long> childIdList = new ArrayList<>();
        //普通管理员查询自己部门下的（包括子部门）有权限，以上的父部门皆为导航节点
        if (DEPT_ADMIN == user.getAdminflag()) {
            childIdList = tbdeptService.getChildDeptId(user.getDeptid());
        }
        return childIdList;
    }


    @Override
    public void saveEmployByRole(Long roleId, String empIds) throws Exception {
        List<Long> employByRoleId = tbemployroleService.getEmployIdByRoleId(roleId.toString());
        List<Long> empIdList = Arrays.stream(empIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        // 只返回1集合重不重复的
        List<Long> addLongs = CollUtil.subtractToList(empIdList, employByRoleId);
        //过去人员身上的角色(记录日志)
        List<EmployVO> newEmployVOList = new ArrayList<>();
        List<EmployVO> oldEmployVOList = getOldEmployVOList(addLongs, newEmployVOList);

        // 增加人员角色
        List<Tbemployrole> list = new ArrayList<>();
        for (Long empId : addLongs) {
            list.add(new Tbemployrole(empId, roleId));
        }
        tbemployroleService.saveBatch(list);
        for (EmployVO employVO : newEmployVOList) {
            Map<Long, List<Tbrole>> roleListGroupByUserId = tbroleService.getRoleListGroupByUserId(addLongs);
            List<Tbrole> tbroles = roleListGroupByUserId.get(employVO.getId());
            String newRoleIds = tbroles.stream()
                    .map(Tbrole::getId)
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            employVO.setRoleIds(newRoleIds);
        }
        saveEmployRoleLog(newEmployVOList, oldEmployVOList);
    }

    @Override
    public void deleteRoleById(Long roleId) throws Exception {
        List<Long> employByRoleId = tbemployroleService.getEmployIdByRoleId(roleId.toString());
        //过去人员身上的角色(记录日志)
        if (employByRoleId != null && employByRoleId.size() > 0) {
            List<EmployVO> newEmployVOList = new ArrayList<>();
            List<EmployVO> oldEmployVOList = getOldEmployVOList(employByRoleId, newEmployVOList);

            tbemployroleService.deleteByRoleIds(Arrays.asList(roleId));
            for (EmployVO employVO : newEmployVOList) {
                Map<Long, List<Tbrole>> roleListGroupByUserId = tbroleService.getRoleListGroupByUserId(employByRoleId);
                List<Tbrole> tbroles = roleListGroupByUserId.get(employVO.getId());
                String newRoleIds = tbroles.stream()
                        .map(Tbrole::getId)
                        .map(Object::toString)
                        .collect(Collectors.joining(","));
                employVO.setRoleIds(newRoleIds);
            }
            saveEmployRoleLog(newEmployVOList, oldEmployVOList);
        }

    }

    @Override
    public void deleteRoleByEmploy(Long roleId, String empIds) throws Exception {
        List<Long> empIdList = Arrays.stream(empIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        //过去人员身上的角色(记录日志)
        List<EmployVO> newEmployVOList = new ArrayList<>();
        List<EmployVO> oldEmployVOList = getOldEmployVOList(empIdList, newEmployVOList);

        // 删除人员角色
        tbemployroleService.deleteByEmpIdsRoleId(empIdList, roleId);
        for (EmployVO employVO : newEmployVOList) {
            Map<Long, List<Tbrole>> roleListGroupByUserId = tbroleService.getRoleListGroupByUserId(empIdList);
            List<Tbrole> tbroles = roleListGroupByUserId.get(employVO.getId());
            String newRoleIds = tbroles.stream()
                    .map(Tbrole::getId)
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            employVO.setRoleIds(newRoleIds);
        }
        saveEmployRoleLog(newEmployVOList, oldEmployVOList);
    }

    /**
     * getOrgTreeData
     * 方法功能: 工作流用 - 查询所有的员工
     *
     * @param deptId    部门id
     * @param type      只查询部门架构
     * @param showLeave 是否显示离职员工
     * @return java.lang.Object
     * @author yanz
     * @date 2023/10/14 13:32
     */
    @Override
    public Json<Dict> getOrgTreeData(String deptId, String type, Boolean showLeave) {
        Dict dict = Dict.create()
                .set("titleDepartments", new ArrayList<>())
                .set("roleList", new ArrayList<>())
                .set("employees", new ArrayList<>());
        List<DeptDto> deptList = ApiStrategyFactory.getStrategy().loadAllDept(deptId);

        List deptVoList = new ArrayList();
        for (DeptDto dept : deptList) {
            OrgTreeVo orgTreeVo = new OrgTreeVo();
            orgTreeVo.setId(dept.getId());
            orgTreeVo.setName(dept.getName());
            orgTreeVo.setType(NodeUserTypeEnum.DEPT.getKey());
            orgTreeVo.setSelected(false);
            orgTreeVo.setStatus(dept.getStatus());
            deptVoList.add(orgTreeVo);
        }
        dict.set("childDepartments", deptVoList);
        if (StrUtil.isNotBlank(deptId)) {
            List<DeptDto> allDept = ApiStrategyFactory.getStrategy().loadAllDept(null);
            List<DeptDto> depts = DataUtil.selectParentByDept(deptId, allDept);
            dict.set("titleDepartments", CollUtil.reverse(depts));
        }

        List userVoList = new ArrayList();
        List<UserDto> userList = ApiStrategyFactory.getStrategy().loadUserByDept(String.valueOf(deptId));

        for (UserDto user : userList) {
            OrgTreeVo orgTreeVo = new OrgTreeVo();
            orgTreeVo.setId(user.getId());
            orgTreeVo.setName(user.getName());
            orgTreeVo.setType(NodeUserTypeEnum.USER.getKey());
            orgTreeVo.setSelected(false);
            orgTreeVo.setStatus(user.getStatus());
            orgTreeVo.setAvatar(user.getAvatarUrl());
            userVoList.add(orgTreeVo);

        }
        dict.set("employees", userVoList);
        return Json.success(dict);
    }

    /**
     * getOrgTreeUser
     * 方法功能: 模糊搜索用户
     *
     * @param userName 用户名/拼音/首字母
     * @return java.lang.Object 匹配到的用户
     * @author yanz
     * @date 2023/10/14 13:54
     */
    @Override
    public Json<List<OrgTreeVo>> getOrgTreeUser(String userName) {
        List<UserDto> userList = ApiStrategyFactory.getStrategy().searchUser(userName);

        List<OrgTreeVo> orgTreeVoList = new ArrayList<>();

        for (UserDto user : userList) {
            OrgTreeVo orgTreeVo = new OrgTreeVo();
            orgTreeVo.setId(user.getId());
            orgTreeVo.setName(user.getName());
            orgTreeVo.setType(NodeUserTypeEnum.USER.getKey());
            orgTreeVo.setAvatar(user.getAvatarUrl());

            orgTreeVo.setStatus(user.getStatus());
            orgTreeVoList.add(orgTreeVo);

        }
        return Json.success(orgTreeVoList);
    }


}
