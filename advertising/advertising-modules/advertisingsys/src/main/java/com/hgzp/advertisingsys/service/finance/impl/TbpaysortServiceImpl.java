package com.hgzp.advertisingsys.service.finance.impl;

import com.hgzp.advertisingsys.service.finance.TbpaysortServiceI;
import com.hgzp.core.model.Tbpaysort;
import com.hgzp.mapper.finance.TbpaysortMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 付款时间设置 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbpaysortServiceImpl extends ServiceImpl<TbpaysortMapper, Tbpaysort> implements TbpaysortServiceI {

}
