package com.qimeixun.modules.pay.controller;

import com.qimeixun.annotation.JwtIgnore;
import com.qimeixun.modules.pay.service.PayCommonService;
import com.qimeixun.modules.pay.service.PayService;
import com.qimeixun.ro.PayRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

/**
 * @author chenshouyang
 * @date 2020/5/2713:44
 */
@Api(tags = "支付")
@RestController
@RequestMapping(value = "/pay", produces = "application/json;charset=UTF-8")
public class PayController {

    @Resource
    PayCommonService payCommonService;

    @ApiOperation(value = "订单支付", notes = "订单支付")
    @RequestMapping(value = "/orderPay", method = RequestMethod.POST)
    public ResponseResultVO orderPay(@RequestBody PayRO payRO) {
        return payCommonService.orderPay(payRO);
    }

    @ApiOperation(value = "微信支付回调函数", notes = "微信支付回调函数")
    @RequestMapping(value = "/wx/getNotifyInfo", method = RequestMethod.POST)
    @JwtIgnore
    public String orderPay(@RequestBody String data) {
        return payCommonService.wxPayNotify(data);
    }

}
