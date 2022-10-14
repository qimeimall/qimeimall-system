package com.qimeixun.modules.coupon.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qimeixun.po.UserCouponDTO;
import com.qimeixun.ro.CouponPageRO;
import com.qimeixun.ro.UserCouponListRO;
import com.qimeixun.ro.UserCouponOrderRO;
import com.qimeixun.vo.CouponListVO;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/3015:40
 */
public interface CouponUserService {
    int insertUserCoupon(String couponId);

    List<CouponListVO> selectUserNotHaveCouponList(String storeId);

    List<UserCouponDTO> selectUserCouponByProduct(UserCouponOrderRO userCouponOrderRO);

    List<UserCouponDTO> selectUserCouponList(UserCouponListRO userCouponListRO);
}
