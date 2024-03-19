package com.hgzp.advertising.service.finance.impl;


import com.hgzp.advertising.service.finance.TbbusinessentityServiceI;
import com.hgzp.core.model.Tbbusinessentity;
import com.hgzp.mapper.finance.TbbusinessentityMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 经营主体 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbbusinessentityServiceImpl extends MyServiceImpl<TbbusinessentityMapper, Tbbusinessentity> implements TbbusinessentityServiceI {
    @Autowired
    TbbusinessentityMapper businessentityMapper;


}
