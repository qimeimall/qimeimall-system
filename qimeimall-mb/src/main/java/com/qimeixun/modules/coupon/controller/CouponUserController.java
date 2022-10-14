package com.qimeixun.modules.coupon.controller;

import cn.hutool.core.util.StrUtil;
import com.qimeixun.annotation.JwtIgnore;
import com.qimeixun.base.BaseController;
import com.qimeixun.modules.coupon.service.CouponUserService;
import com.qimeixun.ro.UserCouponListRO;
import com.qimeixun.ro.UserCouponOrderRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/5/3015:39
 */
@Api(tags = "用户优惠券")
@RestController
@RequestMapping(value = "/coupon", produces = "application/json;charset=UTF-8")
public class CouponUserController extends BaseController {

    @Resource
    CouponUserService couponUserService;

    @ApiOperation(value = "用户领取优惠券", notes = "用户领取优惠券")
    @RequestMapping(value = "/insertUserCoupon", method = RequestMethod.GET)
    public ResponseResultVO insertUserCoupon(@RequestParam String couponId) {
        if (StrUtil.isBlank(couponId)) {
            return ResponseResultVO.failResult("优惠券id不能为空");
        }
        if (couponUserService.insertUserCoupon(couponId) > 0) {
            return ResponseResultVO.successResult("领取成功");
        } else {
            return ResponseResultVO.failResult("领取失败");
        }
    }

    @ApiOperation(value = "用户未领取优惠中心列表", notes = "用户未领取优惠中心列表")
    @RequestMapping(value = "/selectUserNotHaveCouponList", method = RequestMethod.GET)
    @JwtIgnore
    public ResponseResultVO selectUserNotHaveCouponList(@RequestParam(required = false) String storeId) {
        return ResponseResultVO.successResult(couponUserService.selectUserNotHaveCouponList(storeId));
    }

    @ApiOperation(value = "通过用户选择的产品查询符合使用的优惠券", notes = "通过用户选择的产品查询符合使用的优惠券")
    @RequestMapping(value = "/selectUserCouponByProduct", method = RequestMethod.POST)
    public ResponseResultVO selectUserCouponByProduct(@RequestBody UserCouponOrderRO userCouponOrderRO) {
        return ResponseResultVO.successResult(couponUserService.selectUserCouponByProduct(userCouponOrderRO));
    }

    @ApiOperation(value = "查询用户的优惠券列表", notes = "查询用户的优惠券列表")
    @RequestMapping(value = "/selectUserCouponList", method = RequestMethod.POST)
    public ResponseResultVO selectUserCouponList(@RequestBody UserCouponListRO userCouponListRO) {

        return ResponseResultVO.successResult(couponUserService.selectUserCouponList(userCouponListRO));
    }
}
