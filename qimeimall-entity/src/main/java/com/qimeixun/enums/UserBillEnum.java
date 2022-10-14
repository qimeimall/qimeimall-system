package com.qimeixun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserBillEnum {

    //0 用户购买  1 退款   2充值   3 系统充值
    TYPE_BUT("0", "用户购买"),
    TYPE_REFUND("1", "退款"),
    TYPE_EXCHANGE("2", "2充值"),
    TYPE_SYSTEM_ADD("3", "系统充值"),
    TYPE_SYSTEM_SUB("4", "系统减少"),
    TYPE_OTHER("5", "其它");

    private String type;
    private String remark;
}
