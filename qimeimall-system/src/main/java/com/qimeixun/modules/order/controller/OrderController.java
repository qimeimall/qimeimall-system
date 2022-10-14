package com.qimeixun.modules.order.controller;

import com.qimeixun.base.BaseController;
import com.qimeixun.modules.order.service.OrderService;
import com.qimeixun.ro.*;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/6/310:53
 */
@Api(tags = "订单管理")
@RestController
@RequestMapping(value = "/order", produces = "application/json;charset=UTF-8")
public class OrderController extends BaseController {

    @Resource
    OrderService orderService;

    @ApiOperation(value = "查询订单列表", notes = "查询订单列表")
    @RequestMapping(value = "/selectOrderList", method = RequestMethod.POST)
    public ResponseResultVO selectOrderList(@RequestBody OrderListRO orderListRO) {
        return getPageObject(orderService.selectOrderList(orderListRO), orderListRO);
    }

    @ApiOperation(value = "查询售后申请列表", notes = "查询售后申请列表")
    @RequestMapping(value = "/selectRefundOrderList", method = RequestMethod.POST)
    public ResponseResultVO selectRefundOrderList(@RequestBody RefundOrderSystemRO orderListRO) {
        return getPageObject(orderService.selectRefundOrderList(orderListRO), orderListRO);
    }

    @ApiOperation(value = "查询订单详情", notes = "查询订单详情")
    @RequestMapping(value = "/selectOrderById", method = RequestMethod.GET)
    public ResponseResultVO selectOrderById(@RequestParam String orderId) {
        return ResponseResultVO.successResult(orderService.selectOrderById(orderId));
    }

    @ApiOperation(value = "订单发货", notes = "查询订单详情")
    @RequestMapping(value = "/deliverGoods", method = RequestMethod.POST)
    public ResponseResultVO deliverGoods(@RequestBody ExpressOrderRO expressOrderRO) {
        if (expressOrderRO.getOrderId() == null || expressOrderRO.getExpressId() == null || StringUtils.isEmpty(expressOrderRO.getExpressNumber())) {
            return ResponseResultVO.failResult("参数不能为空");
        }
        return updateResult(orderService.deliverGoods(expressOrderRO));
    }

    @ApiOperation(value = "自提订单发货", notes = "自提订单发货")
    @RequestMapping(value = "/userReceiveGoods", method = RequestMethod.GET)
    public ResponseResultVO userReceiveGoods(@RequestParam String orderId) {

        return updateResult(orderService.userReceiveGoods(orderId));
    }

    @ApiOperation(value = "通过退货", notes = "通过退货")
    @RequestMapping(value = "/accessRefund", method = RequestMethod.POST)
    public ResponseResultVO accessRefund(@RequestBody RefundUpdateRO refundUpdateRO) {
        orderService.accessRefund(refundUpdateRO);
        return  ResponseResultVO.successResult();
    }

    @ApiOperation(value = "拒绝退货", notes = "拒绝退货")
    @RequestMapping(value = "/refusedRefund", method = RequestMethod.POST)
    public ResponseResultVO refusedRefund(@RequestBody RefundUpdateRO refundUpdateRO) {
        orderService.refusedRefund(refundUpdateRO);
        return ResponseResultVO.successResult();
    }

    @ApiOperation(value = "退款", notes = "退款")
    @RequestMapping(value = "/refundMoney", method = RequestMethod.GET)
    public ResponseResultVO refundMoney(@RequestParam String refundId) {
        orderService.refundMoney(refundId);
        return ResponseResultVO.successResult();
    }
}
