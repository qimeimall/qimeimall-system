package com.qimeixun.aspect;

import com.qimeixun.annotation.Login;
import com.qimeixun.constant.SystemConstant;
import com.qimeixun.enums.ErrorCode;
import com.qimeixun.util.TokenUtil;
import com.qimeixun.vo.ResponseResultVO;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 登录验证AOP切面实现
 * @author wangdaqiang
 * @date 2019-08-24 11:25
 */
@Component
@Aspect
public class LoginAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoginCheckAspect.class);


    @Value("${admin.token.timeout}")
    private String adminTokenTimeout;
    @Value("${user.token.timeout}")
    private String userTokenTimeout;


    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    @Resource
    private TokenUtil tokenUtil;


    /**
     * 登录验证AOP切面
     * @param joinPoint 切入对象
     * @param login 自定义注解
     * @return
     */
    @Around("@annotation(login)")
    public Object loginCheck(ProceedingJoinPoint joinPoint, Login login) throws Throwable {
        if (logger.isDebugEnabled()) {
            logger.debug("---- 进入登录验证AOP切面 -----");
        }
        // 获取当前用户token
        String token = this.tokenUtil.getToken();
        if (StringUtils.isBlank(token)) {
            return ResponseResultVO.failNoLoginResult("登录失效");
        }
        String tokenRedisKey = "call_center_token:" + token;
        Map<Object, Object> map = this.redisTemplate.opsForHash().entries(tokenRedisKey);
        // Redis中会话的用户信息对象
        Object userInfo = map.get(SystemConstant.REDIS_USER_INFO_KEY);
        if (userInfo == null) {
            return ResponseResultVO.failNoLoginResult("登录失效");
        }
        return joinPoint.proceed();
    }


}

