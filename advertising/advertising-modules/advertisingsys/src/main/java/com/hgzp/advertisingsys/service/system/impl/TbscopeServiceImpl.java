package com.hgzp.advertisingsys.service.system.impl;

import com.hgzp.advertisingsys.service.system.TbscopeServiceI;
import com.hgzp.service.system.impl.BaseTbscopeServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 范围表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbscopeServiceImpl extends BaseTbscopeServiceImpl implements TbscopeServiceI {

}
