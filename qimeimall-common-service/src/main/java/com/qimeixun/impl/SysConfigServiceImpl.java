package com.qimeixun.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.constant.SystemConstant;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.SysConfigMapper;
import com.qimeixun.po.SysConfigDTO;
import com.qimeixun.ro.UpdateSysConfigRO;
import com.qimeixun.service.SysConfigService;
import com.qimeixun.util.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Resource
    SysConfigMapper sysConfigMapper;

    @Resource
    RedisUtil redisUtil;

    @Override
    public SysConfigDTO getSysConfigValue(String key) {
        return sysConfigMapper.selectOne(new QueryWrapper<SysConfigDTO>().eq("config_key", key));
    }

    @Override
    public String getSysConfigValueFromRedis(String key) {
        Object o = redisUtil.get(SystemConstant.SYS_CONFIG_HEAD + key);
        if (o instanceof SysConfigDTO) {
            SysConfigDTO sysConfigDTO = (SysConfigDTO) o;
            return sysConfigDTO.getConfigValue();
        }else{
            return sysConfigMapper.selectOne(new QueryWrapper<SysConfigDTO>().eq("config_key", key)).getConfigValue();
        }
    }

    @Override
    public List<SysConfigDTO> selectConfigList(String keyType) {
        LambdaQueryWrapper<SysConfigDTO> queryWrapper = new QueryWrapper<SysConfigDTO>().lambda().orderByAsc(SysConfigDTO::getCreateTime);
        if(StrUtil.isNotBlank(keyType)){
            queryWrapper.eq(SysConfigDTO::getKeyType, keyType);
        }
        return sysConfigMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public int updateConfig(UpdateSysConfigRO updateSysConfigRO) {
        SysConfigDTO configDTO = sysConfigMapper.selectById(updateSysConfigRO.getId());
        if(configDTO == null){
            throw new ServiceException("配置不存在");
        }
        SysConfigDTO dto = new SysConfigDTO();
        dto.setId(updateSysConfigRO.getId());
        dto.setConfigValue(updateSysConfigRO.getConfigValue());
        //修改redis值内容
        redisUtil.set(SystemConstant.SYS_CONFIG_HEAD + configDTO.getConfigKey(), updateSysConfigRO.getConfigValue());
        return sysConfigMapper.updateById(dto);
    }
}
