package com.qimeixun.modules.system.controller;

import cn.hutool.core.util.StrUtil;
import com.qimeixun.base.BaseController;
import com.qimeixun.ro.UpdateSysConfigRO;
import com.qimeixun.service.SysConfigService;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "系统参数配置")
@RestController
@RequestMapping(value = "/sys/config", produces = "application/json;charset=UTF-8")
public class SysConfigController extends BaseController {

    @Resource
    SysConfigService sysConfigService;

    @ApiOperation(value = "查询系统配置列表", notes = "查询系统配置列表")
    @RequestMapping(value = "/selectConfigList", method = RequestMethod.GET)
    public ResponseResultVO selectConfigList(@RequestParam(required = false) String keyType) {
        return ResponseResultVO.successResult(sysConfigService.selectConfigList(keyType));
    }

    @ApiOperation(value = "修改系统配置", notes = "修改系统配置")
    @RequestMapping(value = "/updateConfig", method = RequestMethod.POST)
    public ResponseResultVO updateConfig(@RequestBody UpdateSysConfigRO updateSysConfigRO) {
        if(updateSysConfigRO.getId() == null){
            return ResponseResultVO.failResult("参数不正确");
        }
        if(StrUtil.isBlank(updateSysConfigRO.getConfigValue())){
            return ResponseResultVO.failResult("修改的值不能为空");
        }
        return updateResult(sysConfigService.updateConfig(updateSysConfigRO));
    }
}
