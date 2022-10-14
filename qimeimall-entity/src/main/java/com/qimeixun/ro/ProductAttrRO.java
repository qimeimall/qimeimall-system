package com.qimeixun.ro;

import lombok.Data;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/2418:19
 */
@Data
public class ProductAttrRO {

    /**
     * 所选规格集合
     */
    private List<String> curNav;

    /**
     * 选择商品数量
     */
    private int selectProductCount;
}
