package com.hgzp.advertising.controller.ad;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.ad.dto.TwadprojectDTO;
import com.hgzp.advertising.pagemodel.ad.vo.AdprojectVO;
import com.hgzp.advertising.pagemodel.common.UpFileModel;
import com.hgzp.advertising.service.flow.TbflowServiceI;
import com.hgzp.advertising.service.system.TbresourcetypeServiceI;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.model.Tbflow;
import com.hgzp.core.model.Tbresourcetype;
import com.hgzp.core.model.Twadproject;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import com.hgzp.advertising.service.ad.TwadprojectServiceI;
import com.hgzp.utils.exception.UfileException;
import com.hgzp.utils.file.UfileUtil;
import com.hgzp.utils.file.WebUploadUtil;
import com.hgzp.utils.model.FileInfo;
import com.hgzp.utils.model.UfileResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.List;

/**
 * AdProjectController
 * 创建人：CGD
 * 类描述：广告项目
 * 创建日期：2023/8/18 9:21
 *
 * @folder ad/AdProjectController
 */
@Validated
@RequestMapping(value = "/ad/adproject")
@RestController
public class AdProjectController extends BaseController {

    @Autowired
    private TwadprojectServiceI twadprojectService;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    private TbflowServiceI tbflowServiceI;
    @Autowired
    private TbresourcetypeServiceI tbresourcetypeServiceI;

    /**
     * 分页查询广告项目
     * 方法功能:  分页查询广告项目
     *
     * @param pageRequeste
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twadproject>>
     * @author CGD
     * @date 2023/8/18 10:28
     */
    @GetMapping(value = "/getAdProjectPageList")
    public Json<List<AdprojectVO>> getAdProjectPageList(PageRequest pageRequeste, BaseQueryInfo query) {
        Page page = getPage(pageRequeste);
        IPage<AdprojectVO> adprojects = twadprojectService.getAdProjectPageList(page, query);
        return Json.success(adprojects);
    }

    /**
     * 广告项目保存
     * 方法功能: 广告项目保存
     *
     * @param adprojectDTO
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/8/18 10:28
     */
    @PostMapping(value = "/saveAdProject")
    public Json saveAdProject(@RequestBody TwadprojectDTO adprojectDTO) throws Exception {
        Twadproject adproject = new Twadproject();
        BeanUtils.copyProperties(adprojectDTO, adproject);
        if (twadprojectService.isExistAdProject(adproject)) {
            return Json.fail("项目名称已存在");
        }
        Long projectId = IdUtil.getSnowflakeNextId();
        adprojectDTO.setId(projectId);
        twadprojectService.saveAdProject(adprojectDTO);
        //申请审批
        if (StrUtil.isNotBlank(adprojectDTO.getFlowId())) {
            //判断是否需要审批 需要根据流程key获取一下tbflow，如果返回是null，则提示需要设置审批流程，
            // 如果返回的 tbflow的 bactive为0，则直接通过，不需要走审批流程，如果为1时，提交审批流程
            Tbflow flowInfo = tbflowServiceI.getFlowTypeByKey(FlowTypes.FLOW_PROJECT.getKey());
            if (flowInfo == null) {
                return Json.fail("请设置审批流程");
            }
            if (flowInfo.getBactive()) {
                Json<String> jsonRet = twadprojectService.approveAdProject(projectId.toString(),
                        adprojectDTO.getFlowId());
                if (!jsonRet.isSuccess()) {
                    return jsonRet;
                }
                String applicationid = jsonRet.getObj();
                if (StrUtil.isBlank(applicationid)) {
                    twadprojectService.updateAdprojectApprovalopinions(applicationid,
                            projectId.toString(), false, "", ApproveStatus.APPROVE_EDIT.getKey());
                    return Json.fail("申请审核失败！请重新申请");
                }
                //更新状态及申请Id
                twadprojectService.updateAdprojectApprovalopinions(applicationid, projectId.toString(),
                        false, "", ApproveStatus.APPROVE_EDITING.getKey());
            } else {
                twadprojectService.updateAdprojectApprovalopinions("", projectId.toString(), false, "",
                        ApproveStatus.APPROVE_PASS.getKey());
            }
        }
        return Json.success();
    }

    /**
     * 修改广告项目
     * 方法功能:  修改广告项目
     *
     * @param adprojectDTO
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/8/18 10:28
     */
    @PostMapping(value = "/updateAdProject")
    public Json updateAdProject(@RequestBody TwadprojectDTO adprojectDTO) {
        Twadproject adproject = new Twadproject();
        BeanUtils.copyProperties(adprojectDTO, adproject);
        if (twadprojectService.isExistAdProject(adproject)) {
            return Json.fail("项目名称已存在");
        }
        innerInterceptor.recoredLog();
        twadprojectService.updateById(adproject);
        //申请审批
        if (StrUtil.isNotBlank(adprojectDTO.getFlowId())) {
            //判断是否需要审批 需要根据流程key获取一下tbflow，如果返回是null，则提示需要设置审批流程，
            // 如果返回的 tbflow的 bactive为0，则直接通过，不需要走审批流程，如果为1时，提交审批流程
            Tbflow flowInfo = tbflowServiceI.getFlowTypeByKey(FlowTypes.FLOW_PROJECT.getKey());
            if (flowInfo == null) {
                return Json.fail("请设置审批流程");
            }
            if (flowInfo.getBactive()) {
                Json<String> jsonRet = twadprojectService.approveAdProject(adprojectDTO.getId().toString(),
                        adprojectDTO.getFlowId());
                if (!jsonRet.isSuccess()) {
                    return jsonRet;
                }
                String applicationid = jsonRet.getObj();
                if (StrUtil.isBlank(applicationid)) {
                    twadprojectService.updateAdprojectApprovalopinions(applicationid,
                            adprojectDTO.getId().toString(), false, "", ApproveStatus.APPROVE_EDIT.getKey());
                    return Json.fail("申请审核失败！请重新申请");
                }
                //更新状态及申请Id
                twadprojectService.updateAdprojectApprovalopinions(applicationid, adprojectDTO.getId().toString(),
                        false, "", ApproveStatus.APPROVE_EDITING.getKey());
            } else {
                twadprojectService.updateAdprojectApprovalopinions("", adprojectDTO.getId().toString(), false, "",
                        ApproveStatus.APPROVE_PASS.getKey());
            }
        }
        return Json.success();
    }

    /**
     * 根据id查询广告项目信息
     * 方法功能: 根据id查询广告项目信息
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Twadproject>
     * @author CGD
     * @date 2023/8/18 10:28
     */
    @GetMapping(value = "/getByAdProjectId")
    public Json<AdprojectVO> getByAdProjectId(@NotNull(message = "请传入需要查询的id") String id) {
        AdprojectVO byId = twadprojectService.getByAdProjectId(id);
        return Json.success(byId);
    }

    /**
     * 根据id删除广告项目信息
     * 方法功能:  根据id删除广告项目信息，支持","多选分割
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/8/18 10:28
     */
    @PostMapping(value = "/deleteAdProject")
    public Json deleteAdProject(@NotBlank(message = "请传入需要删除的id") String ids) {
        innerInterceptor.recoredLog();
        String sRet = twadprojectService.deleteAdProject(ids);
        if (StrUtil.isNotBlank(sRet)) {
            return Json.fail(sRet);
        } else {
            return Json.success();
        }
    }

    /**
     * 获取广告项目序号最大值
     * 方法功能: 获取广告项目序号最大值
     *
     * @param
     * @return com.hgzp.core.page.Json
     * @author CGD
     * @date 2023/8/21 14:24
     */
    @GetMapping("/getMaxSort")
    public Json getMaxSort() {
        Integer maxSort = twadprojectService.getMaxSort();
        return Json.success(maxSort);
    }

    /**
     * 结项项目
     * 方法功能: 结项项目
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2023/9/22 8:37
     */
    @PostMapping(value = "/endAdProject")
    public Json doEndAdProject(@NotBlank(message = "请传入需要结项的id") String ids) {
        innerInterceptor.recoredLog();
        Json jsonRet = twadprojectService.endAdProject(ids);
        return jsonRet;
    }

    /**
     * 获取广告项目列表
     * 方法功能: 获取广告项目列表
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twadproject>>
     * @author songly
     * @date 2023/12/5 15:46
     */
    @GetMapping(value = "/getAdProjectList")
    public Json<List<AdprojectVO>> getAdProjectList() {
        List<AdprojectVO> adprojects = twadprojectService.getAdProjectList();
        return Json.success(adprojects);
    }

    /**
     * 根据广告项目编码查询广告项目信息
     * 方法功能:
     *
     * @param sCode
     * @return com.hgzp.core.page.Json<com.hgzp.core.model.Twadproject>
     * @author songly
     * @date 2024/3/15 14:03
     */
    @GetMapping(value = "/getAdProjectByCode")
    public Json<Twadproject> getAdProjectByCode(@NotNull(message = "请传入需要查询的id") String sCode) {
        Twadproject byCode = twadprojectService.getAdProjectByCode(sCode);
        return Json.success(byCode);
    }

    /**
     * 项目审批
     * 方法功能:
     *
     * @param id
     * @param flowId
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/3/15 14:52
     */
    @PostMapping(value = "/approveAdProject")
    public Json approveAdProject(@NotNull(message = "ID不可为空") String id,
                                 @NotNull(message = "flowId不可为空") String flowId) {
        try {
            //判断是否需要审批 需要根据流程key获取一下tbflow，如果返回是null，则提示需要设置审批流程，
            // 如果返回的 tbflow的 bactive为0，则直接通过，不需要走审批流程，如果为1时，提交审批流程
            Tbflow flowInfo = tbflowServiceI.getFlowTypeByKey(FlowTypes.FLOW_PROJECT.getKey());
            if (flowInfo == null) {
                return Json.fail("请设置审批流程");
            }
            if (!flowInfo.getBactive()) {
                twadprojectService.updateAdprojectApprovalopinions("", id, false, "",
                        ApproveStatus.APPROVE_PASS.getKey());
                return Json.success();
            }
            //申请审批
            Json<String> jsonRet = twadprojectService.approveAdProject(id, flowId);
            if (!jsonRet.isSuccess()) {
                return jsonRet;
            }
            String applicationid = jsonRet.getObj();

            if (StrUtil.isBlank(applicationid)) {
                twadprojectService.updateAdprojectApprovalopinions(applicationid, id, false, "",
                        ApproveStatus.APPROVE_EDIT.getKey());
                return Json.fail("申请审核失败！请重新申请");
            }
            //更新状态及申请Id
            twadprojectService.updateAdprojectApprovalopinions(applicationid, id, false, "",
                    ApproveStatus.APPROVE_EDITING.getKey());

            return Json.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.fail("申请审核失败  " + e.getMessage());
        }
    }

    /**
     * 上传项目文件
     * 方法功能:
     *
     * @param request
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.common.UpFileModel>
     * @author songly
     * @date 2024/3/19 8:51
     */
    @PostMapping("/upLoadProjectFile")
    public Json<UpFileModel> upLoadAdProjectFile(HttpServletRequest request) throws Exception {
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
