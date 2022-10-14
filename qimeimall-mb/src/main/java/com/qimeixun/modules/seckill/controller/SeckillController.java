package com.qimeixun.modules.seckill.controller;

import com.qimeixun.modules.seckill.service.SeckillService;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/6/3017:28
 */
@Api(tags = "秒杀")
@RestController
@RequestMapping(value = "/seckill", produces = "application/json;charset=UTF-8")
public class SeckillController {

    @Resource
    SeckillService seckillService;

    @ApiOperation(value = "获取秒杀配置列表", notes = "获取秒杀配置列表")
    @RequestMapping(value = "/getSeckillConfigList", method = RequestMethod.GET)
    public ResponseResultVO getSeckillConfigList() {
        return ResponseResultVO.successResult(seckillService.getSeckillConfigList());
    }

    @ApiOperation(value = "查询秒杀产品列表", notes = "查询秒杀产品列表")
    @RequestMapping(value = "/getSeckillProductList", method = RequestMethod.GET)
    public ResponseResultVO getSeckillProductList(@RequestParam(required = false) String storeId, @RequestParam String seckillConfigId) {
        return ResponseResultVO.successResult(seckillService.getSeckillProductList(storeId, seckillConfigId));
    }

    @ApiOperation(value = "查询秒杀产品详情", notes = "查询秒杀产品详情")
    @RequestMapping(value = "/getSeckillProduct", method = RequestMethod.GET)
    public ResponseResultVO getSeckillProduct(@RequestParam String seckillProductId) {
        return ResponseResultVO.successResult(seckillService.getSeckillProduct(seckillProductId));
    }
}
