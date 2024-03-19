package com.hgzp.advertising.service.flow.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.service.flow.IProcessStarterService;
import com.hgzp.core.model.ProcessStarter;
import com.hgzp.mapper.flow.ProcessStarterMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 流程发起人 服务实现类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessStarterServiceImpl extends ServiceImpl<ProcessStarterMapper, ProcessStarter> implements IProcessStarterService {

}
