package com.qimeixun.vo;

import lombok.Data;

/**
 * @author chenshouyang
 * @date 2020/5/1321:23
 */
@Data
public class ProductListVO {
    private Integer id;

    private String storeName;

    private String categoryName;

    private String productName;

    private String productImg;

    private double price;

    private double marketPrice;

    private int salesCount;

    private int stockCount;

    private int status;

    private String isPoints;

    private String isHot;

    private String isRecommend;
}
