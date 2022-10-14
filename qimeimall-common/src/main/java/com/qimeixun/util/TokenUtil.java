package com.qimeixun.util;

import com.qimeixun.config.Audience;
import com.qimeixun.constant.SystemConstant;
import com.qimeixun.entity.SysUser;
import com.qimeixun.exceptions.NoLoginException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;

@Component
public class TokenUtil {

    /**
     * Redis存放路径
     */
    public final String redisKey = "token:";

    @Resource
    Audience audience;


    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 获取请求头中的token参数值
     *
     * @return
     */
    public String getToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader(JwtUtil.AUTH_HEADER_KEY);
        return token;
    }

    /**
     * 根据token获取当前登录用户id
     *
     * @return
     */
    public String getUserIdByToken() {
        String token = this.getToken();
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return JwtUtil.getUserId(this.getToken(), audience.getBase64Secret());
    }

    public String getUnCheckUserIdByToken() {
        String token = this.getToken();
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return JwtUtil.getUnCheckUserId(this.getToken(), audience.getBase64Secret());
    }


    /**
     * 获取管理员用户登录信息
     *
     * @return
     */
    public SysUser getSysUserInfo() {
        SysUser sysUser = chenUserInfo();
        if (sysUser == null) {
            throw new NoLoginException("login ex");
        } else {
            return sysUser;
        }
    }

    private SysUser chenUserInfo() {
        String token = this.getToken();
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String tokenRedisKey = this.redisKey + token;
        Map<Object, Object> map = this.redisTemplate.opsForHash().entries(tokenRedisKey);
        // Redis中会话的用户类型
        Object userType = map.get(SystemConstant.REDIS_USER_TYPE_KEY);
        // Redis中会话的用户信息对象
        Object userInfo = map.get(SystemConstant.REDIS_USER_INFO_KEY);
        if (userInfo == null) {
            return null;
        }
        if (SystemConstant.REDIS_SYS_USER_TYPE.equals(userType)) {
            // 后台管理员用户
            if (userInfo instanceof SysUser) {
                return (SysUser) userInfo;
            }
        }
        return null;
    }

    /**
     * 获取外网ip
     *
     * @return
     */
    public static String getV4IP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String[] localhost = {"127.0.0.1", "0:0:0:0:0:0:0:1"};
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (Arrays.asList(localhost).contains(ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    /**
     * 获取请求用户IP
     *
     * @return
     */
    public String getIpAddr() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return this.getV4IP(request);
        } catch (Exception exception) {
            return "";
        }
    }


}
