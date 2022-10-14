package com.qimeixun.modules.coupon.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qimeixun.po.CouponDTO;
import com.qimeixun.ro.CouponPageRO;
import com.qimeixun.ro.CouponRO;
import com.qimeixun.vo.CouponListVO;
import com.qimeixun.vo.ResponseResultVO;

/**
 * @author chenshouyang
 * @date 2020/5/2921:27
 */
public interface CouponService extends IService<CouponDTO> {
    int insertCoupon(CouponRO couponRO);

    IPage<CouponListVO> selectCouponList(CouponPageRO couponPageRO);

    CouponDTO selectCouponById(String id);

    int updateCoupon(CouponRO couponRO);
}
