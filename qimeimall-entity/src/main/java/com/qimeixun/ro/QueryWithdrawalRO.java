package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class QueryWithdrawalRO extends PageRO{

    @ApiModelProperty(value = "0：待审核  1：已审核 2：审核拒绝")
    private String type;

    private String nickName;
}
