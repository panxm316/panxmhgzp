package com.hgzp.advertising.controller.common;

import com.hgzp.advertising.pagemodel.common.UpFileModel;
import com.hgzp.advertising.service.system.TbresourcetypeServiceI;
import com.hgzp.core.model.Tbresourcetype;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import com.hgzp.utils.exception.UfileException;
import com.hgzp.utils.file.UfileUtil;
import com.hgzp.utils.file.WebUploadUtil;
import com.hgzp.utils.model.FileInfo;
import com.hgzp.utils.model.UfileResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * UploadFileController
 * 创建人：suny
 * 类描述：文件上传
 * 创建日期：2023/10/30 15:53
 *
 * @测试：
 * @folder common/UploadFileController
 */
@RestController
@RequestMapping("/common/UploadFile")
public class UploadFileController extends BaseController {
    @Value("${ufile.uExtURL}")
    private String uExtURL;
    @Value("${ufile.uWebURL}")
    private String uWebURL;
    @Autowired
    TbresourcetypeServiceI tbresourcetypeServiceI;

    /**
     * 文件上传到统一文件
     * 方法功能: 文件上传统一文件并返回UpFileModel公共参数
     *
     * @param request
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.common.UpFileModel>
     * @author suny
     * @date 2023/10/30 15:54
     */
    @PostMapping("/importFile")
    public Json<UpFileModel> importFile(HttpServletRequest request) throws Exception {
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
        String url = uWebURL + sFilesha1;
        if (!"Photo".equals(tbresourcetype.getSname())) {
            url = "";
        }
        fileModel.setUrl(url);
        String durl = UfileUtil.getStaticUrl(sFilesha1, "." + sformat);
        fileModel.setDurl(durl);
        upload.getFile().delete();
        return Json.success(fileModel);
    }
}
