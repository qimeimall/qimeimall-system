package com.qimeixun.po;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_order_refund_product")
public class OrderRefundProductDTO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long refundOrderId;
    private Long productId;
    private String attrValue;
    private Integer payNum;
    private Integer refundNum;
    private BigDecimal price;
    @JsonIgnore
    private String productJson;
    private Date createTime;

    @TableField(exist = false)
    private ProductDTO product;

    public ProductDTO getProduct() {
        if(StrUtil.isNotBlank(productJson)){
            ProductDTO productDTO = JSONUtil.toBean(productJson, ProductDTO.class);
            productDTO.setCostPrice(BigDecimal.ZERO);
            return productDTO;
        }
        return product;
    }
}
