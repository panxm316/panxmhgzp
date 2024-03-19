package com.hgzp.advertisingsys.service.ad.impl;

import com.hgzp.advertisingsys.service.ad.TbadindustryextendServiceI;
import com.hgzp.core.model.Tbadindustryextend;
import com.hgzp.mapper.ad.TbadindustryextendMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 广告行业扩展表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbadindustryextendServiceImpl extends ServiceImpl<TbadindustryextendMapper, Tbadindustryextend> implements TbadindustryextendServiceI {

}
