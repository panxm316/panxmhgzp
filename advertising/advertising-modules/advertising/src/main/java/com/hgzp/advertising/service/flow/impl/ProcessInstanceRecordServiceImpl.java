package com.hgzp.advertising.service.flow.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.service.flow.IProcessInstanceRecordService;
import com.hgzp.core.model.ProcessInstanceRecord;
import com.hgzp.mapper.flow.ProcessInstanceRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 流程记录 服务实现类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessInstanceRecordServiceImpl extends ServiceImpl<ProcessInstanceRecordMapper, ProcessInstanceRecord> implements IProcessInstanceRecordService {

}
