package com.qimeixun.modules.product.controller;

import com.qimeixun.annotation.JwtIgnore;
import com.qimeixun.modules.product.Service.ProductListService;
import com.qimeixun.ro.ProductListByRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenshouyang
 * @date 2020/5/229:53
 */
@Api(tags = "产品分类")
@RestController
@RequestMapping(value = "/productClass", produces = "application/json;charset=UTF-8")
public class ProductListController {

    @Resource
    ProductListService productListService;

    @ApiOperation(value = "获取产品分类列表", notes = "获取产品分类列表")
    @RequestMapping(value = "/productClassList", method = RequestMethod.GET)
    @JwtIgnore
    public ResponseResultVO porductClassList(@RequestParam(required = false,defaultValue = "0") String storeId){
        return productListService.selectProductList(storeId);
    }

    @ApiOperation(value = "通过分类id或者产品名称查询商品列表", notes = "通过分类id或者产品名称查询商品列表")
    @RequestMapping(value = "/selectProductListById", method = RequestMethod.POST)
    @JwtIgnore
    public ResponseResultVO selectProductListById(@RequestBody ProductListByRO productListByRO){
            return productListService.selectProductListById(productListByRO);
    }

}
