package com.qimeixun.modules.system.controller;

import com.qimeixun.modules.system.service.SysLogService;
import com.qimeixun.ro.SysLogRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @desc 系统操作日志处理类
 * @author yueyufan
 * @date 2019年11月22日 上午10:08:28
 */
@Api(tags = "系统操作日志接口")
@Controller
@RequestMapping(value = "/sys/log", produces = "application/json;charset=UTF-8")
public class SysLogController {

	@Resource
	private SysLogService sysLogService;

	//@LoginCheck()
	@ApiOperation(value = "查询所有系统操作日志信息", notes = "查询所有系统操作日志信息")
	@RequestMapping(value = "/selectAll", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResultVO selectAll(@RequestBody SysLogRO sysLogRO) {
		return sysLogService.selectAll(sysLogRO);
	}

	//@SystemControllerLog(description = "单条删除系统操作日志信息")
	//@LoginCheck(permission = "sys:log:deleteSingle")
	@ApiOperation(value = "单条删除系统操作日志信息", notes = "单条删除系统操作日志信息")
	@RequestMapping(value = "/deleteSingle", method = RequestMethod.POST)
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "系统日志记录ID", required = true, paramType = "query", dataType = "String") })
	@ResponseBody
	public ResponseResultVO deleteSingle(@RequestParam(value = "id") String id) {
		return sysLogService.deleteByPrimaryKey(id);
	}

	//@SystemControllerLog(description = "批量删除系统操作日志信息")
	//@LoginCheck(permission = "sys:log:delete")
	@ApiOperation(value = "批量删除系统操作日志信息", notes = "批量删除系统操作日志信息")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ApiImplicitParams({ @ApiImplicitParam(name = "ids", value = "系统日志记录ID拼接串", required = true, paramType = "query", dataType = "String") })
	@ResponseBody
	public ResponseResultVO delete(@RequestParam(value = "ids") String ids) {
		return sysLogService.delete(ids);
	}

}
