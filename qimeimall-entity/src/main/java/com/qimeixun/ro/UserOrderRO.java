package com.qimeixun.ro;

import lombok.Data;

@Data
public class UserOrderRO extends PageRO {

    private String type; // 0:未付款  1:未发货 2：待收货  3：已收货  4：待评价 5：已评价  6：待退款  7：已退款

    private String userId;

    private String orderId;
}
