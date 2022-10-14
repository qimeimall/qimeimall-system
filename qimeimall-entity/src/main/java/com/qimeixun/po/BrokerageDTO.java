package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("tb_user_brokerage")
@Data
public class BrokerageDTO {

    @TableId(type = IdType.AUTO)
    private String  id;
    private String  orderId;
    private Long  userId;
     private Long  buyUserId;
     private BigDecimal consumerMoney;
     private BigDecimal  commissionMoney;
     private Date createTime;
    private String  status;

}
