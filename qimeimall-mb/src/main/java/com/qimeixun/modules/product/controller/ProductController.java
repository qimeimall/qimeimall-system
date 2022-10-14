package com.qimeixun.modules.product.controller;

import com.qimeixun.annotation.JwtIgnore;
import com.qimeixun.modules.product.Service.ProductService;
import com.qimeixun.ro.ProductDetailRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

/**
 * @author chenshouyang
 * @date 2020/5/2311:32
 */
@Api(tags = "产品")
@RestController
@RequestMapping(value = "/product", produces = "application/json;charset=UTF-8")
public class ProductController {

    @Resource
    ProductService productService;

    @ApiOperation(value = "通过id获取产品信息", notes = "通过id获取产品信息")
    @RequestMapping(value = "/selectProductInfoById", method = RequestMethod.POST)
    @JwtIgnore
    public ResponseResultVO selectProductInfoById(@RequestBody ProductDetailRO productDetailRO) {
        return ResponseResultVO.successResult(productService.selectProductInfoById(productDetailRO));
    }

    @ApiOperation(value = "查询热门商品", notes = "查询热门商品")
    @RequestMapping(value = "/selectProductHot", method = RequestMethod.GET)
    @JwtIgnore
    public ResponseResultVO selectProductHot(@RequestParam String storeId) {
        return ResponseResultVO.successResult(productService.selectProductHot(storeId));
    }

    @ApiOperation(value = "查询推荐商品", notes = "查询推荐商品")
    @RequestMapping(value = "/selectProductRecommend", method = RequestMethod.GET)
    @JwtIgnore
    public ResponseResultVO selectProductRecommend(@RequestParam(required = false, defaultValue = "0") String storeId) {
        return ResponseResultVO.successResult(productService.selectProductRecommend(storeId));
    }

    @ApiOperation(value = "查询收藏商品", notes = "查询收藏商品")
    @RequestMapping(value = "/selectProductCollection", method = RequestMethod.GET)
    public ResponseResultVO selectProductCollection() {
        return ResponseResultVO.successResult(productService.selectProductCollection());
    }

    @ApiOperation(value = "收藏产品", notes = "收藏产品")
    @RequestMapping(value = "/createCollectionProduct", method = RequestMethod.GET)
    public ResponseResultVO collectionProduct(@RequestParam String productId) {
        return ResponseResultVO.successResult(productService.collectionProduct(productId));
    }

}
