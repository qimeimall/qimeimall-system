package com.qimeixun.ro;

import lombok.Data;

@Data
public class PayRO {

    private String orderId;

    private String couponId;

    private String payType;

    private String code;

    private String application;

    private String rechargeMoney;
}
