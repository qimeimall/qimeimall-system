package com.qimeixun.modules.seckill.service;

import com.qimeixun.po.SeckillConfigDTO;
import com.qimeixun.vo.ResponseResultVO;
import com.qimeixun.vo.SeckillProductListVO;
import com.qimeixun.vo.SeckillProductVO;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/6/3017:28
 */
public interface SeckillService {
    List<SeckillConfigDTO> getSeckillConfigList();

    List<SeckillProductListVO> getSeckillProductList(String storeId, String seckillConfigId);

    SeckillProductListVO getSeckillProduct(String seckillProductId);
}
