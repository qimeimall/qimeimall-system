package com.qimeixun.vo;

import com.qimeixun.enums.OrderTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel
public class UserRefundOrderVO extends UserOrderVO {

    @ApiModelProperty(value = "退款备注")
    private String refundRemark;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundMoney;

    @ApiModelProperty(value = "退款类型")
    private String refundType;

    @ApiModelProperty(value = "退款金额")
    private Date refundCreateTime;

    @ApiModelProperty(value = "商家备注")
    private String systemRemark;

    @ApiModelProperty(value = "退款状态")
    private String userRefundStatus;

    private String refundOrderId;

    @ApiModelProperty(value = "支付时间")
    private Date payTime;

    private String orderType;

    private String orderTypeName;

    public String getOrderTypeName() {
        OrderTypeEnum[] orderTypeEnums = OrderTypeEnum.values();
        for(OrderTypeEnum orderTypeEnum : orderTypeEnums){
            if(orderTypeEnum.getType().equals(orderType)){
                return orderTypeEnum.getRemark();
            }
        }
        return orderTypeName;
    }
}
