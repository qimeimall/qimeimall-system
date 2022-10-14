package com.qimeixun.listen;

import cn.hutool.core.util.StrUtil;
import com.qimeixun.service.OrderCommonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import javax.annotation.Resource;

@Configuration
public class RedisListenerConfig {

    @Resource
    private OrderCommonService orderCommonService;

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        String topic = StrUtil.format("__keyevent@{}__:expired", "*");
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(new RedisKeyExpirationListener(orderCommonService), new PatternTopic(topic));
        return container;
    }
}
