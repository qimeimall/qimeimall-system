package com.qimeixun.aspect;

import com.qimeixun.mapper.SysLogMapper;
import com.qimeixun.po.LogDTO;
import com.qimeixun.util.TokenUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LogRecord {

    @Resource
    SysLogMapper sysLogMapper;

    @Async
    public void inertLogRecord(LogDTO logDTO) {
        sysLogMapper.insert(logDTO);
    }
}
