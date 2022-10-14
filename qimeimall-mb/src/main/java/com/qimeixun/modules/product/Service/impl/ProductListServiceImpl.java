package com.qimeixun.modules.product.Service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.constant.SystemConstant;
import com.qimeixun.entity.Category;
import com.qimeixun.mapper.CategoryMapper;
import com.qimeixun.mapper.ProductMapper;
import com.qimeixun.modules.product.Service.ProductListService;
import com.qimeixun.po.ProductDTO;
import com.qimeixun.ro.ProductListByRO;
import com.qimeixun.vo.ResponseResultVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/2210:03
 */
@Service
public class ProductListServiceImpl implements ProductListService {

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    ProductMapper productMapper;

    @Override
    public ResponseResultVO selectProductList(String storeId) {

        //查询一级目录
        List<Category> categories = getCategories(storeId, 0);
        for (Category category : categories) {
            //查询二级目录
            List<Category> categoriesChilds = getCategories(storeId, category.getId());
            category.setChildren(categoriesChilds);
        }
        return ResponseResultVO.successResult(categories);
    }

    /**
     * 查询产品列表
     *
     * @param productListByRO
     * @return
     */
    @Override
    public ResponseResultVO selectProductListById(ProductListByRO productListByRO) {
        if (StringUtils.isEmpty(productListByRO.getSortType())) {
            return ResponseResultVO.failResult("参数不正确");
        }
        if (StrUtil.isBlank(productListByRO.getProductType())) {
            productListByRO.setProductType("0"); // 普通商品
        }
        QueryWrapper<ProductDTO> productDTOQueryWrapper = new QueryWrapper<>();
        productDTOQueryWrapper.eq("is_delete", 0);
        productDTOQueryWrapper.eq("status", 0);
        productDTOQueryWrapper.eq("is_points", productListByRO.getProductType());
        if (StringUtils.isNotEmpty(productListByRO.getCategoryId())) {
            productDTOQueryWrapper.eq("category_id", productListByRO.getCategoryId());
        }
        if (StringUtils.isNotEmpty(productListByRO.getProductName())) {
            productDTOQueryWrapper.like("product_name", productListByRO.getProductName());
        }
        productDTOQueryWrapper.eq("store_id", productListByRO.getStoreId());
        switch (productListByRO.getSortType()) {
            case SystemConstant.PRODUCT_DEFAULT_SORT_TYPE:
                productDTOQueryWrapper.orderByAsc("sort");
                break;
            case SystemConstant.PRODUCT_TIME_SORT_TYPE:
                productDTOQueryWrapper.orderByDesc("create_time");
                break;
            case SystemConstant.PRODUCT_PRICE_ASC_SORT_TYPE:
                productDTOQueryWrapper.orderByAsc("price");
                break;
            case SystemConstant.PRODUCT_PRICE_DESC_SORT_TYPE:
                productDTOQueryWrapper.orderByDesc("price");
                break;
            case SystemConstant.PRODUCT_SALES_SORT_TYPE:
                productDTOQueryWrapper.orderByDesc("sales_count");
                break;
            default:
                return ResponseResultVO.failResult("参数错误");
        }
        List<ProductDTO> products = productMapper.selectList(productDTOQueryWrapper);
        return ResponseResultVO.successResult(products);
    }

    private List<Category> getCategories(String storeId, Integer pid) {
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.eq("store_id", storeId);
        categoryQueryWrapper.eq("status", "0");
        categoryQueryWrapper.eq("pid", pid);
        categoryQueryWrapper.orderByAsc("sort");
        return categoryMapper.selectList(categoryQueryWrapper);
    }
}
