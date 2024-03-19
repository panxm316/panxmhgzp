package com.hgzp.advertising.controller.system;

import cn.hutool.core.util.ObjUtil;
import com.hgzp.advertising.pagemodel.system.dto.EmpAuthorityDTO;
import com.hgzp.advertising.pagemodel.system.dto.EmployDTO;
import com.hgzp.advertising.pagemodel.system.vo.EmployVO;
import com.hgzp.advertising.service.system.TbemployServiceI;
import com.hgzp.core.constant.SecurityConstants;
import com.hgzp.core.model.Tbemploy;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQuery;
import com.hgzp.core.web.BaseController;
import com.hgzp.utils.WebUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.hgzp.core.constant.ValidateParam.detail;

/**
 * EmpController
 * 创建人：wangwk
 * 类描述：人员设置controller
 * 创建日期：2023/8/21 15:01
 *
 * @folder system/EmpController
 */
@Validated
@RestController
@RequestMapping("/system/emp")
public class EmpController extends BaseController {


    @Autowired
    TbemployServiceI tbemployService;

    /**
     * 个人设置更新人员信息
     * 方法功能:  个人设置更新人员信息
     *
     * @param employModel
     * @return com.hgzp.core.page.Json
     * @author wangwk
     * @date 2023/9/11 11:10
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
     *
     * @param oldPassword 旧密码sm2加密
     * @param newPassword 新密码sm2加密
     * @return com.hgzp.core.page.Json
     * @author wangwk
     * @date 2023/9/11 11:24
     */
    @PostMapping("/updatePassWord")
    public Json updatePassWord(String oldPassword, String newPassword) throws Exception {
        tbemployService.updatePassWord(oldPassword, newPassword);
        return Json.success();
    }

    /**
     * 个人设置更新头像
     * 方法功能:  个人设置更新头像
     *
     * @param imgBase64
     * @return com.hgzp.core.page.Json
     * @author wangwk
     * @date 2023/9/11 13:08
     */
    @PostMapping("/updateEmployImg")
    public Json updateEmployImg(String imgBase64) throws Exception {
        tbemployService.updateEmployImg(imgBase64);
        return Json.success();
    }

    /**
     * 获取当前人员信息
     * 方法功能: 获取当前人员信息
     *
     * @param
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.system.vo.EmployVO>
     * @author suny
     * @date 2023/9/12 15:37
     */
    @GetMapping("/getEmployInfo")
    public Json<EmployVO> getEmployInfo() throws Exception {
        EmployVO employ = tbemployService.getEmployInfo();
        return Json.success(employ);
    }

    /**
     * 按关键词查询所有员工树
     * 方法功能:按关键词查询所有员工树，用布尔型bIgnorePermissions来控制是否忽略权限（显示全部），true：忽略权限，false：需要权限
     *
     * @param queryKey
     * @param bIgnorePermissions 是否忽略权限，true：忽略权限，false：需要权限
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author yanz
     * @date 2023/10/25 9:13
     */
    @GetMapping("/getEmpTree")
    public Json<List<TreeModel>> getEmpTree(TreeQuery queryKey, Boolean bIgnorePermissions) throws Exception {
        List<Long> accessNodes = new ArrayList<>();
        if (ObjUtil.isEmpty(bIgnorePermissions)) {
            bIgnorePermissions = false;
        }
        // 需要权限
        if (!bIgnorePermissions) {
            HttpServletRequest httpServletRequest = WebUtil.getHttpServletRequest();
            EmpAuthorityDTO empAuthorityDTO = (EmpAuthorityDTO)httpServletRequest.getAttribute(SecurityConstants.ROLE_PERMISSION);
            accessNodes = empAuthorityDTO.getDeptIdList();
            bIgnorePermissions = accessNodes.isEmpty();
        }

        List<TreeModel> sysEmpTree = tbemployService.getEmpTree(queryKey, accessNodes, bIgnorePermissions);
        return Json.success(sysEmpTree);
    }

    /**
     * 按关键词查询 所有业务员树
     * 方法功能:按关键词查询所有业务员树，用布尔型bIgnorePermissions来控制是否忽略权限（显示全部），true：忽略权限，false：需要权限
     *
     * @param queryKey
     * @param bIgnorePermissions 是否忽略权限，true：忽略权限，false：需要权限
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.TreeModel>>
     * @author yanz
     * @date 2023/10/28 10:36
     */
    @GetMapping("/getSalespersonTree")
    public Json<List<TreeModel>> getSalespersonTree(TreeQuery queryKey, Boolean bIgnorePermissions) throws Exception {
        List<Long> accessNodes = new ArrayList<>();
        if (ObjUtil.isEmpty(bIgnorePermissions)) {
            bIgnorePermissions = false;
        }

        // 需要权限
        if (!bIgnorePermissions) {
            HttpServletRequest httpServletRequest = WebUtil.getHttpServletRequest();
            EmpAuthorityDTO empAuthorityDTO = (EmpAuthorityDTO)httpServletRequest.getAttribute(SecurityConstants.ROLE_PERMISSION);
            accessNodes = empAuthorityDTO.getDeptIdList();
            bIgnorePermissions = accessNodes.isEmpty();
        }

        List<TreeModel> salespersonTree = tbemployService.getEmpTree(queryKey, accessNodes, bIgnorePermissions);
        salespersonTree = tbemployService.filterNonSalesperson(salespersonTree);
        return Json.success(salespersonTree);
    }
}
