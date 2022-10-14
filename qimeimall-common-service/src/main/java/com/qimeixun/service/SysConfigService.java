package com.qimeixun.service;

import com.qimeixun.po.SysConfigDTO;
import com.qimeixun.ro.UpdateSysConfigRO;

import java.util.List;

public interface SysConfigService {
    SysConfigDTO getSysConfigValue(String key);

    String getSysConfigValueFromRedis(String key);

    List<SysConfigDTO> selectConfigList(String keyType);

    int updateConfig(UpdateSysConfigRO updateSysConfigRO);
}
