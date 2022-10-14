package com.qimeixun.modules.system.service;

import com.qimeixun.ro.SysLogRO;
import com.qimeixun.vo.ResponseResultVO;

public interface SysLogService {

	// 删除
	ResponseResultVO deleteByPrimaryKey(String logId);

	// 批量删除
	ResponseResultVO delete(String ids);

	// 查询所有
	ResponseResultVO selectAll(SysLogRO sysLogRO);

}
