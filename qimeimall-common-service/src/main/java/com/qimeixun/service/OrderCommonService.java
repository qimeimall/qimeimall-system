package com.qimeixun.service;

import com.qimeixun.vo.OrderProductListVO;

import java.util.List;

public interface OrderCommonService {

    int cancelOrder(String orderId, String userId);

    void addOrSubStock(List<OrderProductListVO> orderProductListVOS, String type);

    int confirmReceiptGoods(String orderId, String userId);

    void setCouponIsUsed(Long couponId);
}
