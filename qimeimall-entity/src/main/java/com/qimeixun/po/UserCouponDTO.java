package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/5/3018:26
 */
@Data
@TableName("tb_coupon_user")
public class UserCouponDTO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long couponId;
    private Long userId;
    private String couponName;
    private BigDecimal minConsume;
    private BigDecimal couponMoney;
    private Date effectTime;
    private String type;
    private String effectProductId;
    private Integer isUsed;
    private Date createTime;
    private Integer storeId;
    private String isPlatform;
    @TableField(exist = false)
    private BigDecimal orderCouponMoney;

    @TableField(exist = false)
    private String isEffect;

}
