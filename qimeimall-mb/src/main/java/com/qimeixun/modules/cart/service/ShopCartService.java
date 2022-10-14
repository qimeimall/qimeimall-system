package com.qimeixun.modules.cart.service;

import com.qimeixun.ro.ShopCartGoodsRO;
import com.qimeixun.vo.ResponseResultVO;
import com.qimeixun.vo.ShopCartListVO;

import java.util.List;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/5/2413:34
 */
public interface ShopCartService {
    List<ShopCartListVO> selectShopCartList();

    int insertShopCart(ShopCartGoodsRO shopCartGoodsRO);

    void deleteShopCartProduct(String id);

    void addShopCartProductNum(String id);

    void subShopCartProductNum(String id);

    Map<String, Object> selectGoodsCartCount();
}
