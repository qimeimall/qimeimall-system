package com.qimeixun.ro;

import com.qimeixun.entity.Specs;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/5/1510:29
 */
@Data
public class ProductRO {

    private Long id;

    /**
     * 产品名字
     */
    @NotNull(message = "请输入商品名称")
    private String productName;

    /**
     * 商品分类id
     */
    @NotNull(message = "请选择商品分类")
    private Long categoryId;

    /**
     * 单位
     */
    @NotNull(message = "请填写单位")
    private String unit;

    /**
     * 排序
     */
    private String sort;

    /**
     * 已经出售数量
     */
    private Integer salesCount;

    /**
     * 限购数量
     */
    private Integer limitBuyCount;

    /**
     * 产品图片
     */
    @NotNull(message = "请上传商品图片")
    private String productImg;

    /**
     * 产品轮播图
     */
    @NotNull(message = "请上传商品轮播图")
    private String productBannerImg;

    /**
     * 零售价
     */
    @NotNull(message = "请填写售价")
    @Min(value = 0, message = "售价必须大于等于0")
    private BigDecimal price;

    /**
     * 市场价
     */
    @NotNull(message = "请填写市场价")
    @Min(value = 0, message = "市场价必须大于等于0")
    private BigDecimal marketPrice;

    /**
     * 成本价
     */
    @NotNull(message = "请填写成本价")
    @Min(value = 0, message = "成本价必须大于等于0")
    private BigDecimal costPrice;

    /**
     * 库存
     */
    @NotNull(message = "请填写库存")
    @Min(value = 0, message = "库存必须大于等于0")
    private Integer stockCount;

    /**
     * 产品码
     */
    @NotNull(message = "请填写商品编码")
    private String productCode;

    /**
     * 属性
     */
    private List<Map<String, String>> tableData;

    /**
     * 属性
     */
    private List<Specs> specs;

    /**
     * 产品图文详情
     */
    @NotNull(message = "请填写商品详情")
    private String productDetail;

    private String isHot;

    private String isPoints;

    private String isRecommend;
}
