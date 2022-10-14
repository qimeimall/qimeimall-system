package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenshouyang
 * @date 2022/9/322:55
 */
@Data
@ApiModel
public class MySpreadRO extends PageRO{

    @ApiModelProperty(value = "1 一级代理  2 二级代理")
    private String type;
}
