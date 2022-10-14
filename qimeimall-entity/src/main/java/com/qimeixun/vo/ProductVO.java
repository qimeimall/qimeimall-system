package com.qimeixun.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.qimeixun.entity.Specs;
import com.qimeixun.po.ProductAttrDetailDTO;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/5/2311:37
 */
@Data
public class ProductVO {

    private Long id;

    /**
     * 商品分类id
     */
    private Long categoryId;

    /**
     * 产品名字
     */
    private String productName;

    /**
     * 单位
     */
    private String unit;

    /**
     * 排序
     */
    private String sort;

    /**
     * 已经出售数量
     */
    private int salesCount;

    /**
     * 限购数量
     */
    private int limitBuyCount;

    /**
     * 产品图片
     */
    private String productImg;

    /**
     * 产品轮播图
     */
    private String productBannerImg;

    /**
     * 零售价
     */
    private double price;

    /**
     * 市场价
     */
    private double marketPrice;

    /**
     * 成本价
     */
    private double costPrice;

    /**
     * 库存
     */
    private int stockCount;

    /**
     * 产品码
     */
    private String productCode;

    /**
     * 属性
     */
    @TableField(exist = false)
    private List<Map<String,String>> tableData;

    /**
     * 属性
     */
    private List<Specs> specs;

    /**
     * 产品图文详情
     */
    private String productDetail;

    private String isDelete;

    private String status;

    private String isHot;

    private List<String> banners;

    private List<ProductAttrDetailDTO> attrDetails;

    private String isCollection;
}
