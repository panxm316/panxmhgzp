package com.hgzp.service;

import com.hgzp.core.constant.SecurityConstants;
import com.hgzp.core.model.Twlog;

import com.hgzp.mapper.system.TwlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @description: 异步保存日志
 * @author: wangwk
 * @create：2023/8/4 10:22
 */
@Service
public class AsyncLogService {

    @Autowired
    TwlogMapper worklogMapper;

    @Async
    public void saveSysLog(Twlog sysOperLog) throws Exception {
        worklogMapper.insert(sysOperLog);
    }

}
