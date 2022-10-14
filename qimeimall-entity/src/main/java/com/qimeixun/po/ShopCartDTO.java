package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/5/2418:50
 */
@Data
@TableName("tb_shop_cart")
public class ShopCartDTO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer userId;
    private Long productId;
    private Long productAttrId;
    private int num;
    private int isPay;
    private int isDelete;
    private Date createTime;
    private Long seckillProductId;
    private int isReply;

    private String productInfo;

    private String attrValue;

    private BigDecimal price;

}
