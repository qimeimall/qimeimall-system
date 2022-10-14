package com.qimeixun.modules.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qimeixun.po.OrderDTO;
import com.qimeixun.po.OrderPayDTO;
import com.qimeixun.ro.*;
import com.qimeixun.vo.OrderExpressVO;
import com.qimeixun.vo.ResponseResultVO;
import com.qimeixun.vo.UserOrderVO;

import java.util.List;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/5/2520:27
 */
public interface OrderService {
    Map<String, Object> selectOrderInfoCreateBefore(UserCouponOrderRO userCouponOrderRO);

    OrderDTO createOrder(UserCouponOrderRO userCouponOrderRO);

    IPage selectUserOrderList(UserOrderRO userOrderRO);

    UserOrderVO selectUserOrderDetail(String orderId);

    void updateOrderReplyStatus(String productId, String orderId);

    int refundOrder(String orderId);

    IPage selectUserRefundOrderList(RefundOrderListRO refundOrderListRO);

    OrderDTO createSingleOrder(UserSingleOrderRO userCouponOrderRO);

    String selectOrderAttrValue(String orderId);

    OrderExpressVO selectOrderExpress(String orderId);

    void refundOrderGoods(RefundGoodsRO refundGoodsRO);

    Map selectRefundOrderGoodsDetail(String refundId);

    void cancelRefundOrder(String refundId);
}
