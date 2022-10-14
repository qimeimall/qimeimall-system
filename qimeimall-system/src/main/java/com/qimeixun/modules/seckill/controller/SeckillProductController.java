package com.qimeixun.modules.seckill.controller;

import com.qimeixun.annotation.LoginCheck;
import com.qimeixun.base.BaseController;
import com.qimeixun.modules.seckill.service.SeckillProductService;
import com.qimeixun.ro.SeckillProductRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author chenshouyang
 * @date 2020/6/2814:09
 */
@Api(tags = "秒杀产品")
@RestController
@RequestMapping(value = "/seckillProduct", produces = "application/json;charset=UTF-8")
public class SeckillProductController extends BaseController {

    @Resource
    SeckillProductService seckillProductService;

    @ApiOperation(value = "添加秒杀产品", notes = "添加秒杀产品")
    @RequestMapping(value = "/insertSeckillProduct", method = RequestMethod.POST)
    //@LoginCheck
    public ResponseResultVO insertSeckillProduct(@RequestBody SeckillProductRO seckillProductRO) {
        if (seckillProductRO.getEndTime() == null || seckillProductRO.getStartTime() == null ||
                seckillProductRO.getProductId() == null || seckillProductRO.getSeckillConfigId() == null ||
                seckillProductRO.getSeckillPrice() == null || seckillProductRO.getStock() < 0) {
            return ResponseResultVO.failResult("参数错误");
        }
        if(seckillProductRO.getSeckillPrice().compareTo(BigDecimal.ZERO) == -1){
            return ResponseResultVO.failResult("参数错误");
        }
        return insertResult(seckillProductService.insertSeckillProduct(seckillProductRO));
    }

    @ApiOperation(value = "查询秒杀产品列表", notes = "查询秒杀产品列表")
    @RequestMapping(value = "/selectSeckillProductList", method = RequestMethod.POST)
    //@LoginCheck
    public ResponseResultVO selectSeckillProductList(@RequestBody SeckillProductRO seckillProductRO) {
        return getPageObject(seckillProductService.selectSeckillProductList(seckillProductRO), seckillProductRO);
    }

    @ApiOperation(value = "删除秒杀产品", notes = "删除秒杀产品")
    @RequestMapping(value = "/deleteSeckillProductById", method = RequestMethod.GET)
    //@LoginCheck
    public ResponseResultVO deleteSeckillProductById(@RequestParam String id) {
        return updateResult(seckillProductService.deleteSeckillProductById(id));
    }

    @ApiOperation(value = "更新状态", notes = "更新状态")
    @RequestMapping(value = "/updateStatusSeckillProductById", method = RequestMethod.GET)
    //@LoginCheck
    public ResponseResultVO updateStatusSeckillProductById(@RequestParam String id) {
        return updateResult(seckillProductService.updateStatusSeckillProductById(id));
    }

    @ApiOperation(value = "查询秒杀产品详情", notes = "查询秒杀产品详情")
    @RequestMapping(value = "/selectSeckillProductById", method = RequestMethod.GET)
    //@LoginCheck
    public ResponseResultVO selectSeckillProductById(@RequestParam String id) {
        return ResponseResultVO.successResult(seckillProductService.selectSeckillProductById(id));
    }

    @ApiOperation(value = "修改秒杀产品", notes = "修改秒杀产品")
    @RequestMapping(value = "/updateSeckillProduct", method = RequestMethod.POST)
    //@LoginCheck
    public ResponseResultVO updateSeckillProduct(@RequestBody SeckillProductRO seckillProductRO) {
        if(seckillProductRO.getSeckillPrice() != null && seckillProductRO.getSeckillPrice().compareTo(BigDecimal.ZERO) == -1){
            return ResponseResultVO.failResult("参数错误");
        }
        return updateResult(seckillProductService.updateSeckillProduct(seckillProductRO));
    }
}
