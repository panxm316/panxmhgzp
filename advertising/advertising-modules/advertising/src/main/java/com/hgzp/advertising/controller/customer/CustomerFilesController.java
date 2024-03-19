package com.hgzp.advertising.controller.customer;

import cn.hutool.core.util.ObjectUtil;
import com.hgzp.advertising.pagemodel.common.UpFileModel;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerfilesVo;
import com.hgzp.advertising.service.customer.TwcustomerfilesServiceI;
import com.hgzp.advertising.service.system.TbresourcetypeServiceI;
import com.hgzp.core.model.Tbresourcetype;
import com.hgzp.core.model.Twcustomerfiles;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import com.hgzp.utils.exception.UfileException;
import com.hgzp.utils.file.UfileUtil;
import com.hgzp.utils.file.WebUploadUtil;
import com.hgzp.utils.model.FileInfo;
import com.hgzp.utils.model.UfileResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.List;

/**
 * CustomerFilesController
 * 创建人：songly
 * 类描述：客户资料
 * 创建日期：2023/10/25 13:22
 *
 * @folder customer/CustomerFilesController
 */
@Validated
@RestController
@RequestMapping("/customer/customerfiles")
public class CustomerFilesController extends BaseController {
    @Autowired
    private TwcustomerfilesServiceI filesService;
    @Autowired
    private TbresourcetypeServiceI tbresourcetypeServiceI;
    /**
     * 上传客户资料附件
     * 方法功能:上传客户资料附件
     * @author songly
     * @date 2023/10/27 16:28
     * @param request
     * @return com.hgzp.core.page.Json
     */
    @PostMapping("/upLoadCustomerFile")
    public Json<UpFileModel> upLoadCustomerFile(HttpServletRequest request) throws Exception {
        FileInfo upload = WebUploadUtil.upload(request);
        File file = upload.getFile();
        String sformat = FilenameUtils.getExtension(upload.getFileName());
        Tbresourcetype tbresourcetype = tbresourcetypeServiceI.getResourcetypeByFormat(sformat);
        if (tbresourcetype == null) {
            file.delete();
            throw new UfileException("文件【" + file.getName() + "】不支持此类型 ");
        }
        UfileResponse response = UfileUtil.postFile(file);
        if (!response.isSuccess()) {
            file.delete();
            throw new UfileException("文件【" + file.getName() + "】统一文件推送失败 ");
        }
        String sFilesha1 = response.getSha1();
        UpFileModel fileModel = new UpFileModel();
        fileModel.setSfileformat("." + sformat);
        fileModel.setSfilesize(upload.getFileSize());
        fileModel.setSfileid(sFilesha1);
        if (tbresourcetype != null) {
            fileModel.setSfiletype(tbresourcetype.getSname());
        }
        fileModel.setSoriginalfile(upload.getFileName());
        upload.getFile().delete();
        return Json.success(fileModel);
    }
    /**
     * 获取客户的附件资料
     * 方法功能: 获取客户的附件资料
     *
     * @param customerid
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twcustomerfiles>>
     * @author songly
     * @date 2023/10/25 14:04
     */
    @GetMapping(value = "/getCustomerFilesList")
    public Json<List<CustomerfilesVo>> getCustomerFilesList(@NotBlank(message = "客户Id不能为空！") String customerid) {
        List<CustomerfilesVo> lsFiles = filesService.getCustomerFiles(customerid);
        return Json.success(lsFiles);
    }
    /**
     * 根据Id获取文件信息
     * 方法功能: 根据Id获取文件信息
     * @author hgsongly
     * @date 2023/10/26 9:21
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Twcustomerfiles>
     */
    @GetMapping(value = "/getCustomerFilesById")
    public Json<Twcustomerfiles> getCustomerById(@NotNull(message = "ID不可为空") String id) {
        Twcustomerfiles twcustomer = filesService.getById(id);
        return ObjectUtil.isNotNull(twcustomer) ? Json.success(twcustomer) : Json.fail();
    }
    /**
     * 更新 客户资料附件
     * 方法功能: 更新客户资料附件
     *
     * @param lsCustomerfiles
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/10/25 14:04
     */
    @PostMapping(value = "/updateCustomerFiles")
    public Json updateCustomerFiles(@RequestBody List<Twcustomerfiles> lsCustomerfiles) {
        try {
             filesService.updateCustomerFiles(lsCustomerfiles);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail("保存失败:"+e.getMessage());
        }
    }

    /**
     * 添加 客户资料附件
     * 方法功能: 添加客户资料附件
     *
     * @param lsCustomerfiles
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/10/12 10:36
     */
    @PostMapping(value = "/saveCustomerFiles")
    public Json saveCustomerFiles(@RequestBody List<Twcustomerfiles> lsCustomerfiles)  {
        try {
            filesService.saveCustomerFiles(lsCustomerfiles);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
           return Json.fail("保存失败:"+e.getMessage());
        }
    }

    /**
     * 根据id删除
     * 方法功能: 根据id删除
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/10/12 10:34
     */
    @PostMapping(value = "/deleteCustomerFilesById")
    public Json deleteCustomerFilesById(@NotNull(message = "ID不可为空") String ids) {
        try {
            filesService.deleteFilesByIds(ids, false);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail("传入ID异常，删除失败");
        }
    }

    /**
     * 根据客户Id删除附件
     * 方法功能: 根据客户Id删除附件
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/10/25 14:08
     */
    @PostMapping(value = "/deleteCustomerFilesByCustomerId")
    public Json deleteCustomerFilesBCustomerId(@NotNull(message = "ID不可为空") String ids) {

        // innerInterceptor.recoredLog();
        try {
            filesService.deleteFilesByIds(ids,true);
            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail("删除失败");
        }
    }
//    /**
//     * 恢复已删文件
//     * 方法功能: 恢复已删文件
//     * @author hgsongly
//     * @date 2023/10/25 14:35
//     * @param ids
//     * @return com.hgzp.core.page.Json
//     */
//    @PostMapping(value = "/recoveryCustomerFilesById")
//    public Json recoveryCustomerFilesById(@NotNull(message = "ID不可为空") String ids) {
//
//        // innerInterceptor.recoredLog();
//        try {
//            filesService.recoveryFilesByIds(ids, false);
//            return Json.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Json.fail("传入ID异常，删除失败");
//        }
//    }

//    /**
//     * 恢复已删客户附件
//     * 方法功能:恢复已删客户附件
//     * @author hgsongly
//     * @date 2023/10/25 14:35
//     * @param ids
//     * @return com.hgzp.core.page.Json
//     */
//    @PostMapping(value = "/recoveryCustomerFilesByCustomerId")
//    public Json recoveryCustomerFilesByCustomerId(@NotNull(message = "ID不可为空") String ids) {
//
//        // innerInterceptor.recoredLog();
//        try {
//            filesService.recoveryFilesByIds(ids,true);
//            return Json.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Json.fail("恢复失败");
//        }
//    }

}