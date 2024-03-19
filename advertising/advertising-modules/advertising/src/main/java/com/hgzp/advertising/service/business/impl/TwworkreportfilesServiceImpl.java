package com.hgzp.advertising.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.service.business.TwworkreportfilesServiceI;
import com.hgzp.core.model.Twworkreportfiles;
import com.hgzp.mapper.business.TwworkreportfilesMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 工作报告文件表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
@Service
public class TwworkreportfilesServiceImpl extends ServiceImpl<TwworkreportfilesMapper, Twworkreportfiles> implements TwworkreportfilesServiceI {
    @Override
    public void deleteByWorkReportIds(List<Long> ids) {
        LambdaQueryWrapper<Twworkreportfiles> queryWrapper = Wrappers.lambdaQuery(Twworkreportfiles.class)
                .in(Twworkreportfiles::getWorkreportid, ids);
        this.remove(queryWrapper);
    }
}
