package com.hgzp.advertisingsys.controller.system;


import com.hgzp.core.page.TreeQuery;
import com.hgzp.advertisingsys.service.system.TbdeptServiceI;
import static com.hgzp.core.emnus.ResultDefines.*;
import com.hgzp.core.model.Tbdept;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.TreeModel;
import com.hgzp.utils.file.WebUploadUtil;
import com.hgzp.utils.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * DeptController
 * 创建人：wangwk
 * 类描述：部门设置controller
 * 创建日期：2023/8/18 13:51
 * @folder system/DeptController
 */
@Validated
@RestController
@RequestMapping("/system/dept")
public class DeptController {

    @Autowired
    TbdeptServiceI tbdeptService;

    /**
     * 部门管理查询所有部门树
     * 方法功能: 部门管理查询所有部门树
     * @author wangwk
     * @date 2023/8/21 13:46
     * @param query  查询条件
     * @return com.hgzp.core.page.Json
     */
    @GetMapping("/getSysDeptTree")
    public Json<List<TreeModel>> getSysDeptTree(TreeQuery query) throws Exception {
        List<TreeModel> sysDeptTree = tbdeptService.getSysDeptTree(query, true);
        return Json.success(sysDeptTree);
    }

    /**
     * 人员管理显示的部门树
     * 方法功能: 人员管理显示的部门树
     * @author wangwk
     * @date 2023/8/21 13:46
     * @param query  查询条件
     * @return com.hgzp.core.page.Json
     */
    @GetMapping("/getSysEmpDeptTree")
    public Json<List<TreeModel>> getSysEmpDeptTree(TreeQuery query) throws Exception {
        List<TreeModel> sysDeptTree = tbdeptService.getSysDeptTree(query,false);
        return Json.success(sysDeptTree);
    }



    /**
     * 保存部门
     * 方法功能:  保存部门
     * @author wangwk
     * @date 2023/8/19 15:32
     * @param tbdept
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/saveDept")
    public Json<Tbdept> saveDept(@RequestBody Tbdept tbdept){
        tbdeptService.saveDept(tbdept);
        return Json.success(tbdept);
    }

    /**
     * 更新部门
     * 方法功能: 更新部门
     * @author wangwk
     * @date 2023/8/21 13:27
     * @param tbdept
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/updateDept")
    public Json updateDept(@RequestBody Tbdept tbdept){
        tbdeptService.updateDept(tbdept);
        return Json.success();
    }

    /**
     * 删除部门
     * 方法功能:  删除部门
     * @author wangwk
     * @date 2023/8/21 14:03
     * @param ids 多个用逗号隔开
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/deleteDept")
    public Json deleteDept(@NotBlank(message = "部门id不能为空") String ids){
        tbdeptService.deleteDept(ids);
        return Json.success();
    }

    /**
     * 获取部门信息
     * 方法功能:  获取部门信息
     * @author wangwk
     * @date 2023/8/23 15:39
     * @param id 部门id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Tbdept>
     */
    @GetMapping("/getDeptInfoById")
    public Json<Tbdept> getDeptInfoById(@NotNull(message = "部门id不能为空") Long id){
        Tbdept tbdept = tbdeptService.getById(id);
        return tbdept == null? Json.fail(DATA_NOTEXIT) : Json.success(tbdept);
    }

    /**
     * 获取下一个序号
     * 方法功能: 获取下一个序号
     * @author wangwk
     * @date 2023/8/23 14:18
     * @return com.hgzp.core.page.Json<java.lang.Integer>
     */
    @GetMapping("/getMaxIsort")
    public Json<Integer> getMaxIsort(){
        Integer maxSort = tbdeptService.getMaxSort();
        return Json.success(maxSort);
    }

    /**
     * 获取父部门的buse
     * 方法功能: 获取父部门的buse
     * @author wangwk
     * @date 2023/8/28 9:34
     * @param id
     * @return com.hgzp.core.page.Json<java.lang.Boolean>
     */
    @GetMapping("/getParentDeptBuse")
    public Json<Boolean> getParentDeptBuse(@NotNull(message = "id不能为空") Long id){
        Boolean parentDeptBuse = tbdeptService.getParentDeptBuse(id);
        return Json.success(parentDeptBuse);
    }

    /**
     * 批量调整部门
     * 方法功能:  批量调整部门
     * @author wangwk
     * @date 2023/8/30 10:31
     * @param ids  要调整的部门id，多个用逗号隔开
     * @param pid  调整到的部门id
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/batchUdpateDept")
    public Json batchUdpateDept(String ids, Long pid){
        tbdeptService.batchUdpateDept(ids, pid);
        return Json.success();
    }


    /**
     * 导入人员部门
     * 方法功能:  excel 导入人员部门
     * @author wangwk
     * @date 2023/9/4 12:55
     * @param request
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/importDeptAndEmpData")
    public Json importDeptAndEmpData(HttpServletRequest request) throws Exception {
        FileInfo upload = WebUploadUtil.upload(request);
        tbdeptService.importDeptAndEmpData(upload.getFile());
        upload.getFile().delete();
        return Json.success();
    }




}
