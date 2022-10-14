package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/5/2713:21
 */
@Data
@TableName("tb_order_pay")
public class OrderPayDTO {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String payId;
    private BigDecimal totalMoney;
    private String isPay;
    private String payType;
    private Date createTime;
    private Long platformCouponId;
    private Date payTime;
    private Long userId;
    private String application; // 0 订单支付  充值
    private String orderId;

}
