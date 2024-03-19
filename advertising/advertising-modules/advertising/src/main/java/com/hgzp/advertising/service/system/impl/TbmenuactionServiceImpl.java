package com.hgzp.advertising.service.system.impl;


import com.hgzp.advertising.service.system.TbmenuactionServiceI;
import com.hgzp.service.system.impl.BaseTbmenuactionServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 菜单行为表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbmenuactionServiceImpl extends BaseTbmenuactionServiceImpl implements TbmenuactionServiceI {


}
