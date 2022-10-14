package com.qimeixun.vo;

import lombok.Data;

/**
 * @author chenshouyang
 * @date 2020/7/214:37
 */
@Data
public class SeckillProductListVO {

    private Long seckillProductId;

    /**
     * 产品id
     */
   private Long productId;
    /**
     * 产品名字
     */
   private String productName;
    /**
     * 产品图片
     */
   private String productImg;
    /**
     * 秒杀价格
     */
   private String seckillPrice;
    /**
     * 库存
     */
   private int stockCount;
    /**
     * 开始时间
     */
   private String startTime;
    /**
     * 结束时间
     */
   private String endTime;
    /**
     * 已秒数量
     */
   private int salesStock;
}
