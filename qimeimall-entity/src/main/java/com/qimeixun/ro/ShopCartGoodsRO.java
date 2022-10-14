package com.qimeixun.ro;

import lombok.Data;

/**
 * @author chenshouyang
 * @date 2020/5/2418:24
 */
@Data
public class ShopCartGoodsRO {

    private ProductRO product;

    private ProductAttrRO productAttr;

    /**
     * 秒杀id
     */
    private String seckillProductId;
}
