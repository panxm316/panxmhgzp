package com.hgzp.advertisingsys.service.media.impl;

import com.hgzp.core.model.Tbfoldareaver;
import com.hgzp.mapper.media.TbfoldareaverMapper;
import com.hgzp.advertisingsys.service.media.TbfoldareaverServiceI;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 叠次版本 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbfoldareaverServiceImpl extends ServiceImpl<TbfoldareaverMapper, Tbfoldareaver> implements TbfoldareaverServiceI {

}
