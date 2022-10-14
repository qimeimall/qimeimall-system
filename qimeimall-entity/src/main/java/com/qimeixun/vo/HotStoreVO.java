package com.qimeixun.vo;

import com.qimeixun.po.ProductDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/6/421:33
 */
@Data
public class HotStoreVO implements Serializable {

    private Integer storeId;

    private String storeName;

    private String storeLogo;

    private int productCount;

    private List<ProductDTO> productDTOS;
}
