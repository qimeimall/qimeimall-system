package com.qimeixun.entity;

import com.qimeixun.vo.SysRoleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 管理员用户实体
 * @author wangdaqiang
 * @date 2019-08-22 17:36
 */
@Data
public class SysUser implements Serializable {

    private static final long serialVersionUID = 3739394131602100595L;

    /**
     * 用户id
     */
    private String id;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 密码
     */
    private String password;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 用户类型(0：管理员、1：巡检员、2：收费员)
     */
    private Integer userType;
    /**
     * 是否超级管理员(0：不是、1：是)
     */
    private Integer isSuper;
    /**
     * 登录token(记住密码)
     */
    private String rememberToken;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;
    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
    /**
     * 状态(0：正常)
     */
    private Integer status;
    /**
     * 手持机是否在线(0：不在线、1：在线)
     */
    private Integer isOnline;
    /**
     * 编号
     */
    private String cardNumber;

    @ApiModelProperty(value = "角色集合")
    private List<SysRoleVO> roleList = new ArrayList<>();

    private String storeId;


}
