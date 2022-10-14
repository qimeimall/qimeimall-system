package com.qimeixun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderTypeEnum {

    //0 普通订单； 1：积分订单  2：秒杀订单 3：拼团订单  4：砍价订单
    ORDER_TYPE_NORMAL("0", "普通订单"),
    ORDER_TYPE_POINTS("1", "积分订单"),
    ORDER_TYPE_SECKILL("2", "秒杀订单"),
    ORDER_TYPE_PT("3", "拼团订单"),
    ORDER_TYPE_BARGAIN("4", "砍价订单"),
    TYPE_OTHER("5", "其它");

    private String type;
    private String remark;
}
