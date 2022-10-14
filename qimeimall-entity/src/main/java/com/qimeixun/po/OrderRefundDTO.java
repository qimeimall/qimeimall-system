package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_order_refund")
public class OrderRefundDTO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderId;
    private BigDecimal payMoney;
    private BigDecimal refundMoney;
    private String status;
    private String remark;
    private String type;
    private String systemRemark;
    private Date createTime;

}
