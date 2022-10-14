package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author chenshouyang
 * @date 2020/5/1511:37
 */
@Data
@TableName("tb_product_attr")
public class ProductAttr {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long productId;

    private String groupName;

    private String attrValue;
}
