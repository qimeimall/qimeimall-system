package com.qimeixun.modules.system.service.impl;

import com.qimeixun.mapper.SysLoginLogMapper;
import com.qimeixun.ro.SysLoginLog;
import com.qimeixun.modules.system.service.SysLoginLogService;
import com.qimeixun.vo.ResponseResultVO;
import com.qimeixun.vo.SysLoginLogVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysLoginLogServiceImpl implements SysLoginLogService {

	@Resource
	private SysLoginLogMapper sysLoginLogMapper;

	@Override
	public ResponseResultVO insert(SysLoginLog obj) {
		int result = sysLoginLogMapper.insert(obj);
		if (result == 1) {
			return ResponseResultVO.successResult();
		}
		return ResponseResultVO.failResult();
	}

	@Override
	public ResponseResultVO delete(String id) {
		int result = sysLoginLogMapper.deleteByPrimaryKey(id);
		if (result == 1) {
			return ResponseResultVO.successResult();
		}
		return ResponseResultVO.failResult();
	}

	@Override
	public ResponseResultVO queryAll(SysLoginLog sysLoginLog) {
		List<SysLoginLogVO> list = sysLoginLogMapper.queryAll(sysLoginLog);
        int count = sysLoginLogMapper.queryAllCount(sysLoginLog);
        return ResponseResultVO.resultList(list, count, sysLoginLog);
	}

}
