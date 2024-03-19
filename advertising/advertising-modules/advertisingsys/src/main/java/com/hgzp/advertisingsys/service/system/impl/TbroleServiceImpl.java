package com.hgzp.advertisingsys.service.system.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.system.ActiveModel;
import com.hgzp.advertisingsys.pagemodel.system.RoleMenuModel;
import com.hgzp.advertisingsys.pagemodel.system.dto.RoleActionDTO;
import com.hgzp.advertisingsys.pagemodel.system.dto.RoleDTO;
import com.hgzp.advertisingsys.pagemodel.system.vo.OrgTreeVo;
import com.hgzp.advertisingsys.pagemodel.system.vo.RoleVO;
import com.hgzp.advertisingsys.pagemodel.system.ScopeModel;
import com.hgzp.advertisingsys.service.system.*;
import com.hgzp.annotation.LogModelAddCache;
import com.hgzp.annotation.LogModelRemoveCache;
import com.hgzp.core.emnus.LogTypes;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.*;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.system.TbemployroleMapper;
import com.hgzp.mapper.system.TbmenuMapper;
import com.hgzp.mapper.system.TbroleMapper;
import com.hgzp.mapper.system.TbrolemenuMapper;
import com.hgzp.service.RedisService;
import com.hgzp.service.system.impl.BaseTbroleServiceImpl;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.hgzp.core.constant.system.AdminFlagConstants.DEPT_ADMIN;

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
    TbroleMapper tbroleMapper;
    @Autowired
    TbmenuMapper tbmenuMapper;
    @Autowired
    TbrolemenuMapper tbrolemenuMapper;
    @Autowired
    TbemployroleMapper tbemployroleMapper;
    @Autowired
    TbroledeptServiceI tbroledeptService;
    @Autowired
    TbrolemenuServiceI tbrolemenuService;
    @Autowired
    TbmenuactionServiceI tbmenuactionService;
    @Autowired
    TbroleactionServiceI tbroleactionService;
    @Autowired
    TbmenuactionscopeServiceI tbmenuactionscopeService;
    @Autowired
    TbroleactionscopeServiceI tbroleactionscopeService;
    @Autowired
    RedisService redisService;
    @Autowired
    TwsysoperatelogServiceI twsysoperatelogService;


    @Override
    public IPage<RoleVO> getRolePageList(IPage<Tbrole> page, BaseQueryInfo info) throws Exception {
        IPage<Tbrole> tbroleIPage = getTbroleIPage(page, info);

        List<Tbrole> tbroleList = tbroleIPage.getRecords();

        //查询出的所有角色id
        List<Long> roleIdList = tbroleList.stream().map(Tbrole::getId).collect(Collectors.toList());
        //查出所有的部门
        Map<Long, List<Tbdept>> deptListGroupByRoleIds = tbroledeptService.getDeptListGroupByRoleIds(roleIdList);
        //查出所有的菜单
        Map<Long, List<Tbmenu>> menuListGroupByRoldId = tbrolemenuService.getMenuListGroupByRoldId(roleIdList);

        //转成vo
        List<RoleVO> voList = new ArrayList<>();
        for (Tbrole tbrole : tbroleList) {
            RoleVO vo = new RoleVO(tbrole);
            String deptNames = deptListGroupByRoleIds.get(tbrole.getId())
                    .stream()
                    .map(Tbdept::getSdeptname)
                    .collect(Collectors.joining(","));

            List<Tbmenu> tbmenuList = menuListGroupByRoldId.get(tbrole.getId());
            String menuIds =
                    tbmenuList.stream().map(Tbmenu::getId).map(Objects::toString).collect(Collectors.joining(","));
            String menuNames =
                    tbmenuList.stream().map(Tbmenu::getSname).map(Objects::toString).collect(Collectors.joining(","));

            vo.setMenuIds(menuIds);
            vo.setMenuNames(menuNames);
            vo.setDeptNames(deptNames);
            voList.add(vo);
        }
        Page<RoleVO> result = new Page<>();
        result.setTotal(tbroleIPage.getTotal());
        result.setRecords(voList);
        return result;
    }

    /**
     * getTbroleIPage
     * 方法功能:  分页查询所有角色
     *
     * @param page
     * @param info
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbrole>
     * @author wangwk
     * @date 2023/9/2 9:24
     */
    private IPage<Tbrole> getTbroleIPage(IPage<Tbrole> page, BaseQueryInfo info) throws Exception {
        LoginUser loginUser = WebUtil.getLoginUser();

        List<Long> queryRoleIdList = new ArrayList<>();
        //子管理员只显示自己部门下的角色
        if (DEPT_ADMIN == loginUser.getAdminflag()) {
            if (StrUtil.isNotBlank(loginUser.getSmanagedepts())) {
                String[] deptIdArray = loginUser.getSmanagedepts().split(",");
                List<Long> roleIdList = tbroledeptService.lambdaQuery()
                        .in(Tbroledept::getDeptid, Arrays.asList(deptIdArray))
                        .list()
                        .stream()
                        .map(Tbroledept::getRoleid)
                        .collect(Collectors.toList());
                queryRoleIdList.addAll(roleIdList);
            }
        }

        //超管、兼职超管显示全部角色
        IPage<Tbrole> tbroleIPage = this.lambdaQuery()
                .eq(Tbrole::getBselfrole, false)
                .in(queryRoleIdList.size() > 0, Tbrole::getId, queryRoleIdList)
                .like(StrUtil.isNotBlank(info.getQueryKey()), Tbrole::getSname, info.getQueryKey())
                .page(page);
        return tbroleIPage;
    }

    @Override
    public List<DataCombo> getRoleDataCombo() throws Exception {
        //不分页
        Page<Tbrole> page = new Page<>(1, -1);
        IPage<Tbrole> tbroleIPage = getTbroleIPage(page, new BaseQueryInfo());
        List<Tbrole> records = tbroleIPage.getRecords();
        List<DataCombo> collect = records.stream()
                .map(r -> new DataCombo(r.getId().toString(), r.getSname()))
                .collect(Collectors.toList());
        return collect;

    }


    @Override
    public Tbrole saveRole(RoleDTO roleModel) throws Exception {
        Tbrole tbrole = new Tbrole();
        BeanUtils.copyProperties(roleModel, tbrole);
        if (roleNameExist(tbrole.getSname(), tbrole.getId())) {
            throw new DataExistException("角色名称已存在");
        }
        long roleId = IdUtil.getSnowflakeNextId();
        tbrole.setId(roleId);
        this.save(tbrole);

        //保存菜单关联角色表
        if (StrUtil.isNotBlank(roleModel.getMenuIds())) {
            String[] split = roleModel.getMenuIds().split(",");
            Arrays.stream(split).forEach(item -> {
                Tbrolemenu tbrolemenu = new Tbrolemenu();
                tbrolemenu.setRoleid(roleId);
                tbrolemenu.setMenuid(Long.valueOf(item));
                tbrolemenuService.save(tbrolemenu);
            });
        }

        //角色关联部门
        LoginUser loginUser = WebUtil.getLoginUser();
        if (DEPT_ADMIN == loginUser.getAdminflag()) {
            String managedeptIds = loginUser.getSmanagedepts();
            if (StrUtil.isNotBlank(managedeptIds)) {
                String[] deptIds = managedeptIds.split(",");
                Arrays.stream(deptIds).forEach(item -> {
                    Tbroledept tbroledept = new Tbroledept();
                    tbroledept.setRoleid(roleId);
                    tbroledept.setDeptid(Long.valueOf(item));
                    tbroledeptService.save(tbroledept);
                });
            }
        }
        //单独赋权保存人员关联表
        if (roleModel.getBselfrole() != null && roleModel.getBselfrole()) {
            Tbemployrole employrole = new Tbemployrole();
            employrole.setRoleid(roleId);
            employrole.setEmployid(roleModel.getEmployId());
            tbemployroleMapper.insert(employrole);
        }

        // 操作日志
        roleModel.setId(roleId);
        String compareStr = twsysoperatelogService.compareTowObject(null, roleModel);
        Twsysoperatelog twsysoperatelog = new Twsysoperatelog();
        twsysoperatelog.setSlogtype(LogTypes.LOG_TYPES_INSERT.getTypeStr());
        twsysoperatelog.setSlogname("角色新增");
        twsysoperatelog.setStablename("tbrole");
        twsysoperatelog.setRecordmasterid(roleId);
        twsysoperatelog.setSnewvalue(compareStr);
        twsysoperatelogService.saveAsyncSysoperatelogWithoutUser(twsysoperatelog);

        return tbrole;
    }

    @Override
    @LogModelRemoveCache
    public void updateRole(RoleDTO roleModel) throws Exception {
        Tbrole tbrole = new Tbrole();
        BeanUtils.copyProperties(roleModel, tbrole);
        if (roleNameExist(tbrole.getSname(), tbrole.getId())) {
            throw new DataExistException("角色名称已存在");
        }

        List<Tbrolemenu> tbrolemenuList = tbrolemenuService.lambdaQuery()
                .eq(Tbrolemenu::getRoleid, tbrole.getId())
                .list();
        for (Tbrolemenu tbrolemenu : tbrolemenuList) {
            if (!roleModel.getMenuIds().contains(tbrolemenu.getMenuid().toString())) {
                //删除已经没有的菜单
                tbrolemenuService.removeById(tbrolemenu);
            }
        }

        List<Long> oldMenuIdList = tbrolemenuList.stream()
                .map(Tbrolemenu::getMenuid)
                .collect(Collectors.toList());
        //保存新增的菜单
        List<String> newMenuIdList = Arrays.asList(roleModel.getMenuIds().split(","));
        for (String menuId : newMenuIdList) {
            if (!oldMenuIdList.contains(Long.valueOf(menuId))) {
                Tbrolemenu tbrolemenu = new Tbrolemenu();
                tbrolemenu.setMenuid(Long.valueOf(menuId));
                tbrolemenu.setRoleid(tbrole.getId());
                tbrolemenuService.save(tbrolemenu);
            }
        }
        this.updateById(tbrole);
        // 更新日志
        RoleDTO roleDTOOld = redisService.getCacheObject(roleModel.getCacheDTOKey());
        String compareStr = twsysoperatelogService.compareTowObject(roleDTOOld, roleModel);
        if (StrUtil.isNotBlank(compareStr)) {
            Twsysoperatelog twsysoperatelog = new Twsysoperatelog();
            twsysoperatelog.setSlogtype(LogTypes.LOG_TYPES_UPDATE.getTypeStr());
            twsysoperatelog.setSlogname("角色更新");
            twsysoperatelog.setStablename("tbrole");
            twsysoperatelog.setRecordmasterid(roleModel.getId());
            twsysoperatelog.setSnewvalue(compareStr);
            twsysoperatelogService.saveAsyncSysoperatelogWithoutUser(twsysoperatelog);
        }
    }

    @Override
    public void deleteRoleById(String id) throws Exception {
        RoleDTO roleModel = this.getRoleById(id);
        tbroleMapper.deleteById(id);
        // 操作日志
        String compareStr = twsysoperatelogService.compareTowObject(roleModel, null);
        Twsysoperatelog twsysoperatelog = new Twsysoperatelog();
        twsysoperatelog.setSlogtype(LogTypes.LOG_TYPES_DELETE.getTypeStr());
        twsysoperatelog.setSlogname("角色删除");
        twsysoperatelog.setStablename("tbrole");
        twsysoperatelog.setRecordmasterid(roleModel.getId());
        twsysoperatelog.setSnewvalue(compareStr);
        twsysoperatelogService.saveAsyncSysoperatelogWithoutUser(twsysoperatelog);
    }

    /***
     * roleNameExist
     * 方法功能: 判断角色名是否存在
     * @author wangwk
     * @date 2023/8/24 15:28
     * @return boolean
     */
    public boolean roleNameExist(String sname, Long id) {
        return this.lambdaQuery()
                .eq(Tbrole::getSname, sname)
                .ne(id != null, Tbrole::getId, id)
                .count() > 0;
    }

    @Override
    @LogModelAddCache
    public RoleActionDTO getRoleActionScopeByRoleId(Long roleId) {
        RoleActionDTO roleActionDTO = new RoleActionDTO();
        List<RoleMenuModel> listRoleMenuList = new ArrayList<>();

        List<Tbrolemenu> tbrolemenuList = tbrolemenuMapper.getAuthMenuListById(roleId);
        if (tbrolemenuList.size() == 0) {
            roleActionDTO.setRoleId(roleId);
            roleActionDTO.setRoleMenuModelList(listRoleMenuList);
            return roleActionDTO;
        }
        List<Long> menuIdList = tbrolemenuList.stream().map(Tbrolemenu::getMenuid).collect(Collectors.toList());
        //角色关联的所有菜单
        Map<Long, List<Tbmenu>> menuGroupById =
                tbmenuMapper.selectBatchIds(menuIdList).stream().collect(Collectors.groupingBy(Tbmenu::getId));
        //菜单关联的所有按钮
        Map<Long, List<Tbmenuaction>> menuActionListGroupByMenuId =
                tbmenuactionService.getMenuActionListGroupByMenuId(menuIdList);
        //菜单关联按钮的范围
        List<Long> menuactionIdList =
                tbmenuactionService.getMenuActionListByMenuIdList(menuIdList).stream().map(Tbmenuaction::getId).collect(Collectors.toList());
        Map<Long, List<Tbscope>> scopeGroupByMenuActionId =
                tbmenuactionscopeService.getScopeGroupByMenuActionId(menuactionIdList);
        //角色拥有的菜单关联的所有按钮
        List<Long> roleMenuIdList = tbrolemenuList.stream().map(Tbrolemenu::getId).collect(Collectors.toList());
        Map<Long, List<Tbroleaction>> roleActionListGroupByRoleMenuId =
                tbroleactionService.getRoleActionListGroupByRoleMenuId(roleMenuIdList);
        //角色按钮关联的范围
        List<Long> roleActionIdList =
                tbroleactionService.getRoleActionListByRoleMenuId(roleMenuIdList).stream().map(Tbroleaction::getId).collect(Collectors.toList());
        Map<Long, List<Tbroleactionscope>> roleActionScopeGroupByActionId =
                tbroleactionscopeService.getRoleActionScopeGroupByActionId(roleActionIdList);

        for (Tbrolemenu tbrolemenu : tbrolemenuList) {
            Tbmenu tbmenu = menuGroupById.get(tbrolemenu.getMenuid()).get(0);

            RoleMenuModel roleMenuModel = new RoleMenuModel();
            roleMenuModel.setMenuname(tbmenu.getSname());
            roleMenuModel.setMenuid(tbmenu.getId());
            roleMenuModel.setMenutype(tbmenu.getSgroup());
            roleMenuModel.setChecked(false);

            List<Tbmenuaction> tbmenuactions = menuActionListGroupByMenuId.get(tbmenu.getId());
            tbmenuactions = Optional.ofNullable(tbmenuactions).orElse(new ArrayList<>());
            List<Tbroleaction> tbroleactions = roleActionListGroupByRoleMenuId.get(tbrolemenu.getId());
            tbroleactions = Optional.ofNullable(tbroleactions).orElse(new ArrayList<>());

            List<ActiveModel> activelist = new ArrayList<>();

            for (Tbmenuaction tbmenuaction : tbmenuactions) {
                List<ScopeModel> scopelist = new ArrayList<>();
                //按钮有效
                if (tbmenuaction.getBuse()) {
                    ActiveModel active = new ActiveModel(tbmenuaction.getSkeycode(), tbmenuaction.getSname(),
                            tbmenuaction.getSfunctionurl(),
                            tbmenuaction.getBmodify() != null && tbmenuaction.getBmodify(),
                            tbmenuaction.getSgroupkey());
                    List<Tbroleactionscope> roleactionscopeset = new ArrayList<>();
                    //找出角色上是否已经勾选过这个按钮
                    Optional<Tbroleaction> tbroleaction =
                            tbroleactions.stream().filter(ra -> ra.getSkeycode().equals(tbmenuaction.getSkeycode())).findFirst();
                    if (tbroleaction.isPresent()) {
                        if (tbroleaction.get().getBuse()) {
                            // 如果角色拥有权限,设置按钮为选中
                            active.setChecked(true);
                        }
                        //所有权限标志
                        active.setBall(tbroleaction.get().getBall());
                        //获取角色按钮的范围
                        roleactionscopeset =
                                Optional.ofNullable(roleActionScopeGroupByActionId.get(tbroleaction.get().getId())).orElse(new ArrayList<>());
                    }
                    List<Tbscope> tbscopes = scopeGroupByMenuActionId.get(tbmenuaction.getId());
                    for (Tbscope tbscope : tbscopes) {
                        if ("TBDEPT".equals(tbscope.getStablename().toUpperCase().trim()) || "TBEMPLOY".equals(tbscope.getStablename().toUpperCase().trim())
                                || "TBCATAGORY".equals(tbscope.getStablename().toUpperCase().trim()) || "TBMEDIA".equals(tbscope.getStablename().toUpperCase().trim())) {
                            active.setTreeScope(true);
                            active.setScopeid(tbscope.getId());
                            active.setIdepth(tbscope.getIdepth());
                        }
                        scopelist = createActive(tbscope, roleactionscopeset, active.isTreeScope());
                        active.setActiveScopeClass(tbscope.getStablename() + tbscope.getIdepth());
                    }
                    if (!active.isBmodify()) {
                        active.setChecked(true);
                    }
                    active.setScopes(scopelist);
                    activelist.add(active);
                }

            }
            roleMenuModel.setActives(activelist);
            listRoleMenuList.add(roleMenuModel);
        }
        roleActionDTO.setRoleId(roleId);
        roleActionDTO.setRoleMenuModelList(listRoleMenuList);
        return roleActionDTO;
    }


    /**
     * 方法名称:createActive.<br/>
     * 方法功能:  构造勾选范围.<br/>
     *
     * @param tscope
     * @param roleactionscopeset
     * @return
     * @author wangwk
     * @since JDK 1.7
     */
    private List<ScopeModel> createActive(Tbscope tscope, List<Tbroleactionscope> roleactionscopeset,
                                          boolean treeScope) {
        List<ScopeModel> scopelist = new ArrayList<>();
        if (tscope.getBuse()) {// 如果范围为有效的
            // 查询相关范围
            String sql = "";
            if ("TBDEPT".equals(tscope.getStablename().toUpperCase().trim())) {
                sql = "select " + tscope.getScolumn() + " from " + tscope.getStablename() + " where idepth<= " + tscope.getIdepth() + " order by isort asc";
            } else {
                sql = "select " + tscope.getScolumn() + " from " + tscope.getStablename();
            }
            //id name pid
            List<Map> maps = tbroleMapper.selectScope(sql);

            for (Map objects : maps) {
                if (treeScope) {
                    for (Tbroleactionscope troleactionscope : roleactionscopeset) {
                        if (troleactionscope.getSbusinessid().equals(objects.get("id"))) {
                            ScopeModel scope = new ScopeModel(tscope.getId(), (Long) objects.get("id"), objects.get(
                                    "name").toString(), (Long) objects.get("pid"), true);
                            scope.setChecked(true);
                            scopelist.add(scope);
                            break;
                        }
                    }
                } else {
                    ScopeModel scope = new ScopeModel(tscope.getId(), (Long) objects.get("id"),
                            objects.get("name").toString(), null, false);
                    for (Tbroleactionscope troleactionscope : roleactionscopeset) {
                        if (troleactionscope.getSbusinessid().equals(scope.getBusinessid())) {
                            scope.setChecked(true);
                            break;
                        }
                    }
                    scopelist.add(scope);
                }


            }
        }
        return scopelist;
    }

    @Override
    @LogModelRemoveCache
    public void saveRoleActionAndRoleActionScope(RoleActionDTO roleActionDTO) throws Exception {
        //删除原有角色菜单关联的action  级联删除 actionscope
        Long roleId = roleActionDTO.getRoleId();
        String cacheDTOKey = roleActionDTO.getCacheDTOKey();
        List<RoleMenuModel> roleMenuModelList = roleActionDTO.getRoleMenuModelList();
        List<Tbrolemenu> tbrolemenuList = tbrolemenuMapper.getAuthMenuListById(roleId);
        List<Long> roleMenuIdList = tbrolemenuList.stream().map(Tbrolemenu::getId).collect(Collectors.toList());
        tbroleactionService.deleteRoleActionsByRoleMenuId(roleMenuIdList);

        for (RoleMenuModel roleMenuModel : roleMenuModelList) {
            List<ActiveModel> activeModelList = roleMenuModel.getActives();
            for (ActiveModel activeModel : activeModelList) {
                Tbrolemenu tbrolemenu = tbrolemenuList.stream()
                        .filter(rm -> rm.getMenuid().equals(roleMenuModel.getMenuid()))
                        .filter(rm -> rm.getRoleid().equals(roleId))
                        .findFirst().get();
                Tbroleaction tbroleaction = new Tbroleaction();
                tbroleaction.setId(IdUtil.getSnowflakeNextId());
                tbroleaction.setBall(activeModel.isBall());
                tbroleaction.setBuse(activeModel.isChecked());
                tbroleaction.setIsort(0);
                tbroleaction.setSkeycode(activeModel.getActivekeycode());
                tbroleaction.setSname(activeModel.getActivename());
                tbroleaction.setSfunctionurl(activeModel.getSfunctionurl());
                tbroleaction.setSgroupkey(activeModel.getSgroupkey());
                tbroleaction.setRolemenuid(tbrolemenu.getId());
                tbroleactionService.save(tbroleaction);

                List<ScopeModel> scopes = activeModel.getScopes();
                List<Tbroleactionscope> tbroleactionscopeList = new ArrayList<>();
                for (ScopeModel scope : scopes) {
                    if (scope.isChecked()) {
                        Tbroleactionscope ras = new Tbroleactionscope(IdUtil.getSnowflakeNextId(),
                                tbroleaction.getId(), scope.getScopeid(), scope.getBusinessid(), false);
                        tbroleactionscopeList.add(ras);
                    }
                }
                tbroleactionscopeService.saveBatch(tbroleactionscopeList);

            }
        }

        // 菜单按钮范围日志
        RoleActionDTO cacheRoleActionDTO = (RoleActionDTO) redisService.getCacheObject(cacheDTOKey);
        if (null == cacheRoleActionDTO) {
            throw new IllegalStateException("无法获取缓存数据，请重新获取数据操作");
        }
        List<RoleMenuModel> roleMenuModelListOld = cacheRoleActionDTO.getRoleMenuModelList();
        List<RoleMenuModel> roleMenuModelListNew = roleActionDTO.getRoleMenuModelList();
        List<Map<String, Object>> roleMenuMap = roleMenuModelListOld.stream().map(old -> {
            Map<String, Object> logMap = new LinkedHashMap<>();
            if (roleMenuModelListNew.stream().noneMatch(role -> role.equals(old))) {
                logMap.put("修改前：" + old.getMenuname(), getObjectMap(old));
                Optional<RoleMenuModel> roleMenuModelNew =
                        roleMenuModelListNew.stream().filter(r -> r.getMenuid().equals(old.getMenuid())).findFirst();
                if (roleMenuModelNew.isPresent()) {
                    logMap.put("修改后：" + roleMenuModelNew.get().getMenuname(), getObjectMap(roleMenuModelNew.get()));
                }
                return logMap;
            } else {
                return null;
            }

        }).filter(Objects::nonNull).collect(Collectors.toList());
        System.out.println(JSONObject.toJSONString(roleMenuMap));

        if (!roleMenuMap.isEmpty()) {
            Twsysoperatelog twsysoperatelog = new Twsysoperatelog();
            twsysoperatelog.setSlogtype(LogTypes.LOG_TYPES_UPDATE.getTypeStr());
            twsysoperatelog.setSlogname("角色菜单按钮修改");
            twsysoperatelog.setStablename("tbrole");
            twsysoperatelog.setRecordmasterid(roleId);
            twsysoperatelog.setSnewvalue(JSONObject.toJSONString(roleMenuMap));
            twsysoperatelogService.saveAsyncSysoperatelogWithoutUser(twsysoperatelog);
        }
    }

    /**
     * getObjectMap
     * 方法功能: 整理日志对象map
     *
     * @param roleMenuModel
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author peij
     * @date 2023/9/16 15:51
     */
    private static Map<String, Object> getObjectMap(RoleMenuModel roleMenuModel) {
        Map<String, Object> menuMap = new HashMap<>();
        List<ActiveTemp> activeTemps = roleMenuModel.getActives().stream().map(active -> {
            ActiveTemp activeTemp = new ActiveTemp();
            List<ScopeTemp> scopeTemps = active.getScopes().stream().map(scope -> {
                ScopeTemp scopeTemp = new ScopeTemp();
                scopeTemp.setBusinessname(scope.getBusinessname());
                scopeTemp.setChecked(scope.isChecked());
                return scopeTemp;
            }).collect(Collectors.toList());
            activeTemp.setActivename(active.getActivename());
            activeTemp.setChecked(active.isChecked());
            activeTemp.setScopes(scopeTemps);
            return activeTemp;
        }).collect(Collectors.toList());
        RoleMenuTemp roleMenuTemp = new RoleMenuTemp();
        roleMenuTemp.setMenuname(roleMenuModel.getMenuname());
        roleMenuTemp.setChecked(roleMenuModel.getChecked());
        roleMenuTemp.setActives(activeTemps);
        menuMap.put(roleMenuModel.getMenuname(), roleMenuTemp);
        return menuMap;
    }

    @Override
    public void saveCopyRole(Long roleId, String roleName) throws Exception {
        if (roleNameExist(roleName, null)) {
            throw new DataExistException("角色名称已存在");
        }
        RoleActionDTO roleActionDTO = new RoleActionDTO();
        Long newId = IdUtil.getSnowflakeNextId();

        //复制角色
        Tbrole tbrole = this.getById(roleId);
        tbrole.setId(newId);
        tbrole.setSname(roleName);
        tbrole.setIsort(this.getMaxSort());
        this.save(tbrole);

        // 复制角色菜单关联
        List<Tbrolemenu> tbrolemenuList = tbrolemenuService.lambdaQuery()
                .eq(Tbrolemenu::getRoleid, roleId)
                .list();
        for (Tbrolemenu tbrolemenu : tbrolemenuList) {
            tbrolemenu.setId(IdUtil.getSnowflakeNextId());
            tbrolemenu.setRoleid(newId);
        }
        tbrolemenuService.saveBatch(tbrolemenuList);

        //角色关联部门
        LoginUser loginUser = WebUtil.getLoginUser();
        if (DEPT_ADMIN == loginUser.getAdminflag()) {
            String managedeptIds = loginUser.getSmanagedepts();
            if (StrUtil.isNotBlank(managedeptIds)) {
                String[] deptIds = managedeptIds.split(",");
                Arrays.stream(deptIds).forEach(item -> {
                    Tbroledept tbroledept = new Tbroledept();
                    tbroledept.setRoleid(roleId);
                    tbroledept.setDeptid(Long.valueOf(item));
                    tbroledeptService.save(tbroledept);
                });
            }
        }

        TbroleServiceI service = SpringUtil.getBean(TbroleServiceI.class);
        // 复制按钮范围
        RoleActionDTO roleActionScope = service.getRoleActionScopeByRoleId(roleId);
        List<RoleMenuModel> roleActionScopeList = roleActionScope.getRoleMenuModelList();
        roleActionDTO.setRoleId(newId);
        roleActionDTO.setCacheDTOKey(roleActionScope.getCacheDTOKey());
        roleActionDTO.setRoleMenuModelList(roleActionScopeList);
        service.saveRoleActionAndRoleActionScope(roleActionDTO);

        // 操作日志
        RoleDTO roleDTO = getRoleById(roleId.toString());
        roleDTO.setId(newId);
        roleDTO.setSname(roleName);
        String compareStr = twsysoperatelogService.compareTowObject(null, roleDTO);
        Twsysoperatelog twsysoperatelog = new Twsysoperatelog();
        twsysoperatelog.setSlogtype(LogTypes.LOG_TYPES_INSERT.getTypeStr());
        twsysoperatelog.setSlogname("角色复制");
        twsysoperatelog.setStablename("tbrole");
        twsysoperatelog.setRecordmasterid(newId);
        twsysoperatelog.setSnewvalue(compareStr);
        twsysoperatelogService.saveAsyncSysoperatelogWithoutUser(twsysoperatelog);
    }


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

        Map<Long, List<Tbrole>> roleById =
                this.listByIds(roleIdList).stream().collect(Collectors.groupingBy(Tbrole::getId));

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

    @Override
    @LogModelAddCache
    public RoleDTO getRoleById(String roleId) {
        if (!StrUtil.isNotBlank(roleId)) {
            throw new IllegalArgumentException("角色id不能为空");
        }
        Tbrole tbrole = tbroleMapper.selectById(roleId);
        if (null == tbrole) {
            throw new IllegalStateException("无法获取角色信息");
        }
        RoleDTO roleDTO = new RoleDTO(tbrole);
        List<Tbrolemenu> list = new LambdaQueryChainWrapper<Tbrolemenu>(tbrolemenuMapper).eq(Tbrolemenu::getRoleid,
                roleId).list();
        if (list.size() > 0) {
            List<Tbmenu> menus = new LambdaQueryChainWrapper<Tbmenu>(tbmenuMapper)
                    .in(Tbmenu::getId, list.stream().map(Tbrolemenu::getMenuid)
                            .collect(Collectors.toList())).list();
            roleDTO.setMenuIds(menus.stream().map(Tbmenu::getId).map(Objects::toString).collect(Collectors.joining(","
            )));
        }
        return roleDTO;
    }

    /**
     * getRoleInfos
     * 方法功能:  用于工作流选择角色
     *
     * @param
     * @return com.hgzp.advertisingsys.pagemodel.flow.vo.FlowSelectItemData
     * @author songly
     * @date 2023/10/13 16:59
     */
    @Override
    public Json<Dict> getRoleForFlow() {
        try {
            // 不分页
            Page<Tbrole> page = new Page<>(1, -1);
            IPage<Tbrole> tbroleIPage = getTbroleIPage(page, new BaseQueryInfo());
            List<Tbrole> records = tbroleIPage.getRecords();

            Dict dict = Dict.create()
                    .set("childDepartments", new ArrayList<>())
                    .set("roleList", new ArrayList<>())
                    .set("employees", new ArrayList<>());

            if (records.size() > 0) {
                records=records.stream().sorted(Comparator.comparing(Tbrole::getSname)).collect(Collectors.toList());
                List<OrgTreeVo> lsItems = new ArrayList<>();
                for (Tbrole role : records) {
                    if (role.getBuse() == false) continue;
                    if ("超级管理员".equals(role.getSname())) continue;
                    OrgTreeVo item = new OrgTreeVo();
                    item.setId(role.getId().toString());
                    item.setName(role.getSname());
                    item.setSelected(false);
                    item.setType("role");
                    item.setStatus(1);
                    lsItems.add(item);
                }
                dict.set("roleList", lsItems);
                dict.set("childDepartments", lsItems);
            }
           return Json.success(dict);
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    @Data
    public static class RoleMenuTemp {
        private String menuname;
        private List<ActiveTemp> actives;
        private Boolean checked;
    }

    @Data
    public static class ActiveTemp {
        private String activename;
        private boolean checked;
        private List<ScopeTemp> scopes;
    }

    @Data
    public static class ScopeTemp {
        private String businessname;
        private boolean checked;
    }


}
