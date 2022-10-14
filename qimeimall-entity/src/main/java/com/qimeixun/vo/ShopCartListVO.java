package com.qimeixun.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenshouyang
 * @date 2020/5/2421:04
 */
@Data
public class ShopCartListVO {

    private Integer id;

    private String productName;

    private String productImg;

    private Long categoryId;

    private Long productId;

    private int num;

    private BigDecimal price;

    private boolean selected;

    /**
     * 该商品是否能选择 0 正常可以购买 其余禁止购买  1：已下架  2：库存不够  3：暂无该规格
     */
    private int type;

    private String attrValue;

    private BigDecimal postPrice;

    /**
     * 商品信息json
     */
    private String productInfo;

    private int productAttrId;
}
