package com.qimeixun.modules.order.controller;

import cn.hutool.core.util.StrUtil;
import com.qimeixun.base.BaseController;
import com.qimeixun.entity.ExpressInfo;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.impl.KdApiSearchService;
import com.qimeixun.modules.order.service.OrderService;
import com.qimeixun.ro.*;
import com.qimeixun.service.OrderCommonService;
import com.qimeixun.util.TokenUtil;
import com.qimeixun.vo.OrderExpressVO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/5/2520:27
 */
@Api(tags = "首页")
@RestController
@RequestMapping(value = "/order", produces = "application/json;charset=UTF-8")
public class OrderController extends BaseController {

    @Resource
    OrderService orderService;

    @Resource
    OrderCommonService orderCommonService;

    @Resource
    KdApiSearchService kdApiSearchService;

    @Resource
    TokenUtil tokenUtil;

    @ApiOperation(value = "购物车创建订单", notes = "创建订单")
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    public ResponseResultVO createOrder(@RequestBody(required = false) UserCouponOrderRO userCouponOrderRO) {
        return ResponseResultVO.successResult(orderService.createOrder(userCouponOrderRO));
    }

    @ApiOperation(value = "单独购买创建订单", notes = "单独购买创建订单")
    @RequestMapping(value = "/createSingleOrder", method = RequestMethod.POST)
    public ResponseResultVO createSingleOrder(@RequestBody(required = false) UserSingleOrderRO userCouponOrderRO) {
        if(userCouponOrderRO.getNum() <= 0){
            throw new ServiceException("请选择商品数量");
        }
        return ResponseResultVO.successResult(orderService.createSingleOrder(userCouponOrderRO));
    }

    @ApiOperation(value = "通过传过来的id查询订单详情（生成之前）", notes = "通过传过来的id查询订单详情（生成之前） type:0 购物车过来的  1：直接购买的")
    @RequestMapping(value = "/selectOrderInfoCreateBefore", method = RequestMethod.POST)
    public ResponseResultVO selectOrderInfoCreateBefore(@RequestBody(required = false) UserCouponOrderRO userCouponOrderRO) {
        return ResponseResultVO.successResult(orderService.selectOrderInfoCreateBefore(userCouponOrderRO));
    }

    /**
     * @param
     * @return
     */
    @ApiOperation(value = "查询订单", notes = "查询订单")
    @RequestMapping(value = "/selectUserOrderList", method = RequestMethod.POST)
    public ResponseResultVO selectUserOrderList(@RequestBody UserOrderRO userOrderRO) {
        return getPageObject(orderService.selectUserOrderList(userOrderRO), userOrderRO);
    }

    /**
     * @param
     * @return
     */
    @ApiOperation(value = "查询退款订单列表", notes = "查询退款订单列表")
    @RequestMapping(value = "/selectUserRefundOrderList", method = RequestMethod.POST)
    public ResponseResultVO selectUserRefundOrderList(@RequestBody RefundOrderListRO refundOrderListRO) {
        return getPageObject(orderService.selectUserRefundOrderList(refundOrderListRO), refundOrderListRO);
    }

    @ApiOperation(value = "查询订单详情", notes = "查询订单详情")
    @RequestMapping(value = "/selectUserOrderDetail", method = RequestMethod.GET)
    public ResponseResultVO selectUserOrderDetail(@RequestParam String orderId) {
        return ResponseResultVO.successResult(orderService.selectUserOrderDetail(orderId));
    }

    @ApiOperation(value = "取消订单", notes = "取消订单")
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.GET)
    public ResponseResultVO cancelOrder(@RequestParam String orderId) {
        String userId = tokenUtil.getUserIdByToken();
        return ResponseResultVO.successResult(orderCommonService.cancelOrder(orderId, userId));
    }

    @ApiOperation(value = "确认收货", notes = "确认收货")
    @RequestMapping(value = "/confirmReceiptGoods", method = RequestMethod.GET)
    public ResponseResultVO confirmReceiptGoods(@RequestParam String orderId) {
        if (orderCommonService.confirmReceiptGoods(orderId, tokenUtil.getUserIdByToken()) > 0) {
            return ResponseResultVO.successResult("操作成功");
        } else {
            return ResponseResultVO.failResult("操作失败");
        }
    }

    @ApiOperation(value = "查询物流", notes = "查询物流")
    @RequestMapping(value = "/selectOrderExpress", method = RequestMethod.GET)
    public ResponseResultVO selectOrderExpress(@RequestParam String orderId){
        //查询订单的快递信息
        OrderExpressVO orderExpressVO = orderService.selectOrderExpress(orderId);
        if(orderExpressVO == null){
            throw new ServiceException("物流信息不存在");
        }
        if(StrUtil.isBlank(orderExpressVO.getExpressCode()) || StrUtil.isBlank(orderExpressVO.getExpressNumber())){
            throw new ServiceException("物流信息不存在");
        }
        ExpressInfo expressInfo = kdApiSearchService.getExpressInfo(orderExpressVO.getExpressCode(), orderExpressVO.getExpressNumber());
        expressInfo.setExpressName(orderExpressVO.getExpressName());
        return ResponseResultVO.successResult(expressInfo);
    }

    @ApiOperation(value = "申请退款(未发货的)", notes = "申请退款(未发货的)")
    @RequestMapping(value = "/refundOrder", method = RequestMethod.GET)
    public ResponseResultVO refundOrder(@RequestParam String orderId) {
        return ResponseResultVO.successResult(orderService.refundOrder(orderId));
    }

    @ApiOperation(value = "发起退货申请", notes = "发起退货申请")
    @RequestMapping(value = "/refundOrderGoods", method = RequestMethod.POST)
    public ResponseResultVO refundOrderGoods(@Validated @RequestBody RefundGoodsRO refundGoodsRO){
        //发起退货申请
        orderService.refundOrderGoods(refundGoodsRO);
        return ResponseResultVO.successResult("success");
    }

    @ApiOperation(value = "查询退货申请详情", notes = "查询退货申请详情")
    @RequestMapping(value = "/selectRefundOrderGoodsDetail", method = RequestMethod.GET)
    public ResponseResultVO selectRefundOrderGoodsDetail(@RequestParam String refundId){
        //查询退货申请详情
        return ResponseResultVO.successResult(orderService.selectRefundOrderGoodsDetail(refundId));
    }

    @ApiOperation(value = "撤销退货申请", notes = "撤销退货申请")
    @RequestMapping(value = "/cancelRefundOrder", method = RequestMethod.GET)
    public ResponseResultVO cancelRefundOrder(@RequestParam String refundId){
        //查询退货申请详情
        orderService.cancelRefundOrder(refundId);
        return ResponseResultVO.successResult();
    }

}
