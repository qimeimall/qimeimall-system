package com.qimeixun.po;

import com.qimeixun.po.ShopCartDTO;
import com.qimeixun.vo.ShopCartListVO;
import lombok.Data;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/2520:46
 */
@Data
public class CartOrderDTO {

    private String storeId;

    private String storeName;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 备注
     */
    private String remark;

    private List<ShopCartListVO> shopCartListVOS;
}
