package com.hgzp.advertising.service.ad;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.model.Twadproject;
import com.hgzp.core.model.Twspecialproject;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.pagemodel.ad.AdProjectCountVO;
import com.hgzp.service.common.IMyService;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 特刊项目 服务类
 * </p>
 *
 * @author muyn
 * @since 2024-03-13
 */
public interface SpecialProjectServiceI extends IMyService<Twspecialproject> {
    /**
     * 根据ID查询Twspecialproject
     *
     * @author muyn
     * @since 2024-03-13
     * @param id Twspecialproject.id
     * @return {@link Twspecialproject}
     */
    @Override
    Twspecialproject getById(Serializable id);

    /**
     * saveSpecialProject
     * 方法功能: 保存
     *
     * @param specialproject
     * @return void
     * @author lhl
     * @date 2024/3/13
     */
    void saveAdProject(Twspecialproject specialproject, HttpServletRequest request) throws Exception;

    /**
     * 是否存在名称相同的特刊项目
     * 方法功能:
     *
     * @param specialproject
     * @return boolean
     * @author lhl
     * @date 2024/3/13
     */
    boolean isExistSpecialProject(Twspecialproject specialproject);

    /**
     * 获取特刊项目列表
     * 方法功能:
     *
     * @param
     * @return java.util.List<com.hgzp.core.model.Twspecialproject>
     * @author songly
     * @date 2024/3/13
     */
    IPage<Twspecialproject> getSpecialProjectPageList(Page<Twspecialproject> page, BaseQueryInfo query);

    /**
     * deleteSpecialProject
     * 方法功能: 根据id删除特刊项目信息，支持","多选分割
     *
     * @param ids
     * @return void
     * @author lhl
     * @date 2023/3/13
     */
    String deleteSpecialProject(String ids);

    /**
     * 特刊项目汇总
     * 方法功能: 特刊项目汇总
     *
     * @param stratTime,endTime,adProjectId,pageNum,pageSize,publistStatus,queryKey,projectEnd
     * @return AdProjectCountVO
     * @author lhl
     * @date 2024/3/15
     */
    IPage<AdProjectCountVO> getSpecialProjectCountList(String stratTime, String endTime, String adProjectId, int pageNum, int pageSize, String publishstatus, String queryKey, String projectEnd);


}
