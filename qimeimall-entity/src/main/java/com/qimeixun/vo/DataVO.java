package com.qimeixun.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DataVO {

    private Integer customerCount;
    private Integer todayCustomerCount;

    private Integer allOrderCount;
    private Integer todayOrderCount;

    private BigDecimal orderMoney;
    private BigDecimal todayOrderMoney;

    private Integer allProductCount;
    private Integer todayProductCount;
}
