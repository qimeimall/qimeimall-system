package com.qimeixun.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单-返回数据模型
 * @author wangdaqiang
 * @date 2019-08-26 15:38
 */
@Data
@ApiModel
public class MenuVO implements Serializable {

    private static final long serialVersionUID = -1767984349510470684L;

    @ApiModelProperty(value = "菜单id")
    private String id;

    @ApiModelProperty(value = "资源类型(0：菜单、1：按钮、2：权限资源)")
    private Integer resourceType;

    @ApiModelProperty(value = "中文名")
    private String nameCn;

    @ApiModelProperty(value = "英文名")
    private String nameEn;

    @ApiModelProperty(value = "上级菜单id")
    private String parentId;

    @ApiModelProperty(value = "链接")
    private String url;

    @ApiModelProperty(value = "权限字符串")
    private String permission;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "标识角色是否有此资源权限(0：无权限、1：有权限)")
    private Integer isPermission;

    @ApiModelProperty(value = "菜单下的子菜单集合")
    private List<MenuVO> children = new ArrayList<>();
}
