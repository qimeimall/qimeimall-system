package com.qimeixun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PointsBillEnum {

    //0 签到积分  1 退积分   2  兑换积分  3 系统增加积分  4 系统扣减积分 5 其它
    TYPE_SIGN("0", "签到积分"),
    TYPE_REFUND("1", "退积分"),
    TYPE_EXCHANGE("2", "积分兑换"),
    TYPE_SYSTEM_ADD("3", "系统增加"),
    TYPE_SYSTEM_SUB("4", "系统减少"),
    TYPE_OTHER("5", "其它");

    private String type;
    private String remark;
}