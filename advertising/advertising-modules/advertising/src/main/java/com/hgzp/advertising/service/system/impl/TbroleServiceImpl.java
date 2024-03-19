package com.hgzp.advertising.service.system.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.pagemodel.system.dto.EmpAuthorityDTO;
import com.hgzp.advertising.service.system.*;
import com.hgzp.core.constant.system.AdminFlagConstants;
import com.hgzp.core.model.*;
import com.hgzp.mapper.system.TbemployroleMapper;
import com.hgzp.service.system.impl.BaseTbroleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbroleServiceImpl extends BaseTbroleServiceImpl implements TbroleServiceI {

    @Autowired
    TbemployroleMapper tbemployroleMapper;
    @Autowired
    TbemployServiceI tbemployService;
    @Autowired
    TbdeptServiceI tbdeptService;
    @Autowired
    TbmenuServiceI tbmenuService;
    @Autowired
    TbroleactionServiceI tbroleactionService;
    @Autowired
    TbroleactionscopeServiceI tbroleactionscopeService;


    @Override
    public Map<Long, List<Tbrole>> getRoleListGroupByUserId(List<Long> userIdList) {
        Map<Long, List<Tbrole>> resultMap = new HashMap<>();

        List<Tbemployrole> tbemployroleList = new LambdaQueryChainWrapper<>(tbemployroleMapper)
                .select(Tbemployrole::getRoleid, Tbemployrole::getEmployid)
                .in(Tbemployrole::getEmployid, userIdList)
                .list();

        List<Long> roleIdList = tbemployroleList.stream()
                .map(Tbemployrole::getRoleid)
                .distinct()
                .collect(Collectors.toList());

        if (roleIdList.size() == 0) {
            userIdList.forEach(id -> resultMap.put(id, Collections.emptyList()));
            return resultMap;
        }

        Map<Long, List<Tbrole>> roleById = this.listByIds(roleIdList).stream().collect(Collectors.groupingBy(Tbrole::getId));

        Map<Long, List<Tbemployrole>> roleIdByUserId = tbemployroleList
                .stream()
                .collect(Collectors.groupingBy(Tbemployrole::getEmployid));

        for (Long userId : userIdList) {
            List<Tbemployrole> tbemployroles = roleIdByUserId.get(userId);
            if (tbemployroles == null) {
                resultMap.put(userId, Collections.emptyList());
                continue;
            }
            List<Tbrole> collect = tbemployroles.stream()
                    .map(Tbemployrole::getRoleid)
                    .map(roleById::get)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            resultMap.put(userId, collect);
        }
        return resultMap;

    }


    /**
     * 根据用户身份获取用户权限
     * 方法功能:
     *     1、兼职超管登录后，查询条件不限制；操作员（纯操作员）登录后，默认是能看到所有数据
     *     2、单位管理员登录后，查看所负责的部门及所有子部门
     *     3、业务部门负责人登录后，自己所在的部门和兼职部门及所有子部门的数据。
     *     4、业务员（纯业务员）登录后，只能看自己的数据
     *     5、以上均为默认权限规则，后续角色范围权限在权限拦截器拦截后进行相应赋值
     * @author wangwk
     * @date 2023/11/2 14:19
     * @param userId
     * @return java.util.List<java.lang.Long>
     */
    @Override
    public EmpAuthorityDTO getEmpAuthIdsByEmpId(long userId){
        Tbemploy tbemploy = tbemployService.getById(userId);

        List<Long> authDeptIdList = new ArrayList<>();
        List<Long> authEmpIdList = new ArrayList<>();

        //1、兼职超管登录后，查询条件不限制；操作员（纯操作员）登录后，默认是能看到所有数据
        boolean isOperatorMan = !tbemploy.getBsalesman() && !tbemploy.getBlead() && tbemploy.getBadminflag() == AdminFlagConstants.NORMAL_USER;
        EmpAuthorityDTO empAuthorityDTO = new EmpAuthorityDTO();
        if (tbemploy.getBadminflag() == AdminFlagConstants.PARTTIME_ADMIN || isOperatorMan) {
            return empAuthorityDTO;
        }

        //2、单位管理员登录后，查看所负责的部门及所有子部门
        if (tbemploy.getBadminflag() == AdminFlagConstants.DEPT_ADMIN) {
            List<Long> manageIdList = Arrays.stream(tbemploy.getSmanagedepts().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            List<Long> alldeptIdList = manageIdList.stream()
                    .map(tbdeptService::getChildDeptId)
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());
            authDeptIdList.addAll(alldeptIdList);
        }

        //3、业务部门负责人登录后，自己所在的部门和兼职部门及所有子部门的数据。
        if (tbemploy.getBlead()) {
            List<Long> manageIdList = new ArrayList<>();
            manageIdList.add(tbemploy.getDeptid());
            if(StrUtil.isNotBlank(tbemploy.getParttimedeptid())){
                Arrays.stream(tbemploy.getParttimedeptid().split(",")).forEach(s -> manageIdList.add(Long.parseLong(s)));
            }

            List<Long> alldeptIdList = manageIdList.stream()
                    .map(tbdeptService::getChildDeptId)
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());
            authDeptIdList.addAll(alldeptIdList);
        }

        //4、业务员（纯业务员）登录后，只能看自己的数据
        boolean isSaleMan = tbemploy.getBsalesman() && !tbemploy.getBlead() && tbemploy.getBadminflag() == AdminFlagConstants.NORMAL_USER;
        if(isSaleMan){
            authDeptIdList.add(tbemploy.getDeptid());
            authEmpIdList.add(tbemploy.getId());
        }
        List<Long> deptIdList = authDeptIdList.stream().distinct().collect(Collectors.toList());
        empAuthorityDTO.getDeptIdList().addAll(deptIdList);
        empAuthorityDTO.getEmpIdList().addAll(authEmpIdList);
        return empAuthorityDTO;

    }


    /**
     * 根据用户id和按钮url获取用户权限范围
     * 方法功能:  根据用户id查询在某个url上是否有权限以及
     *          媒体权限范围  或
     *          部门权限范围
     * @author wangwk
     * @date 2023/11/3 10:05
     * @param userId
     * @param functionUrl
     * @return com.hgzp.advertising.pagemodel.system.dto.EmpAuthorityDTO
     */
    @Override
    public EmpAuthorityDTO getUserRoleScopeByMenu(Long userId, String functionUrl) {
        if(StrUtil.isBlank(functionUrl)){
            throw new IllegalArgumentException("按钮Url不能为空");
        }

        boolean result = false;
        // 记录非媒体范围的按钮id
        List<Long> roleActionId = new ArrayList<>();
        // 记录媒体范围的按钮id
        List<Long> mediaRoleActionId = new ArrayList<>();

        Tbemploy tbemploy = tbemployService.getById(userId);
        if (tbemploy == null) {
            return null;
        }
        List<Tbrole> tbroleList = getRolesByEmpId(userId);
        List<Long> roleIdList = tbroleList.stream().map(Tbrole::getId).collect(Collectors.toList());

        List<Tbmenu> tbmenuList = tbmenuService.getTbMenusByRoldIds(roleIdList);
        for (Tbmenu tbmenu : tbmenuList) {
            if(functionUrl.equals(tbmenu.getSfunctionurl())){
                //对应的url是菜单
                result = true;
                break;
            }
        }

        if(!result){
            //菜单上没找到，找按钮
            List<Tbroleaction> tbroleactionList = tbroleactionService.getTbroleactionByRoleIds(roleIdList);
            for (Tbroleaction tbroleaction : tbroleactionList) {
                if(tbroleaction.getSfunctionurl() != null && tbroleaction.getSfunctionurl().contains(functionUrl)){
                    result = true;
                    boolean buse = tbroleaction.getBuse() != null && tbroleaction.getBuse();
                    if(buse){
                        if(tbroleaction.getSkeycode().contains("media")){
                            mediaRoleActionId.add(tbroleaction.getId());
                        }else{
                            roleActionId.add(tbroleaction.getId());
                        }
                    }
                    break;
                }
            }
        }

        EmpAuthorityDTO empAuthorityDTO = new EmpAuthorityDTO();
        //找到按钮，找权限范围
        if(mediaRoleActionId.size() > 0){
            List<Tbroleactionscope> roleActionScopeList = tbroleactionscopeService.getRoleActionScopeList(mediaRoleActionId);
            if(roleActionScopeList.size() > 0){
                List<Long> businessIdList = roleActionScopeList.stream().map(Tbroleactionscope::getSbusinessid).collect(Collectors.toList());
                empAuthorityDTO.getMediaIdList().addAll(businessIdList);
            }
        }
        if(roleActionId.size() > 0){
            List<Tbroleactionscope> roleActionScopeList = tbroleactionscopeService.getRoleActionScopeList(roleActionId);
            if(roleActionScopeList.size() > 0){
                List<Long> businessIdList = roleActionScopeList.stream().map(Tbroleactionscope::getSbusinessid).collect(Collectors.toList());
                empAuthorityDTO.getDeptIdList().addAll(businessIdList);
            }
        }
        empAuthorityDTO.setHasAuthority(result);


        return empAuthorityDTO;
    }

    /**
     * 根据用户id获取角色列表
     * 方法功能: 根据用户id获取角色列表
     *         若有单独赋权则返回单独赋权
     * @author wangwk
     * @date 2023/11/2 15:47
     * @param userId
     * @return java.util.List<com.hgzp.core.model.Tbrole>
     */
    @Override
    public List<Tbrole> getRolesByEmpId(Long userId){
        Tbrole selfRole = getSelfRoleByEmpId(userId);
        if(selfRole != null){
            return Collections.singletonList(selfRole);
        }
        List<Tbemployrole> tbemployroleList = new LambdaQueryChainWrapper<>(tbemployroleMapper)
                .select(Tbemployrole::getRoleid, Tbemployrole::getEmployid)
                .eq(Tbemployrole::getEmployid, userId)
                .list();

        List<Long> roleIdList = tbemployroleList.stream()
                .map(Tbemployrole::getRoleid)
                .distinct()
                .collect(Collectors.toList());

        if (roleIdList.size() == 0) {
            return Collections.emptyList();
        }
        return this.lambdaQuery()
                .eq(Tbrole::getBuse, true)
                .in(Tbrole::getId, roleIdList)
                .list();
    }

    /**
     * 查询个人有无单独赋权
     * 方法功能:  查询个人有无单独赋权
     * @author wangwk
     * @date 2023/11/2 15:40
     * @param userId
     * @return com.hgzp.core.model.Tbrole
     */
    public Tbrole getSelfRoleByEmpId(Long userId){
        List<Tbemployrole> tbemployroleList = new LambdaQueryChainWrapper<>(tbemployroleMapper)
                .eq(Tbemployrole::getEmployid, userId)
                .list();
        List<Long> roleIdList = tbemployroleList.stream().map(Tbemployrole::getRoleid).distinct().collect(Collectors.toList());
        if(roleIdList.size() == 0){
            return null;
        }
        return this.lambdaQuery()
                .in(Tbrole::getId, roleIdList)
                .eq(Tbrole::getBselfrole, true)
                .eq(Tbrole::getBuse, true)
                .one();
    }


    /**
     * 根据用户id获取用户按钮权限
     * 方法功能: 根据用户id获取用户按钮权限
     * @author wangwk
     * @date 2023/11/7 14:03
     * @param empId
     * @return java.util.List<java.lang.String>
     */
    @Override
    public List<String> getUserPermissions(Long empId){
        List<String> permissions = new ArrayList<>();

        List<Tbrole> tbroleList = getRolesByEmpId(empId);
        if(tbroleList.size() == 0){
            return Collections.emptyList();
        }
        List<Long> roleIdList = tbroleList.stream().map(Tbrole::getId).collect(Collectors.toList());
        List<Tbroleaction> tbroleactionList = tbroleactionService.getTbroleactionByRoleIds(roleIdList);
        for (Tbroleaction tbroleaction : tbroleactionList) {
            if(tbroleaction.getBuse()){
                //按钮有权限
                permissions.add(tbroleaction.getSkeycode());
            }
        }
        permissions = permissions.stream().distinct().collect(Collectors.toList());
        return permissions;
    }





}
