package com.qimeixun.modules.cart.controller;

import com.qimeixun.annotation.LoginCheck;
import com.qimeixun.constant.SystemConstant;
import com.qimeixun.modules.cart.service.ShopCartService;
import com.qimeixun.ro.ShopCartGoodsRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

/**
 * @author chenshouyang
 * @date 2020/5/2413:33
 */
@Api(tags = "购物车")
@RestController
@RequestMapping(value = "/cart", produces = "application/json;charset=UTF-8")
public class ShopCartController {

    @Resource
    ShopCartService shopCartService;

    @ApiOperation(value = "查询购物车列表", notes = "查询购物车列表")
    @RequestMapping(value = "/selectShopCartList", method = RequestMethod.GET)
    public ResponseResultVO selectShopCartList() {
        return ResponseResultVO.successResult(shopCartService.selectShopCartList());
    }

    @ApiOperation(value = "查询用户购物车数量", notes = "查询用户购物车数量")
    @RequestMapping(value = "/selectGoodsCartCount", method = RequestMethod.GET)
    public ResponseResultVO selectGoodsCartCount() {
        return ResponseResultVO.successResult(shopCartService.selectGoodsCartCount());
    }

    @ApiOperation(value = "购物车新增商品", notes = "购物车新增商品")
    @RequestMapping(value = "/insertShopCart", method = RequestMethod.POST)
    public ResponseResultVO insertShopCart(@RequestBody ShopCartGoodsRO shopCartGoodsRO) {
        if (shopCartService.insertShopCart(shopCartGoodsRO) > 0) {
            return ResponseResultVO.successResult();
        } else {
            return ResponseResultVO.failResult();
        }
    }

    @ApiOperation(value = "删除购物车中产品", notes = "删除购物车中产品")
    @RequestMapping(value = "/deleteShopCartProduct", method = RequestMethod.GET)
    public ResponseResultVO deleteShopCartProduct(@RequestParam String id) {
        shopCartService.deleteShopCartProduct(id);
        return ResponseResultVO.successResult();
    }

    @ApiOperation(value = "购物车中产品增加", notes = "购物车中产品增加")
    @RequestMapping(value = "/addShopCartProductNum", method = RequestMethod.GET)
    public ResponseResultVO addShopCartProductNum(@RequestParam String id) {
        shopCartService.addShopCartProductNum(id);
        return ResponseResultVO.successResult();
    }

    @ApiOperation(value = "购物车中产品减少", notes = "购物车中产品减少")
    @RequestMapping(value = "/subShopCartProductNum", method = RequestMethod.GET)
    public ResponseResultVO subShopCartProductNum(@RequestParam String id) {
        shopCartService.subShopCartProductNum(id);
        return ResponseResultVO.successResult();
    }
}
