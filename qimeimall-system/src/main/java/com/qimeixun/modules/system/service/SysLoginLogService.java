package com.qimeixun.modules.system.service;

import com.qimeixun.ro.SysLoginLog;
import com.qimeixun.vo.ResponseResultVO;

/**
 * @desc 系统登录日志接口
 * @author yueyufan
 * @date 2019年11月19日 上午8:54:11
 */
public interface SysLoginLogService {
	
	//新增
	ResponseResultVO insert(SysLoginLog obj);
	
	//删除
	ResponseResultVO delete(String id);
	
	//查询所有
	ResponseResultVO queryAll(SysLoginLog sysLoginLog);
	
}
