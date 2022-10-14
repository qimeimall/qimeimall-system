package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色资源-请求参数模型
 * @author wangdaqiang
 * @date 2019-08-26 11:53
 */
@Data
@ApiModel
public class SysRoleResourceRO implements Serializable {

    private static final long serialVersionUID = 6549679213338316155L;


    @ApiModelProperty(value = "角色id", example = "")
    private String roleId;

    @ApiModelProperty(value = "资源id集合(所有选中的资源的id集合)", example = "")
    private List<String> resourceIds;


}
