package com.qimeixun.modules.seckill.controller;

import com.qimeixun.annotation.LoginCheck;
import com.qimeixun.base.BaseController;
import com.qimeixun.modules.seckill.service.SeckillConfigService;
import com.qimeixun.ro.SeckillConfigRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/6/1922:28
 */
@Api(tags = "秒杀配置")
@RestController
@RequestMapping(value = "/seckill", produces = "application/json;charset=UTF-8")
public class SeckillConfigController extends BaseController {

    @Resource
    SeckillConfigService seckillConfigService;

    @ApiOperation(value = "新增秒杀配置", notes = "新增秒杀配置")
    @RequestMapping(value = "/insertSeckillConfig", method = RequestMethod.POST)
    //@LoginCheck
    public ResponseResultVO insertSeckillConfig(@RequestBody SeckillConfigRO seckillConfigRO) {
        return insertResult(seckillConfigService.insertSeckillConfig(seckillConfigRO));
    }

    @ApiOperation(value = "修改秒杀配置", notes = "修改秒杀配置")
    @RequestMapping(value = "/updateSeckillConfigById", method = RequestMethod.POST)
    //@LoginCheck
    public ResponseResultVO updateSeckillConfig(@RequestBody SeckillConfigRO seckillConfigRO) {
        return updateResult(seckillConfigService.updateSeckillConfig(seckillConfigRO));
    }

    @ApiOperation(value = "删除秒杀配置", notes = "删除秒杀配置")
    @RequestMapping(value = "/deleteSeckillConfigById", method = RequestMethod.GET)
    //@LoginCheck
    public ResponseResultVO deleteSeckillConfig(@RequestParam String id) {
        return deleteResult(seckillConfigService.deleteSeckillConfig(id));
    }

    @ApiOperation(value = "查询指定id秒杀配置", notes = "查询秒杀配置")
    @RequestMapping(value = "/selectSeckillConfigById", method = RequestMethod.GET)
    //@LoginCheck
    public ResponseResultVO selectSeckillConfigById(@RequestParam String id) {
        return ResponseResultVO.successResult(seckillConfigService.selectSeckillConfigById(id));
    }

    @ApiOperation(value = "查询秒杀配置", notes = "查询秒杀配置")
    @RequestMapping(value = "/selectSeckillConfigList", method = RequestMethod.POST)
    //@LoginCheck
    public ResponseResultVO selectSeckillConfig(@RequestBody SeckillConfigRO seckillConfigRO) {
        return ResponseResultVO.successResult(seckillConfigService.selectSeckillConfig(seckillConfigRO));
    }
}
