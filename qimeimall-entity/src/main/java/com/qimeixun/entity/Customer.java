package com.qimeixun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/5/517:12
 */
@Data
@TableName("tb_user")
public class Customer {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String nickName;
    private String headImg;
    private String openId;
    private String phone;
    private Date registerTime;
    private Date lastLoginTime;
    private BigDecimal points;
    private BigDecimal balance;
    private String status;
    private String fromType;
    private Date createTime;
    private String sex;
    private String password;
    private Integer levelId;
    private Long referrerId;
    private String referrerCode;
    private Date levelStartTime;
    private Date levelEndTime;

    private Integer signCount;

}
