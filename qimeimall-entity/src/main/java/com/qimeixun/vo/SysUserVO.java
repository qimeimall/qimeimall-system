package com.qimeixun.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理员用户-响应数据模型
 * @author wangdaqiang
 * @date 2019-08-22 17:36
 */
@Data
@ApiModel
public class SysUserVO implements Serializable {

    private static final long serialVersionUID = 3739394131602100595L;


    @ApiModelProperty(value = "id", example = "id")
    private String id;

    @ApiModelProperty(value = "登录名", example = "登录名")
    private String loginName;

    @ApiModelProperty(value = "姓名", example = "姓名")
    private String userName;

    @ApiModelProperty(value = "手机号", example = "手机号")
    private String mobile;

    @ApiModelProperty(value = "用户类型(0：管理员、1：巡检员、2：收费员)", example = "0")
    private Integer userType;

    @ApiModelProperty(value = "是否超级管理员(0：不是、1：是)", example = "0")
    private Integer isSuper;

    @ApiModelProperty(value = "创建时间", example = "2019-08-26 19:49:50")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间", example = "2019-08-26 19:49:50")
    private Date updatedAt;

    @ApiModelProperty(value = "最后登录时间", example = "2019-08-26 19:49:50")
    private Date lastLoginTime;

    @ApiModelProperty(value = "状态(0：正常)", example = "0")
    private Integer status;

    @ApiModelProperty(value = "手持机是否在线(0：不在线、1：在线)", example = "0")
    private Integer isOnline;

    @ApiModelProperty(value = "编号", example = "000001")
    private String cardNumber;


}
