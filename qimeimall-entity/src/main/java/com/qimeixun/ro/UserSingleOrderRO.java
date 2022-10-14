package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class UserSingleOrderRO {

    private String addressId;

    /**
     * 1 邮寄 2 自提
     */
    private String buyType;

    private String storeId;

    private Integer productId;

    private String productAttrId;

    private String seckillProductId;

    private Long couponId;

    private int num;

    private String remark;

    @ApiModelProperty(value = "0 普通订单  1：积分订单")
    private String productType;

    private List<String> curNav;
}
