package com.hgzp.advertisingsys.service.media.impl;

import com.hgzp.core.model.Tbadspec;
import com.hgzp.mapper.media.TbadspecMapper;
import com.hgzp.advertisingsys.service.media.TbadspecServiceI;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 广告规格 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbadspecServiceImpl extends ServiceImpl<TbadspecMapper, Tbadspec> implements TbadspecServiceI {

}
