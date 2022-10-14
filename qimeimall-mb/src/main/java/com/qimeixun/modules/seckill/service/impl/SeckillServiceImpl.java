package com.qimeixun.modules.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.SeckillConfigMapper;
import com.qimeixun.mapper.SeckillProductMapper;
import com.qimeixun.modules.seckill.service.SeckillService;
import com.qimeixun.po.SeckillConfigDTO;
import com.qimeixun.po.SeckillProductDTO;
import com.qimeixun.vo.SeckillProductListVO;
import com.qimeixun.vo.SeckillProductVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/6/3017:29
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Resource
    SeckillProductMapper seckillProductMapper;

    @Resource
    SeckillConfigMapper seckillConfigMapper;

    @Override
    public List<SeckillConfigDTO> getSeckillConfigList() {
        return seckillConfigMapper.selectList(new QueryWrapper<SeckillConfigDTO>());
    }

    @Override
    public List<SeckillProductListVO> getSeckillProductList(String storeId, String seckillConfigId) {
        if (StringUtils.isEmpty(storeId)) {
            storeId = "0";
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("storeId", storeId);
        paramMap.put("seckillConfigId", seckillConfigId);
        return seckillProductMapper.getSeckillProductList(paramMap);
    }

    @Override
    public SeckillProductListVO getSeckillProduct(String seckillProductId) {
        SeckillProductDTO seckillProductDTO = seckillProductMapper.selectById(seckillProductId);
        if (seckillProductDTO == null) {
            throw new ServiceException("秒杀产品不存在");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("seckillId", seckillProductId);
        SeckillProductListVO seckillProduct = seckillProductMapper.getSeckillProduct(paramMap);
        if(seckillProduct == null){
            throw new ServiceException("该秒杀产品不存在");
        }
        return seckillProduct;
    }
}
