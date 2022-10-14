package com.qimeixun.ro;

import com.qimeixun.po.CartOrderDTO;
import lombok.Data;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/3114:13
 */
@Data
public class UserCouponOrderRO {

    private String type;

    /**
     * 购物车ids
     */
    private List<String> ids;

    List<CartOrderDTO> cartOrderDTOS;

    /**
     * 平台优惠券id
     */
    private Long couponId;

    /**
     * 是否开启积分抵扣
     */
    private boolean isPoints;

    private String addressId;

    private String storeId;

    /**
     * 1 邮寄 2 自提
     */
    private String buyType;

    private String remark;

}
