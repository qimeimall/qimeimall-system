package com.qimeixun.modules.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qimeixun.po.ProductDTO;
import com.qimeixun.ro.ProductListRO;
import com.qimeixun.ro.ProductRO;
import com.qimeixun.vo.ProductListVO;
import com.qimeixun.vo.ResponseResultVO;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/1320:14
 */
public interface ProductService {
    IPage<ProductListVO> selectProductList(ProductListRO productListRO);

    int insertProduct(ProductRO productRO);

    ProductDTO selectProductById(String id);

    int updateProduct(ProductRO productRO);

    int deleteProduct(Long id);

    int updateProductStatus(Long id);

    List<ProductDTO> selectSelfProductList();
}
