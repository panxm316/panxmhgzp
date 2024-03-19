package com.hgzp.advertising.service.system.impl;

import com.hgzp.advertising.service.system.TbemployroleServiceI;
import com.hgzp.service.system.impl.BaseTbemployroleServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 人员角色表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbemployroleServiceImpl extends BaseTbemployroleServiceImpl implements TbemployroleServiceI {

}
