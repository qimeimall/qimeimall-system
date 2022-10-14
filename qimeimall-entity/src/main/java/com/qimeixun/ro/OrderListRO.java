package com.qimeixun.ro;

import lombok.Data;

/**
 * @author chenshouyang
 * @date 2020/6/310:57
 */
@Data
public class OrderListRO extends PageRO {
    private String type;

    /**
     * 收件人名称
     */
    private String name;

    /**
     * 收件人电话
     */
    private String phone;

    /**
     * 用户名
     */
    private String nickName;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 订单号
     */
    private String orderId;
}
