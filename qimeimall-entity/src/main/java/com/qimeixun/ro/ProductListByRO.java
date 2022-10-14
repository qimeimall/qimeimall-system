package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenshouyang
 * @date 2020/5/2212:54
 */
@Data
@ApiModel
public class ProductListByRO {

    @ApiModelProperty(value = "分类id")
    private String categoryId;

    private String sortType;

    private String storeId;

    private String productName;

    @ApiModelProperty(value = "0 普通商品  1 积分商品")
    private String productType;
}
