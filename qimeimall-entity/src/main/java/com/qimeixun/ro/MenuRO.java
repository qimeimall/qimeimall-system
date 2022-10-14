package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜单请求参数
 * @author wangdaqiang
 * @date 2019-09-30 14:24
 */
@Data
@ApiModel
public class MenuRO implements Serializable {
    private static final long serialVersionUID = 1661796239006873066L;

    @ApiModelProperty(value = "菜单id", example = "菜单id")
    private String menuId;

    @ApiModelProperty(value = "菜单路径", example = "/sys/user/selectList")
    private String menuUrl;

}
