package com.qimeixun.ro;

import lombok.Data;

/**
 * @author chenshouyang
 * @date 2020/5/1321:17
 */
@Data
public class ProductListRO extends PageRO {

    private Integer storeId;

    private String productName;

    private String categoryName;
}
