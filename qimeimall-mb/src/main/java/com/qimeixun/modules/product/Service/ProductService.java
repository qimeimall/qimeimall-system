package com.qimeixun.modules.product.Service;

import com.qimeixun.po.ProductDTO;
import com.qimeixun.ro.ProductDetailRO;
import com.qimeixun.vo.ProductVO;
import com.qimeixun.vo.ResponseResultVO;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/2311:33
 */
public interface ProductService {
    ProductVO selectProductInfoById(ProductDetailRO productDetailRO);

    List<ProductDTO> selectProductHot(String storeId);

    List<ProductDTO> selectProductRecommend(String storeId);

    int collectionProduct(String productId);

    List<ProductDTO> selectProductCollection();
}
