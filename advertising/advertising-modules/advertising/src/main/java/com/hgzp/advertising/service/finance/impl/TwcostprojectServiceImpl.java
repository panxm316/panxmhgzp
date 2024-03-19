package com.hgzp.advertising.service.finance.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.service.finance.TwcostprojectServiceI;
import com.hgzp.core.model.Twcostproject;
import com.hgzp.mapper.finance.TwcostprojectMapper;
import com.hgzp.utils.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 成本表(项目) 服务实现类
 * </p>
 *
 * @author muyn
 * @since 2024-03-15
 */
@Service
public class TwcostprojectServiceImpl extends ServiceImpl<TwcostprojectMapper, Twcostproject> implements TwcostprojectServiceI {

    @Autowired
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public void importCostProject(FileInfo upfile) throws Exception {

    }
}
