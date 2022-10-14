package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SysReplayRO extends PageRO{

    @ApiModelProperty(value = "1未回复  1已回复")
    private String type;
}
