package com.hgzp.advertisingsys.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.system.dto.EmployDTO;
import com.hgzp.advertisingsys.pagemodel.system.vo.EmployVO;
import com.hgzp.advertisingsys.service.system.TbemployServiceI;
import com.hgzp.core.model.Tbemploy;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.hgzp.core.constant.ValidateParam.*;

/**
 * EmpController
 * 创建人：wangwk
 * 类描述：人员设置controller
 * 创建日期：2023/8/21 15:01
 * @folder system/EmpController
 */
@Validated
@RestController
@RequestMapping("/system/emp")
public class EmpController extends BaseController {


    @Autowired
    TbemployServiceI tbemployService;

    /**
     * 人员列表
     * 方法功能:  人员管理，人员列表
     * @author wangwk
     * @date 2023/8/21 15:53
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.core.model.Tbemploy>>
     */
    @GetMapping("/getEmployPageList")
    public Json<List<EmployVO>> getEmployPageList(PageRequest pageRequest, EmployVO query) throws Exception {
        Page<Tbemploy> page = getPage(pageRequest);
        IPage<EmployVO> employPageList = tbemployService.getEmployPageList(page, query);
        return Json.success(employPageList);
    }

    /**
     * 保存人员
     * 方法功能: 保存人员
     * @author wangwk
     * @date 2023/8/23 12:55
     * @param employModel
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/saveEmploy")
    public Json saveEmploy(@Validated(value = {add.class}) @RequestBody EmployDTO employModel) throws Exception {
        tbemployService.saveEmploy(employModel);
        return Json.success();
    }


    /**
     * 更新人员
     * 方法功能: 更新人员
     * @author wangwk
     * @date 2023/8/23 13:51
     * @param employModel
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/updateEmploy")
    public Json updateEmploy(@Validated(value = {edit.class}) @RequestBody EmployDTO employModel) throws Exception {
        tbemployService.updateEmploy(employModel);
        return Json.success();
    }

    /**
     * 删除人员
     * 方法功能:  删除人员
     * @author wangwk
     * @date 2023/8/23 14:04
     * @param id
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/deleteEmp")
    public Json deleteEmp(@NotNull Long id) throws Exception {
        tbemployService.deleteEmp(id);
        return Json.success();
    }


    /**
     * 批量启用/禁用人员
     * 方法功能: 批量启用/禁用 人员
     * @author wangwk
     * @date 2023/8/23 14:11
     * @param ids 人员id，多个用英文逗号分隔
     * @param buse 是否启用
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/disableEmp")
    public Json disableEmp(@NotBlank String ids, @NotNull Boolean buse){
        tbemployService.disableEmps(ids, buse);
        return Json.success();
    }

    /**
     * 获取下一个人员序号
     * 方法功能: 获取下一个人员序号
     * @author wangwk
     * @date 2023/8/23 14:18
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     */
    @GetMapping("/getMaxIsort")
    public Json<Integer> getMaxIsort(){
        Integer maxSort = tbemployService.getMaxSort();
        return Json.success(maxSort);
    }

    /**
     * 批量设置人员角色
     * 方法功能:  批量设置人员角色
     * @author wangwk
     * @date 2023/9/2 10:12
     * @param empIds  人员id，多个用逗号分隔
     * @param roleIds  角色id，多个用逗号分隔
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/updateBatchEmpRoles")
    public Json updateBatchEmpRoles(@NotBlank(message = "人员id不能为空") String empIds,
                                    @NotBlank(message = "角色id不能为空") String roleIds) throws Exception {
        tbemployService.updateBatchEmpRoles(empIds, roleIds);
        return Json.success();
    }


    /**
     * 个人设置更新人员信息
     * 方法功能:  个人设置更新人员信息
     * @author wangwk
     * @date 2023/9/11 11:10
     * @param employModel
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/updatePersonSetting")
    public Json updatePersonSetting(@Validated(value = {detail.class}) @RequestBody EmployDTO employModel) throws Exception {
        Tbemploy tbemploy = new Tbemploy();
        BeanUtils.copyProperties(employModel, tbemploy);
        tbemployService.updatePersonSetting(tbemploy);
        return Json.success();
    }

    /**
     * 个人设置更新密码
     * 方法功能: 个人设置更新密码
     * @author wangwk
     * @date 2023/9/11 11:24
     * @param oldPassword  旧密码sm2加密
     * @param newPassword  新密码sm2加密
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/updatePassWord")
    public Json updatePassWord(String oldPassword, String newPassword) throws Exception {
        tbemployService.updatePassWord(oldPassword, newPassword);
        return Json.success();
    }

    /**
     * 个人设置更新头像
     * 方法功能:  个人设置更新头像
     * @author wangwk
     * @date 2023/9/11 13:08
     * @param imgBase64
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/updateEmployImg")
    public Json updateEmployImg(String imgBase64) throws Exception {
        tbemployService.updateEmployImg(imgBase64);
        return Json.success();
    }

    /**
     * 获取当前人员信息
     * 方法功能: 获取当前人员信息
     * @author suny
     * @date 2023/9/12 15:37
     * @param
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.system.vo.EmployVO>
     */
    @GetMapping("/getEmployInfo")
    public Json<EmployVO> getEmployInfo() throws Exception{
        EmployVO employ = tbemployService.getEmployInfo();
        return Json.success(employ);
    }

    /**
     * 根据id查询人员详情
     * 方法功能: 根据id查询人员详情
     * @author wangwk
     * @date 2023/9/15 9:46
     * @param id 人员id
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.system.vo.EmployVO>
     */
    @GetMapping("/getEmployInfoById")
    public Json<EmployVO> getEmployInfoById(Long id){
        EmployVO employ = tbemployService.getEmployInfoById(id);
        return Json.success(employ);
    }

    /**
     * 根据角色获取角色上的人员
     * 方法功能: 根据角色获取角色上的人员
     * @author CGD
     * @date 2023/9/15 12:52
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.advertising.pagemodel.system.vo.EmployVO>>
     */
    @GetMapping("/getEmployPageListByRoleId")
    public Json<List<EmployVO>> getEmployPageListByRoleId(PageRequest pageRequest, EmployVO query) throws Exception {
        Page<Tbemploy> page = getPage(pageRequest);
        IPage<EmployVO> employPageList = tbemployService.getEmployPageListByRoleId(page, query);
        return Json.success(employPageList);
    }

    /**
     * 根据角色获取不在角色上的人员
     * 方法功能:  根据角色获取不在角色上的人员
     * @author CGD
     * @date 2023/9/16 13:14
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List<com.hgzp.advertising.pagemodel.system.vo.EmployVO>>
     */
    @GetMapping("/getEmployPageListNotByRoleId")
    public Json<List<EmployVO>> getEmployPageListNotByRoleId(PageRequest pageRequest, EmployVO query) throws Exception {
        Page<Tbemploy> page = getPage(pageRequest);
        IPage<EmployVO> employPageList = tbemployService.getEmployPageListNotByRoleId(page, query);
        return Json.success(employPageList);
    }

    /**
     * 根据角色批量添加人员关联
     * 方法功能:  根据角色批量添加人员关联
     * @author CGD
     * @date 2023/9/16 15:07
     * @param empIds
     * @param roleId
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/saveEmployByRole")
    public Json saveEmployByRole(@NotBlank(message = "人员id不能为空") String empIds,
                                 @NotNull(message = "角色id不能为空") Long roleId) throws Exception {
        tbemployService.saveEmployByRole(roleId, empIds);
        return Json.success();
    }
    /**
     * 根据角色id重置角色上的人员关联
     * 方法功能: 根据角色id重置角色上的人员关联
     * @author CGD
     * @date 2023/9/18 11:27
     * @param roleId
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/deleteRoleById")
    public Json deleteRoleById(@NotNull(message = "角色id不能为空") Long roleId) throws Exception {
        tbemployService.deleteRoleById(roleId);
        return Json.success();
    }
    /**
     * 删除角色上的人员关联
     * 方法功能: 删除角色上的人员关联
     * @author CGD
     * @date 2023/9/16 15:50
     * @param empIds
     * @param roleId
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/deleteRoleByEmploy")
    public Json deleteRoleByEmploy(@NotBlank(message = "人员id不能为空") String empIds,
                                 @NotNull(message = "角色id不能为空") Long roleId) throws Exception {
        tbemployService.deleteRoleByEmploy(roleId, empIds);
        return Json.success();
    }

}
