package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统资源请求参数模型
 * @author wangdaqiang
 * @date 2019-09-02 17:35
 */
@Data
@ApiModel
public class SysResourceRO implements Serializable {

    private static final long serialVersionUID = 1703786216310606040L;


    @ApiModelProperty(value = "id", example = "")
    private String id;

    @ApiModelProperty(value = "资源类型(0：菜单、1：按钮、2：权限资源)", example = "0")
    private Integer resourceType;

    @ApiModelProperty(value = "上级资源id", example = "")
    private String parentId;

    @ApiModelProperty(value = "中文名称", example = "")
    private String nameCn;

    @ApiModelProperty(value = "英文名称", example = "")
    private String nameEn;

    @ApiModelProperty(value = "权限字符串", example = "")
    private String permission;

    @ApiModelProperty(value = "角色id(若指定，则返回数据中会标识出该角色所拥有的资源)", example = "")
    private String roleId;


}
