package com.qimeixun.modules.product.controller;

import com.qimeixun.annotation.LoginCheck;
import com.qimeixun.base.BaseController;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.modules.product.service.ProductService;
import com.qimeixun.ro.ProductListRO;
import com.qimeixun.ro.ProductRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/5/1320:14
 */
@Api(tags = "产品管理")
@RestController
@RequestMapping(value = "/product", produces = "application/json;charset=UTF-8")
public class ProductController extends BaseController {

    @Resource
    ProductService productService;

    @ApiOperation(value = "查询产品列表", notes = "查询素材组列表")
    @RequestMapping(value = "/selectProductList", method = RequestMethod.POST)
    //@LoginCheck
    public ResponseResultVO selectProductList(@RequestBody ProductListRO productListRO) {
        return getPageObject(productService.selectProductList(productListRO), productListRO);
    }

    @ApiOperation(value = "添加产品", notes = "添加产品")
    @RequestMapping(value = "/insertProduct", method = RequestMethod.POST)
    //@LoginCheck
    public ResponseResultVO insertProduct(@Validated @RequestBody ProductRO productRO) {
        if(CollectionUtils.isEmpty(productRO.getSpecs())){
            throw new ServiceException("请填写规格组");
        }
        if(CollectionUtils.isEmpty(productRO.getTableData())){
            throw new ServiceException("请填写规格值");
        }
        return insertResult(productService.insertProduct(productRO));
    }

    @ApiOperation(value = "查询产品id", notes = "查询产品id")
    @RequestMapping(value = "/selectProductById", method = RequestMethod.GET)
    //@LoginCheck
    public ResponseResultVO selectProductById(@RequestParam String id) {
        return ResponseResultVO.successResult(productService.selectProductById(id));
    }

    @ApiOperation(value = "修改产品", notes = "修改产品")
    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
    //@LoginCheck
    public ResponseResultVO updateProduct(@RequestBody ProductRO productRO) {
        return updateResult(productService.updateProduct(productRO));
    }

    @ApiOperation(value = "删除产品", notes = "删除产品")
    @RequestMapping(value = "/deleteProduct", method = RequestMethod.GET)
    //@LoginCheck
    public ResponseResultVO deleteProduct(@RequestParam Long id) {
        return deleteResult(productService.deleteProduct(id));
    }

    @ApiOperation(value = "上下架产品", notes = "上下架产品")
    @RequestMapping(value = "/updateProductStatus", method = RequestMethod.GET)
    //@LoginCheck
    public ResponseResultVO updateProductStatus(@RequestParam Long id) {
        return updateResult(productService.updateProductStatus(id));
    }

    @ApiOperation(value = "查询所有的产品", notes = "查询所有的产品")
    @RequestMapping(value = "/selectSelfProductList", method = RequestMethod.GET)
    //@LoginCheck
    public ResponseResultVO selectSelfProductList() {
        return ResponseResultVO.successResult(productService.selectSelfProductList());
    }
}
