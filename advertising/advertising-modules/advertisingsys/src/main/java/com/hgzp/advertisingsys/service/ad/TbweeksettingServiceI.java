package com.hgzp.advertisingsys.service.ad;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.ad.vo.WeekSettingVO;
import com.hgzp.core.model.Tbadtype;
import com.hgzp.core.model.Tbweeksetting;
import com.hgzp.service.common.IMyService;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 星期设置 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
public interface TbweeksettingServiceI extends IMyService<Tbweeksetting> {

    /**
     * getWeekSettingPageList
     * 方法功能:分页：获取星期设置
     *
     * @param page
     * @param vo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbweeksetting>
     * @author yanz
     * @date 2023/8/31 15:10
     */
    IPage<Tbweeksetting> getWeekSettingPageList(Page<Tbweeksetting> page, WeekSettingVO vo);

    /**
     * deleteWeekSettingByIds
     * 方法功能:据id批量删除
     *
     * @param idList
     * @return boolean
     * @author yanz
     * @date 2023/8/25 10:24
     */
    boolean deleteWeekSettingByIds(List<String> idList);

    /**
     * 判重名
     * 方法功能:
     *
     * @param id
     * @param sname
     * @return boolean
     * @author songly
     * @date 2024/2/27 9:20
     */
    boolean isDuplicateSname(Long id, String sname);
}
