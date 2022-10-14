package com.qimeixun.modules.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qimeixun.po.SeckillProductDTO;
import com.qimeixun.ro.SeckillProductRO;
import com.qimeixun.vo.ResponseResultVO;
import com.qimeixun.vo.SeckillProductVO;

/**
 * @author chenshouyang
 * @date 2020/6/2814:13
 */
public interface SeckillProductService {
    int insertSeckillProduct(SeckillProductRO seckillProductRO);

    IPage<SeckillProductVO> selectSeckillProductList(SeckillProductRO seckillProductRO);

    int deleteSeckillProductById(String id);

    int updateStatusSeckillProductById(String id);

    SeckillProductDTO selectSeckillProductById(String id);

    int updateSeckillProduct(SeckillProductRO seckillProductRO);
}
