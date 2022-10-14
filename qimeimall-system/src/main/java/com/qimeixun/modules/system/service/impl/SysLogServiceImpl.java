package com.qimeixun.modules.system.service.impl;

import com.qimeixun.mapper.SysLogMapper;
import com.qimeixun.ro.SysLogRO;
import com.qimeixun.modules.system.service.SysLogService;
import com.qimeixun.vo.ResponseResultVO;
import com.qimeixun.vo.SysLogVO;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {

	@Resource
	private SysLogMapper sysLogMapper;

	@Override
	public ResponseResultVO deleteByPrimaryKey(String logId) {
		int result = sysLogMapper.deleteByPrimaryKey(logId);
		if (result == 1) {
			return ResponseResultVO.successResult();
		}
		return ResponseResultVO.failResult();
	}

	@Override
	public ResponseResultVO selectAll(SysLogRO sysLogRO) {
		List<SysLogVO> list = sysLogMapper.selectAll(sysLogRO);
		int count = sysLogMapper.selectAllCount(sysLogRO);
		return  ResponseResultVO.resultList(list, count, sysLogRO);
	}

	@Override
	public ResponseResultVO delete(String ids) {
		int result = sysLogMapper.deleteBatch(ids.split(","));
		if (result > 0) {
			return ResponseResultVO.successResult();
		}
		return ResponseResultVO.failResult();
	}

}
