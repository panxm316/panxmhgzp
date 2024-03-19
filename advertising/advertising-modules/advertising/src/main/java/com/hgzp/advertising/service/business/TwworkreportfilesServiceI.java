package com.hgzp.advertising.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.core.model.Twworkreportfiles;

import java.util.List;

/**
 * <p>
 * 工作报告文件表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
public interface TwworkreportfilesServiceI extends IService<Twworkreportfiles> {
    /**
     * 方法功能: 根据工作报告id删除相关文件
     *
     * @param ids
     * @return void
     * @author suny
     * @date 2023/10/30 9:43
     */
    void deleteByWorkReportIds(List<Long> ids);
}
