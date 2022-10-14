package com.qimeixun.modules.system.controller;

import com.qimeixun.modules.system.service.SysLoginLogService;
import com.qimeixun.ro.SysLoginLog;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @desc 登录日志相关接口
 * @author yueyufan
 * @date 2019年11月19日 下午2:57:37
 */
@Api(tags = "登录日志相关接口")
@Controller
@RequestMapping(value = "/sys/loginLog", produces = "application/json;charset=UTF-8")
public class LoginLogController {

	@Resource
	private SysLoginLogService sysLoginLogService;

	@ApiOperation(value = "查询所有登录日志信息", notes = "查询所有登录日志信息")
	@RequestMapping(value = "/selectAll", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResultVO selectAll(@RequestBody SysLoginLog sysLoginLog) {
		return sysLoginLogService.queryAll(sysLoginLog);
	}

	//@SystemControllerLog(description = "删除登录日志信息")
	//@LoginCheck(permission = "sys:loginLog:delete")
	@ApiOperation(value = "删除登录日志信息", notes = "删除登录日志信息")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "记录ID", required = true, paramType = "query", dataType = "String") })
	@ResponseBody
	public ResponseResultVO delete(@RequestParam(value = "id") String id) {
		return sysLoginLogService.delete(id);
	}

}
