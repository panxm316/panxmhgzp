package com.hgzp.advertisingsys.controller.ad;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.UpFileModel;
import com.hgzp.advertisingsys.pagemodel.ad.vo.AdindustryVO;
import com.hgzp.advertisingsys.service.ad.TbadindustryServiceI;
import com.hgzp.advertisingsys.service.system.TbresourcetypeServiceI;
import com.hgzp.core.model.Tbadindustry;
import com.hgzp.core.model.Tbresourcetype;
import com.hgzp.core.page.*;
import com.hgzp.core.web.BaseController;
import com.hgzp.utils.exception.UfileException;
import com.hgzp.utils.file.UfileUtil;
import com.hgzp.utils.file.WebUploadUtil;
import com.hgzp.utils.model.FileInfo;
import com.hgzp.utils.model.UfileResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * AdIndustryController
 * 创建人：muyn
 * 类描述：广告行业信息设置
 * 创建日期：2023/8/17 11:54
 * @folder ad/AdIndustryController
 */
@RequestMapping(value = "/ad/adindustry")
@RestController
public class AdIndustryController extends BaseController {
    @Autowired
    private TbadindustryServiceI tbAdIndustryService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    private TbresourcetypeServiceI tbresourcetypeServiceI;

    /**
     * 通过ID查询单条行业数据
     * 方法功能: 通过ID查询单条行业数据
     * @author muyn
     * @date 2023/8/17 10:58
     * @param id 主键
     * @return com.hgzp.core.page.Json
     */
    @GetMapping(value = "/getTbAdIndustryById")
    public Json<AdindustryVO> getTbAdIndustryById(String id){
        AdindustryVO tbAdIndustry=tbAdIndustryService.getTbAdIndustryById(id);
        return Json.success(tbAdIndustry);
    }

    /**
     * 获取行业树
     * 方法功能:   获取行业树
     * @author muyn
     * @date 2023/8/17 10:58
     * @param queryInfo
     * @return com.hgzp.core.page.Json
     */
    @GetMapping(value = "/getTbAdIndustryTree")
    public Json<List<TreeModel>> getTbAdIndustryTree(TreeQueryVo queryInfo){
        List<TreeModel> tbAdIndustryListTree = tbAdIndustryService.getTbAdIndustryTree(queryInfo);
        // 将平缓的树列表转为带children的树结构
        // List<TreeModel> tbAdIndustryListTree = TreeModel.buildTreeModel(tbAdIndustryListTreeTmp);
        return Json.success(tbAdIndustryListTree);
    }

    /**
     * 分页查询广告行业
     * 方法功能: 分页查询广告行业
     * @author muyn
     * @date 2023/8/17 10:58
     * @param pageRequeste
     * @param queryInfo
     * @return com.hgzp.core.page.Json
     */
    @GetMapping(value = "/getTbAdIndustryPageList")
    public Json<List<AdindustryVO>> getTbAdIndustryPageList(PageRequest pageRequeste, TreeQueryVo queryInfo){
        Page page = getPage(pageRequeste);
        IPage<AdindustryVO> tbAdIndustryIPage = tbAdIndustryService.getAdIndustryPageList(page, queryInfo);
        return Json.success(tbAdIndustryIPage);
    }

    /**
     * 新增广告行业数据
     * 方法功能: 新增广告行业数据
     * @author muyn
     * @date 2023/8/17 10:58
     * @param tbAdIndustry 实例对象
     * @return 实例对象
     */
    @PostMapping(value = "/saveTbAdIndustry")
    public Json saveTbAdIndustry(@RequestBody Tbadindustry tbAdIndustry){
        tbAdIndustryService.saveAdIndustry(tbAdIndustry);
        return Json.success(tbAdIndustry);
    }

    /**
     * 更新广告行业数据
     * 方法功能:   更新广告行业数据
     * @author muyn
     * @date 2023/8/17 10:58
     * @param tbAdIndustry 实例对象
     * @return 实例对象
     */
    @PostMapping(value = "/updateTbAdIndustry")
    public Json updateTbAdIndustry(@RequestBody Tbadindustry tbAdIndustry){
        tbAdIndustryService.updateAdIndustry(tbAdIndustry);
        return  Json.success();
    }

    /**
     * 通过主键删除广告行业数据
     * 方法功能:   通过主键删除广告行业数据
     * @author muyn
     * @date 2023/8/17 10:58
     * @param tbAdIndustryIds 主键
     * @return 是否成功
     */
    @PostMapping(value = "/deleteTbAdIndustry")
    public Json deleteTbAdIndustry(String tbAdIndustryIds){
        innerInterceptor.recoredLog();
        tbAdIndustryService.removeByIds(CollUtil.newArrayList(tbAdIndustryIds.split(",")));
        return Json.success();
    }

    /**
     * 获取最大序号
     * 方法功能:  获取最大序号
     * @author muyn
     * @date 2023/8/17 10:58
     * @return Json
     */
    @GetMapping("/getMaxSort")
    public Json getMaxSort() {
        Integer maxSort = tbAdIndustryService.getMaxSort();
        return Json.success(maxSort);
    }

    /**
     * 重置来源扩展
     * 方法功能:  重置来源扩展
     * @author muyn
     * @date 2023/2/2 10:58
     * @return Json
     */
    @GetMapping("/resetAdIndustryExtend")
    public Json resetAdFromExtend() {
        tbAdIndustryService.resetAdIndustryExtend();
        return Json.success();
    }
    /**
     * 上传行业文件
     * 方法功能:
     * @author songly
     * @date 2024/3/16 13:23
     * @param request
     * @return com.hgzp.core.page.Json<com.hgzp.advertisingsys.pagemodel.UpFileModel>
     */
    @PostMapping("/upLoadIndustryFile")
    public Json<UpFileModel> upLoadAdIndustryFile(HttpServletRequest request) throws Exception {
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
}
