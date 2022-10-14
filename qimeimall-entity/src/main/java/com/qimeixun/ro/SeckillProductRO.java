package com.qimeixun.ro;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/6/2814:09
 */
@Data
public class SeckillProductRO extends PageRO {

    private Long id;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 秒杀配置id
     */
    private Long seckillConfigId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 库存
     */
    private int stock;

    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;

    /**
     * 限购数量
     */
    private int limitCount;

    /**
     * 门店id
     */
    private String storeId;

    /**
     * 是否删除
     */
    private int isDelete;


}
