package com.qimeixun.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel
public class RetailRecordVO {

    private String id;

    private String userName;

    private String buyUserName;

    private BigDecimal consumerMoney;

    private BigDecimal commissionMoney;

    private Date createTime;
}
