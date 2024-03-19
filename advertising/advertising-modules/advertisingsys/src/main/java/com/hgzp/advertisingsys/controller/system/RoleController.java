package com.hgzp.advertisingsys.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.system.dto.RoleActionDTO;
import com.hgzp.advertisingsys.pagemodel.system.dto.RoleDTO;
import com.hgzp.advertisingsys.pagemodel.system.vo.RoleVO;
import com.hgzp.advertisingsys.service.system.TbemployServiceI;
import com.hgzp.advertisingsys.service.system.TbroleServiceI;
import static com.hgzp.core.constant.ValidateParam.*;

import com.hgzp.core.model.Tbrole;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * RoleController
 * 创建人：wangwk
 * 类描述：角色controller
 * 创建日期：2023/8/23 16:06
 *
 * @folder system/RoleController
 */
@Validated
@RestController
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    @Autowired
    TbroleServiceI tbroleService;
    @Autowired
    TbemployServiceI tbemployService;

    /**
     * 角色分页列表
     * 方法功能: 查询 角色分页列表       
     * @author wangwk
     * @date 2023/8/24 16:47 
     * @param pageRequest
     * @param queryInfo
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.Tbrole>>        
     */
    @GetMapping("/getRolePageList")
    public Json<List<RoleVO>> getRolePageList(PageRequest pageRequest, BaseQueryInfo queryInfo) throws Exception {
        Page page = getPage(pageRequest);
        IPage<RoleVO> rolePageList = tbroleService.getRolePageList(page, queryInfo);
        return Json.success(rolePageList);
    }

    /**
     * 保存角色
     * 方法功能: 保存角色
     * @author wangwk
     * @date 2023/8/24 16:49
     * @param roleModel
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/saveRole")
    public Json<Tbrole> saveRole(@Validated(value = {add.class}) @RequestBody RoleDTO roleModel) throws Exception {
        Tbrole tbrole = tbroleService.saveRole(roleModel);
        return Json.success(tbrole);
    }

    /**
     * 更新角色
     * 方法功能: 更新角色
     * @author wangwk
     * @date 2023/8/24 16:59
     * @param roleModel
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/updateRole")
    public Json updateRole(@Validated(value = {edit.class})@RequestBody RoleDTO roleModel) throws Exception {
        tbroleService.updateRole(roleModel);
        return Json.success();
    }

    /**
     * 删除角色
     * 方法功能: 删除角色
     * @author wangwk
     * @date 2023/9/2 9:04
     * @param ids id字符串，多个用逗号分隔
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/deleteRoleById")
    public Json deleteRoleById(@NotBlank(message = "id不能为空") String ids) throws Exception {
        for (String id : ids.split(",")) {
            long count = tbemployService.countEmpByRoleId(id);
            if(count != 0){
                return Json.fail("该角色与人员已绑定");
            }
            tbroleService.deleteRoleById(id);
        }
        return Json.success("删除成功");
    }

    /**
     * 获取最大序号
     * 方法功能: 获取最大序号
     * @author wangwk
     * @date 2023/8/25 8:54
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     */
    @GetMapping("/getMaxIsort")
    public Json<Integer> getMaxIsort(){
        return Json.success(tbroleService.getMaxSort());
    }


    /**
     * 初始化角色操作
     * 方法功能: 初始化角色操作，获取菜单的按钮以及范围
     * @author wangwk
     * @date 2023/8/28 16:29
     * @param roleId
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.advertising.pagemodel.system.RoleMenuModel>>
     */
    @GetMapping("/getRoleActionScopeByRoleId")
    public Json<RoleActionDTO> getRoleActionScopeByRoleId(Long roleId){
        RoleActionDTO roleActionScopeByRoleId = tbroleService.getRoleActionScopeByRoleId(roleId);
        return Json.success(roleActionScopeByRoleId);
    }


    /**
     * 保存角色 菜单行为、范围
     * 方法功能: 保存角色 菜单行为、范围
     * @author wangwk
     * @date 2023/9/1 16:59
     * @param roleActionDTO
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/saveRoleAction")
    public Json saveRoleAction(@RequestBody RoleActionDTO roleActionDTO) throws Exception {
        tbroleService.saveRoleActionAndRoleActionScope(roleActionDTO);
        return Json.success();
    }

    /**
     * 复制角色
     * 方法功能: 复制角色
     * @author wangwk
     * @date 2023/9/2 8:58
     * @param roleId  角色id
     * @param roleName  角色名称
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/saveCopyRole")
    public Json saveCopyRole(Long roleId, String roleName) throws Exception {
        tbroleService.saveCopyRole(roleId, roleName);
        return Json.success();
    }

    /**
     * 角色下拉列表
     * 方法功能: 角色下拉列表
     * @author wangwk
     * @date 2023/9/2 9:35
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.page.DataCombo>>
     */
    @GetMapping("/getRoleCombo")
    public Json<List<DataCombo>> getRoleCombo() throws Exception {
        List<DataCombo> roleDataCombo = tbroleService.getRoleDataCombo();
        return Json.success(roleDataCombo);
    }
    /**
     * 根据roleid 获取角色
     * 方法功能: 根据roleid 获取角色
     * @author peij
     * @date 2023/9/12 15:12
     * @param roleId
     * @return com.hgzp.advertising.pagemodel.system.dto.RoleDTO
     */
    @GetMapping("/getRoleById")
    public Json<RoleDTO> getRoleById(String roleId) throws Exception{
        RoleDTO roleDTO = tbroleService.getRoleById(roleId);
        return Json.success(roleDTO);
    }

}
