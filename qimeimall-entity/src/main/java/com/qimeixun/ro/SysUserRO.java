package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 管理员用户-请求数据模型
 * @author wangdaqiang
 * @date 2019-08-22
 */
@Data
@ApiModel
public class SysUserRO extends PageRO {

    private static final long serialVersionUID = 295144720826078061L;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id(新增不填)", example = "新增不填")
    private String id;
    /**
     * 登录名
     */
    @ApiModelProperty(value = "登录名", example = "admin")
    private String loginName;
    /**
     * 密码
     */
    @ApiModelProperty(value = "登录密码", example = "123456")
    private String password;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", example = "管理员")
    private String userName;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", example = "13668221636")
    private String mobile;
    /**
     * 用户类型(0：管理员、1：巡检员、2：收费员)
     */
    @ApiModelProperty(value = "用户类型(0：管理员、1：巡检员、2：收费员)", example = "0")
    private Integer userType;
    /**
     * 编号
     */
    @ApiModelProperty(value = "用户编号", example = "000001")
    private String cardNumber;

    @ApiModelProperty(value = "门店id", example = "000001")
    private String storeId;

    @ApiModelProperty(value = "角色id集合")
    private List<String> roleIds;


}
