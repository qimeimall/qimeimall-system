package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class UserCouponListRO extends PageRO{

    private String userId;

    @ApiModelProperty(value = "0: 未使用， 1：已使用 2：已失效")
    private String type;
}
