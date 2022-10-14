package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/2923:01
 */
@Data
@TableName("tb_coupon")
public class CouponDTO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String couponName;
    private BigDecimal minConsume;
    private BigDecimal couponMoney;
    private Integer effectDays;
    private Date effectTime;
    private Integer totalCount;
    private Integer surplusCount;
    private String status;
    private String storeId;
    private String applyExplain;
    private Integer sort;
    private String type;
    private String effectType;
    private Date createTime;

    private String effectProductId;

    /**
     * 优惠的产品类型组
     */
    @TableField(exist = false)
    private List<Integer> productClass;

    /**
     * 具体优惠的产品
     */
    @TableField(exist = false)
    private List<Integer> product;

}
