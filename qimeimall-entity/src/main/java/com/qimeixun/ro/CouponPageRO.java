package com.qimeixun.ro;

import lombok.Data;

/**
 * @author chenshouyang
 * @date 2020/5/309:28
 */
@Data
public class CouponPageRO extends PageRO {

    private String couponName;

    private String storeId;

    private String userId;
}
