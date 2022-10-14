package com.qimeixun.aspect;

import com.qimeixun.annotation.LoginCheck;
import com.qimeixun.constant.SystemConstant;
import com.qimeixun.entity.SysUser;
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
import java.util.concurrent.TimeUnit;

/**
 * 登录验证AOP切面实现
 * @author wangdaqiang
 * @date 2019-08-24 11:25
 */
@Component
@Aspect
public class LoginCheckAspect {

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
     * @param loginCheck 自定义注解
     * @return
     */
    @Around("@annotation(loginCheck)")
    public Object loginCheck(ProceedingJoinPoint joinPoint, LoginCheck loginCheck) throws Throwable {
        if (logger.isDebugEnabled()) {
            logger.debug("---- 进入登录验证AOP切面 -----");
        }
        // 获取当前用户token
        String token = this.tokenUtil.getToken();
        if (StringUtils.isBlank(token)) {
            return ResponseResultVO.failNoLoginResult("登录失效");
        }
        String tokenRedisKey = "token:" + token;
        Map<Object, Object> map = this.redisTemplate.opsForHash().entries(tokenRedisKey);
        // Redis中会话的用户类型
        Object userType = map.get(SystemConstant.REDIS_USER_TYPE_KEY);
        // Redis中会话的用户信息对象
        Object userInfo = map.get(SystemConstant.REDIS_USER_INFO_KEY);
        if (userInfo == null) {
            return ResponseResultVO.failNoLoginResult("登录失效");
        }
        // 是否不验证权限
        boolean isNotCheckPermission = false;
        if (SystemConstant.REDIS_MEMBER_USER_TYPE.equals(userType)) {
            // 会员用户
            isNotCheckPermission = true;
            // 重置token失效时间
            this.redisTemplate.expire(tokenRedisKey, Integer.parseInt(this.userTokenTimeout), TimeUnit.DAYS);
        } else if (SystemConstant.REDIS_SYS_USER_TYPE.equals(userType)){
            if (userInfo instanceof SysUser) {
                // 后台管理员用户
                SysUser sysUser = (SysUser) userInfo;
                // 判断是否是超级管理员
                if (sysUser.getIsSuper() != null && sysUser.getIsSuper() == 1) {
                    isNotCheckPermission = true;
                }
            }
            // 重置token失效时间
            this.redisTemplate.expire(tokenRedisKey, Integer.parseInt(this.adminTokenTimeout), TimeUnit.MINUTES);
        } else {
            // 不匹配的会话数据当做登录失效处理
            return ResponseResultVO.failNoLoginResult("登录失效");
        }
        // 权限验证字符串
        String permission = loginCheck.permission();
        // 超级管理员不进行权限验证
        if (StringUtils.isBlank(permission) || isNotCheckPermission) {
            return joinPoint.proceed();
        }
        // TODO 暂时屏蔽权限过滤 wangdaqiang 2019-11-20 16:43
//        // 获取用户权限字符串集合
//        Object permissionObj = map.get(SystemConstant.REDIS_PERMISSION_LIST_KEY);
//        if (permissionObj == null) {
//            return new ResponseResultVO<>(ErrorCode.NO_PERMISSION);
//        }
//        List<String> permissionList = (List<String>) permissionObj;
//
//        if (!permissionList.contains(permission)) {
//            return new ResponseResultVO<>(ErrorCode.NO_PERMISSION);
//        }
        return joinPoint.proceed();
    }


}
