package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenshouyang
 * @date 2020/5/1512:09
 */
@Data
@TableName("tb_product_attr_detail")
public class ProductAttrDetailDTO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long productId;

    private String attrValue;

    private int stockCount;

    private BigDecimal price;

    private String productCode;

    private String attrContent;
}
