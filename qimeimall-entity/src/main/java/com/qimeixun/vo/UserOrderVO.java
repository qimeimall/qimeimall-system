package com.qimeixun.vo;

import com.qimeixun.enums.OrderTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/6/214:15
 */
@Data
public class UserOrderVO {


    private String orderId;

    private String payId;

    private BigDecimal totalMoney;

    private BigDecimal totalPost;

    private BigDecimal storeCouponMoney;

    private BigDecimal platformCouponMoney;

    private BigDecimal payMoney;

    private Integer storeId;

    private String storeName;

    private Long platformCouponId;

    private Date createTime;

    private int status;

    private String nickName;

    private String remark;

    private List<UserProductVO> userProductVOS;

    private String phone;

    private String name;

    private String address;

    private String buyType;

    private int totalNum;

    private String isPoints;

    private String payType;

    private String refundStatus;

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
