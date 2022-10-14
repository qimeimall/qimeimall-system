package com.qimeixun.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/5/309:33
 */
@Data
public class CouponListVO {

    private Integer id;

    private String couponName;

    private String storeName;

    private double minConsume;

    private double couponMoney;

    private int effectDays;

    private Date effectTime;

    private int totalCount;

    private int surplusCount;

    private String status;

    private String sort;

    private String effectType;

    private String effectProductId;

    private Integer storeId;

    private int type;
}
