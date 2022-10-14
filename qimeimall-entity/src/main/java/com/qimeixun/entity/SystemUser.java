package com.qimeixun.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/4/2613:48
 */
@Data
@TableName("tb_system_user")
public class SystemUser {
    private Integer id;
    private String username;
    private String password;
    private String phone;
    private String nickname;
    private Date lastLoginTime;
    private String type;
    private Date createTime;
    private Date udpateTime;
    private Integer isSuper;
}
