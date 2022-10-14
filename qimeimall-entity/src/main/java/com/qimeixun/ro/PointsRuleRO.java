package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class PointsRuleRO {

    private Long id;
    @ApiModelProperty(value = "第几天")
    private String daysKey;
    @ApiModelProperty(value = "获得积分")
    private BigDecimal points;
    @ApiModelProperty(value = "排序")
    private Integer sortNo;
    @ApiModelProperty(value = "是否显示")
    private String isShow;

}
