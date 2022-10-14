package com.qimeixun.initdata;

import com.qimeixun.constant.SystemConstant;
import com.qimeixun.po.SysConfigDTO;
import com.qimeixun.service.SysConfigService;
import com.qimeixun.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class InitParameterService {

    @Resource
    private SysConfigService sysConfigService;

    @Resource
    private RedisUtil redisUtil;

    @PostConstruct
    public void initParameter() {
        //初始化参数
        log.info("开始初始化参数开始");
        setRedisSysConfig();
        log.info("开始初始化参数结束");
    }

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    private void setRedisSysConfig() {
//        SysConfigDTO sysConfigDTO = sysConfigService.getSysConfigValue(key);
        List<SysConfigDTO> sysConfigDTOS = sysConfigService.selectConfigList(null);
        for(SysConfigDTO sysConfig: sysConfigDTOS){
            redisUtil.set(SystemConstant.SYS_CONFIG_HEAD + sysConfig.getConfigKey(), sysConfig.getConfigValue());
        }
    }

}
