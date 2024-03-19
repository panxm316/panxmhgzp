package com.hgzp.advertisingsys.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.system.dto.DeptEmpImportDTO;
import com.hgzp.advertisingsys.pagemodel.system.vo.OrgTreeVo;
import com.hgzp.advertisingsys.service.system.TbdeptServiceI;
import com.hgzp.advertisingsys.service.system.TbdeptexServiceI;
import com.hgzp.advertisingsys.utils.DataUtil;
import com.hgzp.common.flowable.constants.NodeUserTypeEnum;
import com.hgzp.common.flowable.dto.third.DeptDto;
import com.hgzp.common.flowable.factory.ApiStrategyFactory;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.exception.DataNotExistException;
import com.hgzp.core.exception.DataNotSupportException;
import com.hgzp.core.model.Tbdept;
import com.hgzp.core.model.Tbdeptex;
import com.hgzp.core.model.Tbemploy;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQuery;
import com.hgzp.mapper.system.TbdeptMapper;
import com.hgzp.mapper.system.TbemployMapper;
import com.hgzp.service.system.impl.BaseTbdeptServiceImpl;
import com.hgzp.utils.PinyinUtil;
import com.hgzp.utils.SecurityUtils;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import com.hgzp.utils.security.SM4Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.hgzp.core.constant.system.AdminFlagConstants.*;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbdeptServiceImpl extends BaseTbdeptServiceImpl implements TbdeptServiceI {

    private static final Pattern PATTERN = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9])|(14[57]))\\d{8}$");

    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Autowired
    TbdeptMapper tbdeptMapper;
    @Autowired
    TbdeptexServiceI tbdeptexService;
    @Autowired
    TbemployMapper tbemployMapper;


    @Override
    public List<TreeModel> getSysDeptTree(TreeQuery query, boolean showUnuseDept) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        List<TreeModel> result = new ArrayList<>();

        //根据关键字查询出符合的部门id
        List<Long> queryDeptId = new ArrayList<>();
        if (StrUtil.isNotBlank(query.getQueryKey())) {
            queryDeptId = getQueryDeptId(query.getQueryKey());
            queryDeptId.add(0L);
        }
        //要排除的部门
        List<Long> excludeIdList = Collections.emptyList();
        if (StrUtil.isNotBlank(query.getExcludeIds())) {
            List<String> stringList = Arrays.asList(query.getExcludeIds().split(","));
            excludeIdList = stringList.stream()
                    .map(Long::valueOf)
                    .map(this::getChildDeptId)
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());
        }
        //已经选择的部门
        List<Long> selectIdList = new ArrayList<>();
        List<Long> selectIdParentIdList = new ArrayList<>();
        if (StrUtil.isNotBlank(query.getContainsIds())) {
            selectIdList = Arrays.stream(query.getContainsIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
            selectIdParentIdList = selectIdList.stream().map(this::getParentDeptId).flatMap(Collection::stream).collect(Collectors.toList());
//            selectIdList = CollUtil.addAll(selectIdList, selectParentIdList).stream().distinct().collect(Collectors.toList());
        }

        //普通管理员查询自己部门下的（包括子部门）有权限，以上的父部门皆为导航节点
        if (DEPT_ADMIN == user.getAdminflag()) {

            //子节点id
            List<Long> childIdList = getChildDeptId(user.getDeptid());
            //父节点id
            List<Long> parentIdList = getParentDeptId(user.getDeptid());

            //管理的部门
            String smanagedepts = user.getSmanagedepts();
            if (StrUtil.isNotBlank(smanagedepts)) {
                for (String id : smanagedepts.split(",")) {
                    childIdList.addAll(getChildDeptId(Long.parseLong(id)));
                    parentIdList.addAll(getParentDeptId(Long.parseLong(id)));
                }
            }
            childIdList = childIdList.stream().distinct().collect(Collectors.toList());
            parentIdList = parentIdList.stream().distinct().collect(Collectors.toList());

            //子节点树
            Collection<Long> intersectionId = childIdList;
            if (queryDeptId.size() > 0) {
                //只有有被查询的条件才取交集
                intersectionId = CollUtil.intersection(childIdList, queryDeptId);
            }

            List<Tbdept> childDeptList = getDeptListByQuery(intersectionId, showUnuseDept, excludeIdList, selectIdList);
            List<TreeModel> childTreeModelList = deptConvertTreeModel(childDeptList, TreeModel.UNIT, true);
            result.addAll(childTreeModelList);

            //父节点树
            if (parentIdList.size() > 0) {
                Collection<Long> intersectionParentIdList = parentIdList;
                if (queryDeptId.size() > 0) {
                    //只有有被查询的条件才取交集
                    intersectionParentIdList = CollUtil.intersection(parentIdList, queryDeptId);
                }
                List<Tbdept> parentDeptList = getDeptListByQuery(intersectionParentIdList, showUnuseDept, excludeIdList, selectIdParentIdList);
                List<TreeModel> parentTreeList = deptConvertTreeModel(parentDeptList, TreeModel.UNIT, false);
                result.addAll(parentTreeList);
            }
        }

        if (ADMIN == user.getAdminflag() || PARTTIME_ADMIN == user.getAdminflag()) {
            //超管、 兼职超管查询全部
            selectIdList.addAll(selectIdParentIdList);
            List<Tbdept> tbdeptList = getDeptListByQuery(queryDeptId, showUnuseDept, excludeIdList, selectIdList);
            List<TreeModel> treeModelList = deptConvertTreeModel(tbdeptList, TreeModel.UNIT, true);
            result.addAll(treeModelList);
        }
        return query.isShowRoot() ? addRootDept(result) : result;
    }

    /**
     * getDeptListByQuery
     * 方法功能: 查询部门列表
     *
     * @param containsIdList 包含的部门id列表
     * @param showUnuseDept  是否显示未启用部门
     * @param notInIdList    不包含的部门id列表
     * @param selectIdList   已选择的部门id列表
     * @return java.util.List<com.hgzp.core.model.Tbdept>
     * @author wangwk
     * @date 2023/8/31 14:33
     */
    public List<Tbdept> getDeptListByQuery(Collection<Long> containsIdList, boolean showUnuseDept, Collection<Long> notInIdList, Collection<Long> selectIdList) {
        List<Tbdept> deptList = this.lambdaQuery()
                .and(containsIdList.size() > 0, i ->
                        i.in(containsIdList.size() > 0, Tbdept::getId, containsIdList)
                                .or()
                                .in(selectIdList.size() > 0, Tbdept::getId, selectIdList))
                // .in(containsIdList.size() > 0, Tbdept::getId, containsIdList)
                .notIn(notInIdList.size() > 0, Tbdept::getId, notInIdList)
                .eq(Tbdept::getBinner, true)
                .eq(!showUnuseDept, Tbdept::getBuse, true)
                .orderByAsc(Tbdept::getIsort)
                .list();
        return deptList;
    }


    /**
     * addRootDept
     * 方法功能: 部门树添加[全部部门]节点
     *
     * @param treeModelList
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author wangwk
     * @date 2023/8/29 13:17
     */
    public List<TreeModel> addRootDept(List<TreeModel> treeModelList) {
        for (TreeModel treeModel : treeModelList) {
            if (treeModel.getParentId() == null) {
                treeModel.setParentId(0L);
            }
        }
        //添加一个根节点
        TreeModel root = new TreeModel();
        root.setId(0L);
        root.setName("所有部门");
        root.setChecked(false);
        root.setNocheck(false);
        root.setIconSkin(TreeModel.UNIT);
        treeModelList.add(root);
        return treeModelList;
    }


    /**
     * getQueryDeptId
     * 方法功能:  通过关键字查询时查询出所有符合条件的节点以及父节点id
     *
     * @param querykey
     * @return java.util.List<java.lang.Long>
     * @author wangwk
     * @date 2023/8/29 13:03
     */
    public List<Long> getQueryDeptId(String querykey) {
        List<Long> resultIdList = new ArrayList<>();
        List<Long> idList = this.lambdaQuery()
                .like(Tbdept::getSdeptname, querykey)
                .list()
                .stream()
                .map(Tbdept::getId)
                .collect(Collectors.toList());

        for (Long id : idList) {
            List<Long> parentDeptId = getParentDeptId(id);
            resultIdList.addAll(parentDeptId);
            resultIdList.add(id);
        }

        return resultIdList.stream().distinct().collect(Collectors.toList());
    }


    @Override
    public void saveDept(Tbdept tbdept) {
        if (isExistDept(tbdept)) {
            throw new DataExistException("已存在同名部门");
        }
        if (StrUtil.isNotBlank(tbdept.getSdeptalias())) {
            tbdept.setSdeptalias(tbdept.getSdeptname());
        }
        if (ObjUtil.isEmpty(tbdept.getParentid())) {
            throw new IllegalStateException("父id不应为空");
        }

        tbdept.setId(IdUtil.getSnowflakeNextId());

        innerInterceptor.recoredLog();
        tbdeptMapper.insert(tbdept);
        tbdeptexService.addDeptEx(tbdept);
    }


    @Override
    public void updateDept(Tbdept tbdept) {
        if (isExistDept(tbdept)) {
            throw new DataExistException("已存在同名部门");
        }
        if (!tbdept.getBuse()) {
            //子部门也禁用
            QueryWrapper<Tbdeptex> query = new QueryWrapper<>();
            query.eq("parentid" + tbdept.getIdepth(), tbdept.getId());
            List<Tbdeptex> list = tbdeptexService.list(query);
            List<Long> childIdList = list.stream()
                    .map(Tbdeptex::getDeptid)
                    .collect(Collectors.toList());
            if (childIdList.size() > 0) {
                innerInterceptor.recoredLog();
                tbdeptMapper.updateBuseByIdList(false, childIdList);
            }
        } else {
            //父部门都启用

            List<Long> parentDeptId = getParentDeptId(tbdept.getId());
            if (parentDeptId.size() > 0) {
                innerInterceptor.recoredLog();
                tbdeptMapper.updateBuseByIdList(true, parentDeptId);
            }

        }


        if (tbdept.getParentid() == null || tbdept.getParentid() == 0) {
            tbdept.setIdepth(1);
        } else {
            Tbdept parentDept = tbdeptMapper.selectById(tbdept.getParentid());
            tbdept.setIdepth(parentDept.getIdepth() + 1);
        }


        Tbdept oldDept = tbdeptMapper.selectById(tbdept);
        boolean needRest = ObjectUtil.notEqual(tbdept.getParentid(), oldDept.getParentid());
        boolean needUpdateName = ObjectUtil.notEqual(tbdept.getSdeptname(), oldDept.getSdeptname());
        if (needRest) {
            //调整子部门的深度
            List<Long> childDeptId = getChildDeptId(tbdept.getId());
            if (childDeptId.size() > 0) {
                List<Tbdept> list = this.lambdaQuery()
                        .in(Tbdept::getId, childDeptId)
                        .list();
                //移除第一个是自身
                list.remove(0);
                list.sort(Comparator.comparing(Tbdept::getIdepth));
                for (Tbdept childDept : list) {
                    if (childDept.getParentid().equals(tbdept.getId())) {
                        childDept.setIdepth(tbdept.getIdepth() + 1);
                    } else {
                        Integer idepth = list.stream().filter(dept -> dept.getId().equals(childDept.getParentid())).findFirst().get().getIdepth();
                        childDept.setIdepth(idepth + 1);
                    }
                    if (childDept.getIdepth() > 5) {
                        throw new DataNotSupportException("不允许调整后的部门超过5级");
                    }
                    this.updateById(childDept);
                }
            }
        }
        innerInterceptor.recoredLog();
        tbdeptMapper.updateById(tbdept);
        if (needRest) {
            tbdeptexService.resetDeptEx();
        } else if (needUpdateName) {
            tbdeptexService.updateDeptExName(tbdept);
        }
    }

    @Override
    public void batchUdpateDept(String ids, Long pid) {
        String[] split = ids.split(",");
        //父部门不能调整到子部门下
        Long finalPid = pid;
        boolean match = Arrays.stream(split)
                .map(Long::parseLong)
                .map(this::getChildDeptId)
                .flatMap(Collection::stream)
                .distinct()
                .anyMatch(id -> id.equals(finalPid));

        if (match) {
            throw new DataNotSupportException("部门不允许调整到自己的子部门");
        }


        int depth = 0;
        if (pid != null && pid != 0) {
            List<Long> parentDeptIdList = getParentDeptId(pid);
            depth = parentDeptIdList.size() + 1;
        }

        //调整后的深度不能超过5级
        for (String childId : split) {
            if (pid != null && pid != 0) {
                List<Long> childDeptIdList = getChildDeptId(Long.parseLong(childId));
                List<Tbdept> tbdeptList = this.lambdaQuery().in(Tbdept::getId, childDeptIdList).list();
                Integer maxIdepth = tbdeptList.stream().max(Comparator.comparing(Tbdept::getIdepth)).get().getIdepth();
                Integer minIdepth = tbdeptList.stream().min(Comparator.comparing(Tbdept::getIdepth)).get().getIdepth();
                if (depth + (maxIdepth - minIdepth + 1) > 5) {
                    throw new DataNotSupportException("不允许调整后的部门超过5级");
                }
            }
        }

        if (pid != null && pid == 0) {
            pid = null;
        }

        //将调整部门的父部门修改
        List<Long> childIdList = Arrays.stream(split)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        //调整子部门的深度
        for (Long id : childIdList) {
            List<Long> childDeptId = getChildDeptId(id);
            if (childDeptId.size() > 0) {
                List<Tbdept> list = this.lambdaQuery()
                        .in(Tbdept::getId, childDeptId)
                        .list();
                //移除第一个是自身
                list.remove(0);
                list.sort(Comparator.comparing(Tbdept::getIdepth));
                for (Tbdept childDept : list) {
                    if (childDept.getParentid().equals(id)) {
                        childDept.setIdepth(depth + 1 + 1);
                    } else {
                        Integer idepth = list.stream().filter(dept -> dept.getId().equals(childDept.getParentid())).findFirst().get().getIdepth();
                        childDept.setIdepth(idepth + 1);
                    }
                    this.updateById(childDept);
                }
            }
        }

        innerInterceptor.recoredLog();
        tbdeptMapper.updatePidIdepthByIdList(pid, depth + 1, childIdList);

        tbdeptexService.resetDeptEx();
    }


    @Override
    public void deleteDept(String ids) {
        String[] split = ids.split(",");
        List<Long> deleteIdList = Arrays.stream(split).map(Long::parseLong).collect(Collectors.toList());
        //存在子部门不能删除
        Long childDeptCount = new LambdaQueryChainWrapper<>(tbdeptMapper)
                .in(Tbdept::getParentid, deleteIdList)
                .count();
        if (childDeptCount > 0) {
            throw new DataExistException("部门下还有子部门，不能删除!");
        }

        //部门下存在人不能删除
        Long empCount = new LambdaQueryChainWrapper<>(tbemployMapper)
                .in(Tbemploy::getDeptid, deleteIdList)
                .count();
        if (empCount > 0) {
            throw new DataExistException("部门下还有人员，不能删除!");
        }
        //TODO 未来这里还有业务判断

        // 删除管理部门
        this.deleteManageDept(ids);

        innerInterceptor.recoredLog();
        tbdeptMapper.deleteBatchIds(deleteIdList);
        tbdeptexService.resetDeptEx();
    }

    /**
     * deleteManageDept
     * 方法功能: 删除管理部门
     *
     * @param ids
     * @return void
     * @author yanz
     * @date 2024/3/13 16:21
     */
    @Override
    public void deleteManageDept(String ids) {
        String[] split = ids.split(",");
        List<Long> deleteIdList = Arrays.stream(split).map(Long::parseLong).collect(Collectors.toList());
        List<Tbemploy> tbemploys = tbemployMapper.selectList(null);
        for (Tbemploy tbemploy : tbemploys) {
            String smanagedepts = tbemploy.getSmanagedepts();
            if (StrUtil.isNotBlank(smanagedepts)) {
                String[] smanagedeptsArray = smanagedepts.split(",");
                List<String> smanagedeptsList = new ArrayList<>(Arrays.asList(smanagedeptsArray));
                Iterator<String> iterator = smanagedeptsList.iterator();
                while (iterator.hasNext()) {
                    String smanagedept = iterator.next();
                    if (deleteIdList.contains(Long.parseLong(smanagedept))) {
                        iterator.remove();
                    }
                }
                String smanagedeptsNew = String.join(",", smanagedeptsList);
                tbemploy.setSmanagedepts(smanagedeptsNew);
                innerInterceptor.recoredLog();
                tbemployMapper.updateById(tbemploy);
            }
        }
    }

    @Override
    public Boolean getParentDeptBuse(Long id) {
        if (id == null || id == 0) {
            return true;
        }
        return this.getById(id).getBuse();
    }


    public boolean isExistDept(Tbdept tbdept) {
        Long count = new LambdaQueryChainWrapper<>(tbdeptMapper)
                .eq(tbdept.getParentid() != null, Tbdept::getParentid, tbdept.getParentid())
                .eq(Tbdept::getSdeptname, tbdept.getSdeptname())
                .ne(tbdept.getId() != null, Tbdept::getId, tbdept.getId())
                .count();
        return count > 0;
    }


    /**
     * deptConvertTreeModel
     * 方法功能:  将数据库部门对象转换成树对象
     *
     * @param tbdeptList
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author wangwk
     * @date 2023/8/18 13:41
     */
    private List<TreeModel> deptConvertTreeModel(List<Tbdept> tbdeptList, String iconSkin, boolean showCheck) {
        List<TreeModel> treeModelList = new ArrayList<>();
        for (Tbdept tbdept : tbdeptList) {
            TreeModel treeModel = new TreeModel();
            treeModel.setId(tbdept.getId());
            treeModel.setIconSkin(iconSkin);
            treeModel.setParentId(tbdept.getParentid());
            treeModel.setName(tbdept.getSdeptname());
            treeModel.setNocheck(!showCheck);
            treeModelList.add(treeModel);
        }
        return treeModelList;
    }

    @Override
    public void importDeptAndEmpData(File file) throws Exception {
        boolean singlesite = false;
        //是否有第六列集团信息
        boolean hasOrg = false;

        ExcelReader reader = ExcelUtil.getReader(file);
        List<DeptEmpImportDTO> exceldataList = reader.readAll(DeptEmpImportDTO.class);

        int datacount = exceldataList.size();
        List<Object> firstLineData = reader.read(0).get(0);

        StringBuffer errMsg = new StringBuffer();
        if (!"所在媒体".equals(firstLineData.get(0).toString().trim())) {
            errMsg.append("请检查第一列是否是所在媒体列!<br>");
        }
        if (!"部门名称".equals(firstLineData.get(1).toString().trim())) {
            errMsg.append("请检查第二列是否是部门名称列!<br>");
        }
        if (!"姓名".equals(firstLineData.get(2).toString().trim())) {
            errMsg.append("请检查第三列是否是姓名列!<br>");
        }
        if (!"手机号".equals(firstLineData.get(3).toString().trim())) {
            errMsg.append("请检查第四列是否是手机号列!<br>");
        }
        if (!"登录账号".equals(firstLineData.get(4).toString().trim())) {
            errMsg.append("请检查第五列是否是登录账号列!<br>");
        }
        if (!"登录密码".equals(firstLineData.get(5).toString().trim())) {
            errMsg.append("请检查第六列是否是密码列!<br>");
        }
        if (errMsg.length() > 0) {
            throw new IllegalArgumentException(errMsg.toString());
        }


        Set<String> orgSet = new HashSet<>();
        for (int i = 0; i < datacount; i++) {
            DeptEmpImportDTO lineData = exceldataList.get(i);
            if (StrUtil.isBlank(lineData.getSecondDeptName())) {
                errMsg.append("第" + (i + 2) + "行 应填写所在媒体!<br>");
            }
            if (!singlesite) {
                //汇畅无部门提示但是可以正常导入  ，此处避免带来歧义先去掉提示
//                if (StrUtil.isBlank(lineData.getThirdDeptName())) {
//                    errMsg.append("第" + (i + 2) + "行 应填写部门名称!<br>");
//                }
            }
            if (StrUtil.isBlank(lineData.getEmpName())) {
                errMsg.append("第" + (i + 2) + "行 应填写姓名!<br>");
            }
            //检查第六列 若一行有值则视为有集团单位  后面在进行所有行非空校验
            if (!hasOrg) {
                hasOrg = StrUtil.isNotBlank(lineData.getRootDeptName());
            }
            orgSet.add(lineData.getRootDeptName());
        }


        if (singlesite) {
            //单节点部署必须有第六列
            //有且仅有一个一级单位
            if (!hasOrg) {
                throw new DataNotExistException("集团单位必须填写！");
            }
            if (orgSet.size() > 1) {
                throw new DataNotExistException("单节点部署，集团单位只能有一个！");
            }
        }

        // 查询所有人员
        List<Tbemploy> tbemployList = tbemployMapper.selectList(null);

        //手机号判重
        List<Integer> repeatphoneLineNumlist = new ArrayList<>();
        for (int i = 0; i < datacount; i++) {
            DeptEmpImportDTO lineData = exceldataList.get(i);
            for (Tbemploy t : tbemployList) {
                if (StrUtil.isNotBlank(t.getSphone()) && lineData.getPhone().trim().equals(SM4Utils.decrypt(t.getSphone().trim())) && StrUtil.isNotBlank(lineData.getPhone())) {
                    repeatphoneLineNumlist.add(i);
                }
            }
        }
        if (repeatphoneLineNumlist.size() > 0) {
            if (repeatphoneLineNumlist.size() == 1) {
                errMsg.append("第" + (repeatphoneLineNumlist.get(0) + 2) + "行手机号已注册过!");
            } else {
                for (int m = 0; m < repeatphoneLineNumlist.size(); m++) {
                    if (m == 0) {
                        errMsg.append("第" + (repeatphoneLineNumlist.get(m) + 2) + "、");
                    } else if (m == repeatphoneLineNumlist.size() - 1) {
                        errMsg.append((repeatphoneLineNumlist.get(m) + 2) + "行手机号已注册过!<br>");
                    } else {
                        errMsg.append((repeatphoneLineNumlist.get(m) + 2) + "、");
                    }
                }
            }
        }

        long maxemp = tbemployList.stream().map(Tbemploy::getIsort).max(Long::compareTo).orElse(0L);

        List<Tbdept> isortlist = tbdeptMapper.selectList(null);
        //取一级节点的最大值
        int firstisort = isortlist.stream().map(Tbdept::getIsort).max(Integer::compareTo).orElse(0);

        //开始导入
        for (int i = 0; i < datacount; i++) {
            //临时一级，二级部门
            String firstdept = "", seconddept = "", thirddept = "";
            DeptEmpImportDTO lineData = exceldataList.get(i);

            if (!PATTERN.matcher(lineData.getPhone().trim()).matches() && !"".equals(lineData.getPhone().trim())) {
                errMsg.append("第" + (i + 2) + "行手机号只能是13、15或18开头的11位整数!<br>");
                continue;
            }
            if (StrUtil.isBlank(lineData.getPhone().trim()) && StrUtil.isBlank(lineData.getEmpName().trim())) {
                continue;
            }
            boolean phoneflag = false;
            for (Integer t : repeatphoneLineNumlist) {
                if (i == t) {
                    phoneflag = true;
                    break;
                }
            }
            //手机号重复的不导入
            if (phoneflag) {
                continue;
            }

            //重复手机号
            List<Integer> temp = new ArrayList<>();
            for (int n = datacount - 1; n > 0; n--) {
                if (!"".endsWith(exceldataList.get(n).getPhone().trim()) && exceldataList.get(n).getPhone().equals(lineData.getPhone()) && i != n) {
                    temp.add(i);
                    temp.add(n);
                }
            }

            //若有重复手机号
            if (temp.size() > 1) {
                //剔除重复元素
                List<Integer> numlist = temp.stream().distinct().collect(Collectors.toList());
                if (numlist.size() > 0) {
                    if (numlist.size() == 1) {
                        errMsg.append("第" + (numlist.get(0) + 2) + "行手机号重复!");
                    } else {
                        for (int m = 0; m < numlist.size(); m++) {
                            if (m == 0) {
                                String msg = "第" + (numlist.get(m) + 2) + "、";
                                //有重复不在追加字符串
                                if (!errMsg.toString().contains(msg)) {
                                    errMsg.append(msg);
                                }
                            } else if (m == numlist.size() - 1) {
                                String msg = (numlist.get(m) + 2) + "行手机号重复!<br>";
                                if (!errMsg.toString().contains(msg)) {
                                    errMsg.append(msg);
                                }
                            } else {
                                String msg = (numlist.get(m) + 2) + "、";
                                if (!errMsg.toString().contains(msg)) {
                                    errMsg.append(msg);
                                }
                            }
                        }
                    }
                }
                continue;
            }


            //第一列部门为空不导入
            if (StrUtil.isBlank(lineData.getSecondDeptName().trim())) {
                continue;
            }


            if (hasOrg) {
                if (StrUtil.isBlank(lineData.getRootDeptName().trim())) {
                    errMsg.append("第" + (i + 1) + "行 应填写所在集团单位!<br>");
                    continue;
                }

                firstdept = lineData.getRootDeptName();
                seconddept = lineData.getSecondDeptName();
                thirddept = lineData.getThirdDeptName();
            } else {
                //如果没有集团根部二级单位不存在也不导入
                if (StrUtil.isBlank(lineData.getSecondDeptName().trim())) {
                    continue;
                }
                firstdept = lineData.getSecondDeptName();
                seconddept = lineData.getThirdDeptName();
            }


            List<Tbdept> isortlist2 = this.lambdaQuery().eq(Tbdept::getIdepth, 1).list();
            String finalFirstdept = firstdept;
            boolean hasFirst = isortlist2.stream().anyMatch(e -> finalFirstdept.equals(e.getSdeptname()));

            Tbemploy emp = new Tbemploy();
            long empid = IdUtil.getSnowflakeNextId();
            emp.setId(empid);
            if (!hasFirst) {
                //若媒体不存在则添加
                //保存一级节点
                Tbdept dept = saveBuildTbdept(firstisort + i, firstdept, 1, null);
                //保存二级节点
                Tbdept tbdept2 = saveBuildTbdept(1, seconddept, 2, dept.getId());
                emp.setDeptid(tbdept2.getId());
                //如果三级节点存在保存 三级
                if (StringUtils.hasText(thirddept)) {
                    Tbdept tbdept3 = saveBuildTbdept(1, thirddept, 3, tbdept2.getId());
                    emp.setDeptid(tbdept3.getId());
                }
            } else {
                //若媒体存在则只添加部门或人员信息
                //获取已存在的一级节点
                Tbdept first = this.lambdaQuery().eq(Tbdept::getSdeptname, firstdept).eq(Tbdept::getIdepth, 1).one();

                //防止添加到别的相同名称的部门下
                Tbdept tempsecond = this.lambdaQuery()
                        .eq(Tbdept::getSdeptname, seconddept)
                        .eq(Tbdept::getParentid, first.getId()).one();

                //若2级节点不存在则新加
                if (tempsecond == null) {
                    //先查一级节点获取所有子节点 取isort最大号
                    Integer maxisort = this.getMaxSort();

                    Tbdept scondept = saveBuildTbdept(maxisort, seconddept, 2, first.getId());
                    emp.setDeptid(scondept.getId());
                    //如果三级节点存在保存 三级
                    if (StringUtils.hasText(thirddept)) {
                        Tbdept tbdept3 = saveBuildTbdept(1, thirddept, 3, scondept.getId());
                        emp.setDeptid(tbdept3.getId());
                    }
                } else {
                    // peij 20230803 三级部门名称为空不处理
                    if (StringUtils.hasText(thirddept)) {
                        // peij 20230803 判断三级部门名称是否存在
                        Tbdept tempthir = this.lambdaQuery()
                                .eq(Tbdept::getParentid, tempsecond.getId())
                                .eq(Tbdept::getSdeptname, thirddept)
                                .one();
                        // peij 20230803 如果不存在获取最大isort新增
                        if (tempthir == null) {
                            Integer maxisort3 = this.getMaxSort();
                            Tbdept tbdept3 = saveBuildTbdept(maxisort3, thirddept, 3, tempsecond.getId());
                            emp.setDeptid(tbdept3.getId());
                        } else {
                            emp.setDeptid(tempthir.getId());
                        }
                    } else {
                        emp.setDeptid(tempsecond.getId());
                    }
                }
            }

            //登录名 不填就获取姓名全拼 例：chixq
            String piny = "";
            if (StrUtil.isBlank(lineData.getLoginName().trim())) {
                piny = PinyinUtil.converterTofirstall(lineData.getEmpName().trim());
            } else {
                piny = lineData.getLoginName().trim();
            }
            List<Tbemploy> emps = new LambdaQueryChainWrapper<>(tbemployMapper).likeRight(Tbemploy::getSloginname, piny).list();
            //登录名不存在 直接存储
            if (emps.size() == 0) {
                emp.setSloginname(piny);
                //密码
                if (StrUtil.isBlank(lineData.getPassword().trim())) {
                    emp.setSpassword(SecurityUtils.encryptPassword(piny + "@123456"));
                } else {
                    emp.setSpassword(SecurityUtils.encryptPassword(lineData.getPassword().trim()));
                }
            } else {
                // 登录名存在 叠加
                int maxnum = 0;
                //最终保存的登录名
                String py = "";
                for (Tbemploy b : emps) {
                    if (!b.getSloginname().equals(piny) && (b.getSloginname().contains(piny + "0")
                            || b.getSloginname().contains(piny + "1") || b.getSloginname().contains(piny + "2")
                            || b.getSloginname().contains(piny + "3") || b.getSloginname().contains(piny + "4")
                            || b.getSloginname().contains(piny + "5") || b.getSloginname().contains(piny + "6")
                            || b.getSloginname().contains(piny + "7") || b.getSloginname().contains(piny + "8")
                            || b.getSloginname().contains(piny + "9"))) {
                        int num = Integer.parseInt(b.getSloginname().substring(piny.length()));
                        if (num > maxnum) {
                            maxnum = num;
                        }
                    }
                }
                if (maxnum >= 9) {
                    py = piny + (maxnum + 1);
                } else {
                    py = piny + "0" + (maxnum + 1);
                }
                emp.setSloginname(py);
                //密码
                if (StrUtil.isBlank(lineData.getPassword().trim())) {
                    emp.setSpassword(SecurityUtils.encryptPassword(py + "@123456"));
                } else {
                    emp.setSpassword(SecurityUtils.encryptPassword(lineData.getPassword().trim()));
                }
            }
            emp.setSusername(lineData.getEmpName().trim());
            emp.setBuse(true);
            emp.setSphone(SM4Utils.encrypt(lineData.getPhone().trim()));
            emp.setIsort(maxemp + i + 1);
            emp.setBadminflag(NORMAL_USER);
            emp.setBinner(true);
            innerInterceptor.recoredLog();
            tbemployMapper.insert(emp);
        }

        if (errMsg.length() > 0) {
            throw new IllegalArgumentException(errMsg.toString());
        }

    }

    private Tbdept saveBuildTbdept(int maxIsort, String deptName, int depth, Long parentDeptId) throws Exception {
        long deptid = IdUtil.getSnowflakeNextId();
        Tbdept dept = new Tbdept();
        dept.setSdeptname(deptName);
        dept.setSdeptalias(deptName);
        dept.setBuse(true);
        dept.setIsort(maxIsort + 1);
        dept.setId(deptid);
        dept.setIdepth(depth);
        dept.setBflagroot(false);
        dept.setBinner(true);
        dept.setParentid(parentDeptId);
        innerInterceptor.recoredLog();
        this.save(dept);
        tbdeptexService.addDeptEx(dept);
        return dept;
    }

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

        return Json.success(dict);
    }
}
