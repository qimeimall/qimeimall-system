package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 系统角色-请求参数模型
 * @author wangdaqiang
 * @date 2019-08-24 15:34
 */
@ApiModel
public class SysRoleRO extends PageRO {

    private static final long serialVersionUID = -3771894404498592797L;


    @ApiModelProperty(value = "角色id", example = "")
    private String id;

    @ApiModelProperty(value = "角色名称", example = "管理员")
    private String roleName;

    @ApiModelProperty(value = "角色描述", example = "系统管理员权限")
    private String description;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
