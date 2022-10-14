package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/6/2814:26
 */
@Data
@TableName("tb_seckill_product")
public class SeckillProductDTO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String storeId;
    private Long productId;
    private Long seckillConfigId;
    private Date startTime;
    private Date endTime;
    private Integer stock;
    private BigDecimal seckillPrice;
    private Integer limitCount;
    private Date createTime;
    private Integer isDelete;
    private Integer status;
    private Integer salesStock;

}
