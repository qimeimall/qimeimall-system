package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/5/2712:11
 */
@Data
@TableName("tb_order")
public class OrderDTO {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String orderId;
    private String payId;
    private Long userId;
    private BigDecimal totalMoney;
    private Integer totalNum;
    private BigDecimal totalPost;
    private BigDecimal payMoney;
    private BigDecimal payPost;
    private String isPay;
    private String payType;
    private String status;
    private Date createTime;
    private String phone;
    private String name;
    private String address;
    private String storeId;
    private BigDecimal storeCouponMoney;
    private BigDecimal platformCouponMoney;
    private Long couponId;
    private String remark;
    private String isDelete;
    private String isPoints;

    private String buyType;

    private String refundStatus;

    private BigDecimal refundMoney;

    private String orderType;

}
