package com.qimeixun.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BrokerageVO {

    private String id;

    /**
     * 购买人
     */
    private String buyUserName;

    /**
     * 消费金额
     */
    private BigDecimal consumerMoney;

    /**
     * 佣金金额
     */
    private BigDecimal commissionMoney;

    /**
     * 创建时间
     */
    private Date createTime;
}
