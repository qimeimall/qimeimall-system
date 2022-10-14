package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qimeixun.entity.Specs;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author chenshouyang1111111111111
 * @date 2020/5/1511:11
 */
@Data
@TableName("tb_product")
public class ProductDTO implements Serializable{

    /**
     * 序列化id
     */
    private static final long serialVersionUID = -2387049612227179911L;

    @TableId(type = IdType.AUTO)
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
    private Integer salesCount;

    /**
     * 限购数量
     */
    private Integer limitBuyCount;

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
    private BigDecimal price;

    /**
     * 市场价
     */
    private BigDecimal marketPrice;

    /**
     * 成本价
     */
    private BigDecimal costPrice;

    /**
     * 库存
     */
    private Integer stockCount;

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
    @TableField(exist = false)
    private List<Specs> specs;

    /**
     * 产品图文详情
     */
    private String productDetail;

    private String isDelete;

    private String status;

    private String isHot;

    /**
     * 邮费
     */
    private double postPrice;

    private String storeId;

    private String isRecommend;


    /**
     * 是否积分商品
     */
    private String isPoints;

    private Date createTime;

}
