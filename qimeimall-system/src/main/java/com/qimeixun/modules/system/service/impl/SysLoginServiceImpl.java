package com.qimeixun.modules.system.service.impl;

import cn.hutool.core.util.IdUtil;
import com.qimeixun.config.Audience;
import com.qimeixun.constant.SystemConstant;
import com.qimeixun.entity.SysUser;
import com.qimeixun.enums.TerminalType;
import com.qimeixun.mapper.SysResourceMapper;
import com.qimeixun.mapper.SysRoleMapper;
import com.qimeixun.mapper.SysUserMapper;
import com.qimeixun.ro.SysLoginRO;
import com.qimeixun.modules.system.service.SysLoginService;
import com.qimeixun.modules.system.service.SysOperateLogService;
import com.qimeixun.util.*;
import com.qimeixun.vo.ResponseResultVO;
import com.qimeixun.vo.SysRoleVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sun.misc.BASE64Encoder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 管理员登录接口实现
 * @author wangdaqiang
 * @date 2019-08-23 15:45
 */
@Service
public class SysLoginServiceImpl implements SysLoginService {

    private static final Logger logger = LoggerFactory.getLogger(SysLoginServiceImpl.class);

    @Value("${admin.token.timeout}")
    private Integer tokenTimeout;

    @Value("${admin.password.md5.key}")
    private String sysUserMd5Key;


    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysResourceMapper sysResourceMapper;
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    @Resource
    private TokenUtil tokenUtil;
    @Resource
    private SysOperateLogService logService;
    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private VerifyCodeUtil verifyCodeUtil;

    @Resource
    Audience audience;

    @Override
    public ResponseResultVO login(SysLoginRO sysLoginRO) {
        try {

            Object o = this.redisTemplate.opsForValue().get("captcha:" + sysLoginRO.getClient());
            if(o == null){
                return ResponseResultVO.failResult("验证码不存在");
            }else{
                this.redisTemplate.delete("captcha:" + sysLoginRO.getClient()); // 删除已经使用过得验证码
            }
            if (!String.valueOf(o).toUpperCase().equals(sysLoginRO.getCaptcha().toUpperCase())){
                return ResponseResultVO.failResult("验证码失效");
            }

            SysUser sysUser = this.sysUserMapper.selectByLogin(sysLoginRO);
            if (sysUser == null) {
                return ResponseResultVO.failResult("账号不存在");
            }
            String oldToken = sysUser.getRememberToken();
            // 加密登录密码然后匹配
            String password = MD5Util.md5(sysLoginRO.getPassword(), this.sysUserMd5Key);
            if (!password.equals(sysUser.getPassword())) {
                return ResponseResultVO.failResult("密码错误");
            }
            // 密码不返回
            sysUser.setPassword(null);
            // 查询用户角色
            List<SysRoleVO> roleList = this.sysRoleMapper.selectByUserId(sysUser.getId());
            if (!CollectionUtils.isEmpty(roleList)) {
                sysUser.setRoleList(roleList);
            }

            // 查询用户权限资源集合
            List<String> permissionList = this.sysResourceMapper.selectPermissionByUserId(sysUser.getId(), TerminalType.WEB);
            if (permissionList == null) {
                permissionList = new ArrayList<>();
            }

            // 生成登录token
            //String token = Tool.getPrimaryKey();
            String token = JwtUtil.TOKEN_PREFIX + JwtUtil.createJWT(String.valueOf(sysUser.getId()), sysUser.getLoginName(), permissionList, audience);

            //记录token
            sysUser.setRememberToken(token);
            // 更新最后登录时间 以及更新token
            this.updateLastLoginTime(sysUser);


            Map<String, Object> map = new HashMap<>();
            map.put(SystemConstant.REDIS_USER_TYPE_KEY, SystemConstant.REDIS_SYS_USER_TYPE);
            map.put(SystemConstant.REDIS_USER_INFO_KEY, sysUser);
            map.put("token", token);
            // 用户的资源权限字符串集合
            map.put(SystemConstant.REDIS_PERMISSION_LIST_KEY, permissionList);

            // 缓存key
            final String tokenKey = "token:";
            String tokenRedisKey = tokenKey + token;
            this.redisTemplate.opsForHash().putAll(tokenRedisKey, map);
            this.redisTemplate.expire(tokenRedisKey, this.tokenTimeout, TimeUnit.MINUTES);

            if(StringUtils.isNotEmpty(oldToken)){
                if(this.redisTemplate.hasKey(tokenKey+oldToken)){
                    this.redisTemplate.delete(tokenKey + oldToken);
                }
            }

            String adminUserToken = "admin_user:" + sysUser.getLoginName();
            /*Object oldToken = this.redisTemplate.opsForValue().get(adminUserToken);
            if (oldToken != null && StringUtils.isNotBlank(oldToken.toString())) {
                // 删除历史token
                //this.redisTemplate.expire(tokenKey + oldToken.toString(), 1, TimeUnit.MILLISECONDS);
            }*/
            this.redisTemplate.opsForValue().set(adminUserToken, token);

            this.logService.insertLoginLog(sysUser, "登录系统");

            return ResponseResultVO.successResult(map);
        } catch (Exception e) {
            logger.error("管理员登录-异常", e);
            return ResponseResultVO.failResult();
        }
    }


    @Override
    public ResponseResultVO loginOut() {
        try {

            String token = this.tokenUtil.getToken();
            String tokenRedisKey = "token:" + token;
            this.redisTemplate.expire(tokenRedisKey, 1, TimeUnit.MILLISECONDS);

            //this.logService.insertLoginLog(sysUser, "退出系统");
            return ResponseResultVO.successResult("注销登录成功");
        } catch (Exception e) {
            logger.error("管理员注销登录-异常", e);
            return ResponseResultVO.errorResult();
        }
    }

    @Override
    public void loginOutByToken(String token){
        String tokenRedisKey = "token:" + token;
        if(this.redisTemplate.hasKey(tokenRedisKey)){
            this.redisTemplate.expire(tokenRedisKey, 1, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public ResponseResultVO captcha() {
        Map<String, Object> map = new HashMap<>();
        BufferedImage image = verifyCodeUtil.getImage();
        String code = verifyCodeUtil.getText();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Long client = IdUtil.getSnowflake(1, 1).nextId();
        try {
            ImageIO.write(image, "jpg", bos);
            byte[] imageBytes = bos.toByteArray();
            BASE64Encoder encoder = new BASE64Encoder();
            String imageString = encoder.encode(imageBytes);
            this.redisTemplate.opsForValue().set("captcha:" + client, code, 1,TimeUnit.MINUTES);
            map.put("base64Img", "data:image/jpg;base64," + imageString);
            map.put("client", String.valueOf(client));
            return ResponseResultVO.successResult(map);
        } catch (Exception ex) {
            return ResponseResultVO.failResult("获取验证码失败");
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loginOut(String userId) {
        if (StringUtils.isBlank(userId)) {
            return;
        }
        SysUser sysUser = this.sysUserMapper.selectById(userId);
        if (sysUser == null) {
            return;
        }
        final String tokenKey = "token:";
        final String adminUserToken = "admin_user:" + sysUser.getLoginName();

        Object token = this.redisTemplate.opsForValue().get(adminUserToken);
        if (token != null && StringUtils.isNotBlank(token.toString())) {
            // 删除token
            this.redisTemplate.expire(tokenKey + token.toString(), 1, TimeUnit.MILLISECONDS);
        }
    }


    /**
     * 更新最后登录时间
     * @param sysUser 用户id
     */
    private void updateLastLoginTime(SysUser sysUser) {
        SysUser userTemp = new SysUser();
        userTemp.setId(sysUser.getId());
        userTemp.setRememberToken(sysUser.getRememberToken());
        userTemp.setLastLoginTime(new Date());
        this.sysUserMapper.update(userTemp);
    }


}
