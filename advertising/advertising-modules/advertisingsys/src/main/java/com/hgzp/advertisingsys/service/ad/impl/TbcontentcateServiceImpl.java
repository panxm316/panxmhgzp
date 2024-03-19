package com.hgzp.advertisingsys.service.ad.impl;

import com.hgzp.advertisingsys.service.ad.TbcontentcateServiceI;
import com.hgzp.core.model.Tbcontentcate;
import com.hgzp.mapper.ad.TbcontentcateMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 内容类别(暂时不用) 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbcontentcateServiceImpl extends ServiceImpl<TbcontentcateMapper, Tbcontentcate> implements TbcontentcateServiceI {

}
