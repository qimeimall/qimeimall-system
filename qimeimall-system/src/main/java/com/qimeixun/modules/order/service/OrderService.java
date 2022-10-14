package com.qimeixun.modules.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qimeixun.po.OrderDTO;
import com.qimeixun.ro.*;
import com.qimeixun.vo.ResponseResultVO;
import com.qimeixun.vo.UserOrderVO;

/**
 * @author chenshouyang
 * @date 2020/6/310:53
 */
public interface OrderService {
    IPage<UserOrderVO> selectOrderList(OrderListRO orderListRO);

    OrderDTO selectOrderById(String orderId);

    int deliverGoods(ExpressOrderRO expressOrderRO);

    int userReceiveGoods(String orderId);

    IPage selectRefundOrderList(RefundOrderSystemRO orderListRO);

    void accessRefund(RefundUpdateRO refundUpdateR);

    void refusedRefund(RefundUpdateRO refundUpdateR);

    void refundMoney(String refundId);
}
