package com.qimeixun.ro;

import lombok.Data;

/**
 * @author chenshouyang
 * @date 2020/7/318:11
 */
@Data
public class ProductDetailRO {

    /**
     * 产品id
     */
    private String productId;

    /**
     * 秒杀产品id
     */
    private String seckillProductId;
}
