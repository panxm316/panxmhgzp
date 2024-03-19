package com.hgzp.advertising.service.finance.impl;


import com.hgzp.advertising.service.finance.TbadprintitemServiceI;
import com.hgzp.core.model.Tbadprintitem;
import com.hgzp.mapper.finance.TbadprintitemMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 开票项目 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbadprintitemServiceImpl extends MyServiceImpl<TbadprintitemMapper, Tbadprintitem> implements TbadprintitemServiceI {
    @Resource
    private TbadprintitemMapper mapper;


}
