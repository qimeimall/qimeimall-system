package com.qimeixun.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qimeixun.enums.TerminalType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统资源-实体类
 * @author wangdaqiang
 * @date 2019-08-23 18:02
 */
@Data
@ApiModel
@TableName("sys_resource")
public class SysResource implements Serializable {

    private static final long serialVersionUID = 4899337630863718497L;

    /**
     * 资源id
     */
    @ApiModelProperty(value = "资源id", example = "")
    private String id;
    /**
     * 资源类型(0：菜单、1：按钮、2：权限资源)
     */
    @ApiModelProperty(value = "资源类型(0：菜单、1：按钮、2：权限资源)", example = "0")
    private Integer resourceType;
    /**
     * 父级id
     */
    @ApiModelProperty(value = "上级资源id", example = "0")
    private String parentId;
    /**
     * 中文名称
     */
    @ApiModelProperty(value = "中文名称", example = "菜单管理")
    private String nameCn;
    /**
     * 英文名称
     */
    @ApiModelProperty(value = "英文名称", example = "menus")
    private String nameEn;
    /**
     * 资源链接
     */
    @ApiModelProperty(value = "资源链接", example = "/sys/resource/index")
    private String url;
    /**
     * 权限字符串
     */
    @ApiModelProperty(value = "权限字符串", example = "sys:resource:index")
    private String permission;
    /**
     * 排序序号
     */
    @ApiModelProperty(value = "排序序号", example = "100000")
    private Integer sortNo;
    /**
     * 图标
     */
    @ApiModelProperty(value = "图标", example = "el-icon-date")
    private String icon;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;

    /**
     * 终端类型
     */
    @ApiModelProperty(value = "终端类型", example = "WEB")
    @TableField(exist = false)
    private String terminalType;

}
