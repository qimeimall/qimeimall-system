package com.qimeixun.ro;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/2921:29
 */
@Data
public class CouponRO {

    private Long id;

    /**
     * 优惠券名称
     */
    @NotNull(message = "请输入优惠券名")
    private String couponName;

    /**
     * 最低消费金额
     */
    @NotNull(message = "请输入最低消费金额")
    private BigDecimal minConsume;

    /**
     * 优惠金额
     */
    @NotNull(message = "请输入优惠金额")
    private BigDecimal couponMoney;

    /**
     * 有效天数
     */
    private Integer effectDays;

    /**
     * 有效时间
     */
    private Date effectTime;

    /**
     * 有效类型
     */
    @NotNull(message = "请输入优惠类型")
    private String effectType;

    /**
     * 总数量
     */
    @NotNull(message = "请输入优惠券总数量")
    private Integer totalCount;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 类型 0 商品类型   1：具体商品 2：不限制
     */
    @NotNull(message = "请输入优惠券名")
    private String type;

    /**
     * 0 展示  1：不展示
     */
    private Integer status;

    /**
     * 优惠的产品类型组
     */
    private List<Integer> productClass;

    /**
     * 具体优惠的产品
     */
    private List<Integer> product;

    /**
     * 使用说明
     */
    private String applyExplain;
}
