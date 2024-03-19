package com.hgzp.advertisingsys.service.ad.impl;

import com.hgzp.advertisingsys.service.ad.TbcontentcategroupServiceI;
import com.hgzp.core.model.Tbcontentcategroup;
import com.hgzp.mapper.ad.TbcontentcategroupMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 内容类别_分组(暂时不用) 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbcontentcategroupServiceImpl extends ServiceImpl<TbcontentcategroupMapper, Tbcontentcategroup> implements TbcontentcategroupServiceI {

}
