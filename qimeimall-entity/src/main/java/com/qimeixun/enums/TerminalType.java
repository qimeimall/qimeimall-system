package com.qimeixun.enums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 终端类型-枚举类
 * @author wangdaqiang
 * @date 2019-08-26 15:03
 */
@ApiModel
public enum TerminalType {

    /**
     * 后台web端
     */
    @ApiModelProperty(value = "后台web端")
    WEB,
    /**
     * APP用户端
     */
    @ApiModelProperty(value = "APP用户端")
    APP,
    /**
     * PDA手持机端
     */
    @ApiModelProperty(value = "PDA手持机端")
    PDA;
}
