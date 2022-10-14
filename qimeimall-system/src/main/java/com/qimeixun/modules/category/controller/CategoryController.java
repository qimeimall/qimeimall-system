package com.qimeixun.modules.category.controller;

import com.qimeixun.annotation.CheckPermission;
import com.qimeixun.annotation.LoginCheck;
import com.qimeixun.base.BaseController;
import com.qimeixun.entity.Category;
import com.qimeixun.modules.category.service.CategoryService;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenshouyang
 * @date 2020/5/1312:38
 */
@Api(tags = "商品分类管理")
@RestController
@RequestMapping(value = "/product", produces = "application/json;charset=UTF-8")
public class CategoryController extends BaseController {

    @Resource
    CategoryService categoryService;

    @ApiOperation(value = "查询商品分类列表", notes = "查询商品分类列表")
    @RequestMapping(value = "/selectCategoryList", method = RequestMethod.GET)
    @CheckPermission(permission = "product:category")
    public ResponseResultVO selectCategoryList(@RequestParam String storeId) {
        return ResponseResultVO.successResult(categoryService.selectCustomerList(storeId));
    }

    @ApiOperation(value = "新增商品分类", notes = "新增商品分类")
    @RequestMapping(value = "/insertCategory", method = RequestMethod.POST)
    @CheckPermission(permission = "category:add")
    public ResponseResultVO insertCategory(@RequestBody Category category) {
        if (StringUtils.isEmpty(category.getCategoryName())) {
            return ResponseResultVO.failResult("分类名称不能为空");
        }
        if (StringUtils.isEmpty(category.getSort())) {
            return ResponseResultVO.failResult("排序不能为空");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(category.getCategoryImg())) {
            return ResponseResultVO.failResult("图标不能为空");
        }
        return insertResult(categoryService.insertCategory(category));
    }

    @ApiOperation(value = "编辑商品分类", notes = "编辑商品分类")
    @RequestMapping(value = "/updateCategory", method = RequestMethod.POST)
    @CheckPermission(permission = "category:update")
    public ResponseResultVO updateCategory(@RequestBody Category category) {
        return updateResult(categoryService.updateCategory(category));
    }

    @ApiOperation(value = "通过id查询商品分类", notes = "通过id查询商品分类")
    @RequestMapping(value = "/selectCategoryById", method = RequestMethod.GET)
    public ResponseResultVO selectCategoryById(@RequestParam String id) {
        return ResponseResultVO.successResult(categoryService.selectCategoryById(id));
    }

    @ApiOperation(value = "删除分类", notes = "通过id查询商品分类")
    @RequestMapping(value = "/deleteCategoryById", method = RequestMethod.GET)
    @CheckPermission(permission = "category:delete")
    public ResponseResultVO deleteCategoryById(@RequestParam String id) {
        return deleteResult(categoryService.deleteCategoryById(id));
    }

    @ApiOperation(value = "查询商品分类未分页", notes = "查询商品分类未分页")
    @RequestMapping(value = "/selectCategoryListNoPage", method = RequestMethod.GET)
    public ResponseResultVO selectCategoryListNoPage(@RequestParam(required = false) String storeId) {
        return ResponseResultVO.successResult(categoryService.selectCategoryListNoPage(storeId));
    }

    @ApiOperation(value = "查看自己门店的产品分类", notes = "查看自己门店的产品分类")
    @RequestMapping(value = "/selectSelfCategoryList", method = RequestMethod.GET)
    public ResponseResultVO selectSelfCategoryList() {
        return ResponseResultVO.successResult(categoryService.selectSelfCategoryList());
    }
}
