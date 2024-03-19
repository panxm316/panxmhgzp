package com.hgzp.advertisingsys.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertisingsys.pagemodel.system.dto.ParameterDTO;
import com.hgzp.advertisingsys.service.system.TwparameterServiceI;
import com.hgzp.core.model.*;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.mapper.system.TwparameterMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 * 系统参数表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TwparameterServiceImpl extends ServiceImpl<TwparameterMapper, Twparameter> implements TwparameterServiceI {


    /***
     *
     * 方法功能: 更新Twparameter
     * @author lijp
     * @date 2024/3/7 13:05
     * @param parameterDTO
     * @return void
     */
    @Override
    public void updateParm(ParameterDTO parameterDTO) {

        Twparameter twparameter = new Twparameter();
        BeanUtils.copyProperties(parameterDTO, twparameter);
        log.info("系统参数:" + twparameter);
        this.updateById(twparameter);
    }


    /***
     *
     * 方法功能: 保存Twparameter
     * @author lijp
     * @date 2024/3/7 13:05
     * @param parameterDTO
     * @return void
     */
    @Override
    public void saveParm(ParameterDTO parameterDTO) {

        if (Objects.isNull(parameterDTO)) {
            return;
        }
        Twparameter twparameter = new Twparameter();
        BeanUtils.copyProperties(parameterDTO, twparameter);
        log.info("系统参数:" + twparameter);
        this.save(twparameter);
    }

    @Autowired
    private TwparameterMapper twparameterMapper;
    /***
     * 获取系统参数分页列表
     * 方法功能: 获取系统参数分页列表
     * @author lijp
     * @date 2024/3/15 15:16
     * @param page
     * @param info
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twparameter>
     */
    @Override
    public IPage<Twparameter> getParameterPageList(Page<Twparameter> page, BaseQueryInfo info) throws Exception {
        LambdaQueryWrapper<Twparameter> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(info.getQueryKey()), Twparameter::getSname, info.getQueryKey());
        return twparameterMapper.selectPage(page, lqw);


    }
}


