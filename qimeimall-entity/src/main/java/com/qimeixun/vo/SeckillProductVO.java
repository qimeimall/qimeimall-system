package com.qimeixun.vo;

import com.qimeixun.po.SeckillProductDTO;
import lombok.Data;

/**
 * @author chenshouyang
 * @date 2020/6/2814:48
 */
@Data
public class SeckillProductVO  extends SeckillProductDTO {

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 秒杀配置开启时间
     */
    private int beginTime;

    /**
     * 秒杀配置持续时间
     */
    private int continueTime;

}
