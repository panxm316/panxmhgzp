package com.hgzp.advertisingsys.service.ad.impl;

import com.hgzp.advertisingsys.service.ad.TbadfromextendServiceI;
import com.hgzp.core.model.Tbadfromextend;
import com.hgzp.mapper.ad.TbadfromextendMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 广告来源扩展表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbadfromextendServiceImpl extends ServiceImpl<TbadfromextendMapper, Tbadfromextend> implements TbadfromextendServiceI {

}
