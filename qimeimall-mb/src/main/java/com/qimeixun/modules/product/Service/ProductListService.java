package com.qimeixun.modules.product.Service;

import com.qimeixun.ro.ProductListByRO;
import com.qimeixun.vo.ResponseResultVO;

/**
 * @author chenshouyang
 * @date 2020/5/2210:03
 */
public interface ProductListService {
    ResponseResultVO selectProductList(String storeId);

    ResponseResultVO selectProductListById(ProductListByRO productListByRO);
}
