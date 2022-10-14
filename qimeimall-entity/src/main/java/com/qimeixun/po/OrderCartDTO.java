package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_order_cart")
public class OrderCartDTO {

    private Long id;
    private Long orderId;
    private Long cartId;
    private Date createTime;

}
