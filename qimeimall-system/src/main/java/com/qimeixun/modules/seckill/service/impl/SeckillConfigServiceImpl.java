package com.qimeixun.modules.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.SeckillConfigMapper;
import com.qimeixun.modules.seckill.service.SeckillConfigService;
import com.qimeixun.po.SeckillConfigDTO;
import com.qimeixun.ro.SeckillConfigRO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/6/1922:28
 */
@Service
public class SeckillConfigServiceImpl implements SeckillConfigService {

    @Resource
    SeckillConfigMapper seckillConfigMapper;

    @Override
    public int insertSeckillConfig(SeckillConfigRO seckillConfigRO) {
        if (checkParameter(seckillConfigRO)) {
            throw new ServiceException("开始时间或者持续时长不能为空");
        }
        SeckillConfigDTO seckillConfigDTO = new SeckillConfigDTO();
        BeanUtils.copyProperties(seckillConfigRO, seckillConfigDTO);
        return seckillConfigMapper.insert(seckillConfigDTO);
    }

    @Override
    public int updateSeckillConfig(SeckillConfigRO seckillConfigRO) {
        if (checkParameter(seckillConfigRO)) {
            throw new ServiceException("开始时间或者持续时长不能为空");
        }
        SeckillConfigDTO seckillConfigDTO = new SeckillConfigDTO();
        BeanUtils.copyProperties(seckillConfigRO, seckillConfigDTO);
        return seckillConfigMapper.updateById(seckillConfigDTO);
    }

    @Override
    public int deleteSeckillConfig(String id) {
        return seckillConfigMapper.deleteById(id);
    }

    @Override
    public SeckillConfigDTO selectSeckillConfigById(String id) {
        return seckillConfigMapper.selectById(id);
    }

    @Override
    public List<SeckillConfigDTO> selectSeckillConfig(SeckillConfigRO seckillConfigRO) {
        QueryWrapper<SeckillConfigDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("begin_time");
        return seckillConfigMapper.selectList(queryWrapper);
    }

    private boolean checkParameter(SeckillConfigRO seckillConfigRO) {
        if (seckillConfigRO.getBeginTime() == null || seckillConfigRO.getContinueTime() == null) {
            return true;
        }
        return false;
    }
}
