package com.qimeixun.vo;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qimeixun.po.ProductDTO;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author chenshouyang
 * @date 2020/6/214:20
 */
@Data
public class UserProductVO {

    private Integer cartId;

    @JsonIgnore
    private String productInfoJson;

    private ProductDTO productDTO;

    private int num;

    private String attrValue;

    private BigDecimal price;

    private String isReply;

    private int refundNum;

    public String getIsReply() {
        return isReply;
    }

    public void setIsReply(String isReply) {
        this.isReply = isReply;
    }

    public String getProductInfoJson() {
        return productInfoJson;
    }

    public void setProductInfoJson(String productInfoJson) {
        this.productInfoJson = productInfoJson;
        if(StringUtils.isNotEmpty(productInfoJson)){
            ProductDTO productDTO = JSON.parseObject(productInfoJson, ProductDTO.class);
            this.productDTO = productDTO;
        }
    }

    public ProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
