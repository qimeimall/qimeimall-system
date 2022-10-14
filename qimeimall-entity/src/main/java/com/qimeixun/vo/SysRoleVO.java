package com.qimeixun.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统角色-返回数据模型
 * @author wangdaqiang
 * @date 2019-08-26 11:31
 */
@Data
@ApiModel
public class SysRoleVO implements Serializable {

    private static final long serialVersionUID = -1760765822851966681L;


    @ApiModelProperty(value = "角色id", example = "id")
    private String id;

    @ApiModelProperty(value = "角色名称", example = "roleName")
    private String roleName;

    @ApiModelProperty(value = "描述", example = "description")
    private String description;


}
