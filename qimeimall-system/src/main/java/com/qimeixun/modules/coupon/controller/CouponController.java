package com.qimeixun.modules.coupon.controller;

import com.qimeixun.base.BaseController;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.modules.coupon.service.CouponService;
import com.qimeixun.ro.CouponPageRO;
import com.qimeixun.ro.CouponRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenshouyang
 * @date 2020/5/2921:26
 */
@Api(tags = "优惠券管理")
@RestController
@RequestMapping(value = "/coupon", produces = "application/json;charset=UTF-8")
public class CouponController extends BaseController {

    @Resource
    CouponService couponService;

    @ApiOperation(value = "新增优惠券", notes = "新增优惠券")
    @RequestMapping(value = "/insertCoupon", method = RequestMethod.POST)
    public ResponseResultVO insertCoupon(@Validated @RequestBody CouponRO couponRO) {
        if("0".equals(couponRO.getEffectType()) && CollectionUtils.isEmpty(couponRO.getProduct())){
            throw new ServiceException("请选择优惠商品");
        }
        if("1".equals(couponRO.getEffectType())){
            throw new ServiceException("请选择优惠商品类型");
        }
        return insertResult(couponService.insertCoupon(couponRO));
    }

    @ApiOperation(value = "查询优惠券", notes = "查询优惠券")
    @RequestMapping(value = "/selectCouponList", method = RequestMethod.POST)
    public ResponseResultVO selectCouponList(@RequestBody CouponPageRO couponPageRO) {
        return getPageObject(couponService.selectCouponList(couponPageRO), couponPageRO);
    }

    @ApiOperation(value = "优惠券详情", notes = "优惠券详情")
    @RequestMapping(value = "/selectCouponById", method = RequestMethod.GET)
    public ResponseResultVO selectCouponById(@RequestParam String id) {
        return ResponseResultVO.successResult(couponService.selectCouponById(id));
    }

    @ApiOperation(value = "删除优惠券", notes = "删除优惠券")
    @RequestMapping(value = "/deleteCouponById", method = RequestMethod.GET)
    public ResponseResultVO deleteCouponById(@RequestParam String id) {
        return deleteResult(couponService.removeById(id));
    }

    @ApiOperation(value = "修改优惠券", notes = "修改优惠券")
    @RequestMapping(value = "/updateCoupon", method = RequestMethod.POST)
    public ResponseResultVO updateCoupon(@RequestBody CouponRO couponRO) {
        return updateResult(couponService.updateCoupon(couponRO));
    }

}
