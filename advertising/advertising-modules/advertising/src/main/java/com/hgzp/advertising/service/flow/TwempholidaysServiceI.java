package com.hgzp.advertising.service.flow;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.flow.dto.EmpHolidayDTO;
import com.hgzp.core.model.Twempholidays;
import com.hgzp.core.page.Json;
import com.hgzp.service.common.IMyService;

import java.util.List;

/**
 * <p>
 * 人员假期表，假期期间工作移交给其他人员 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-10-23
 */
public interface TwempholidaysServiceI extends IMyService<Twempholidays> {
    /**
     * getHolidayPageList
     * 方法功能: 获取休假分页列表
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twempholidays>
     * @author suny
     * @date 2023/10/24 13:26
     */
    IPage<Twempholidays> getHolidayPageList(Page<Twempholidays> page, Twempholidays query) throws Exception;


    /**
     * replaceHolidayUser
     * 方法功能: 替换休假人员,如果替换的人员也休假则继续由前人处理
     *
     * @param userIdList
     * @return java.util.List<java.lang.String>
     * @author wangwk
     * @date 2023/10/23 17:08
     */
    List<String> replaceHolidayUser(List<String> userIdList);

    /**
     * saveEmpHolidays
     * 方法功能: 保存休假信息
     *
     * @param empholidays
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/10/24 13:26
     */
    Json saveEmpHolidays(EmpHolidayDTO empholidays) throws Exception;

    /**
     * updateEmpHolidays
     * 方法功能: 更新休假信息
     *
     * @param empholidays
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/10/25 10:30
     */
    Json updateEmpHolidays(EmpHolidayDTO empholidays) throws Exception;
}
