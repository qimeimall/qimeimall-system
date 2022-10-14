package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理员角色-请求参数模型
 * @author wangdaqiang
 * @date 2019-08-26 10:45
 */
@Data
@ApiModel
public class SysUserRoleRO implements Serializable {

    private static final long serialVersionUID = 6170720742447010881L;


    @ApiModelProperty(value = "用户id", example = "")
    private String userId;

    @ApiModelProperty(value = "角色id", example = "")
    private String roleId;

    @ApiModelProperty(value = "操作类型(0：添加、1：删除)，默认0", example = "0：添加、1：删除")
    private Integer opType;



}
