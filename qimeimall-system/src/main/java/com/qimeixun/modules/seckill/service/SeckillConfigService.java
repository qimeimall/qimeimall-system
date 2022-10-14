package com.qimeixun.modules.seckill.service;

import com.qimeixun.po.SeckillConfigDTO;
import com.qimeixun.ro.SeckillConfigRO;
import com.qimeixun.vo.ResponseResultVO;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/6/1922:28
 */
public interface SeckillConfigService {
    int insertSeckillConfig(SeckillConfigRO seckillConfigRO);

    int updateSeckillConfig(SeckillConfigRO seckillConfigRO);

    int deleteSeckillConfig(String id);

    SeckillConfigDTO selectSeckillConfigById(String id);

    List<SeckillConfigDTO> selectSeckillConfig(SeckillConfigRO seckillConfigRO);
}
