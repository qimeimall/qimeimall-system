package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class RefundUpdateRO {

    @ApiModelProperty(value = "退款id")
    private String refundId;

    @ApiModelProperty(value = "备注")
    private String remark;
}
