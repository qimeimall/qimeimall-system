package com.qimeixun.modules.system.service;

import com.qimeixun.ro.SysLoginRO;
import com.qimeixun.vo.ResponseResultVO;

/**
 * 管理员登录接口
 * @author wangdaqiang
 * @date 2019-08-23 15:39
 */
public interface SysLoginService {


    /**
     * 管理员登录
     * @param sysLoginRO
     * @return
     */
    ResponseResultVO login(SysLoginRO sysLoginRO);


    /**
     * 管理员注销登录
     * @return
     */
    ResponseResultVO loginOut();


    /**
     * 踢出用户
     * @param userId 用户id
     */
    void loginOut(String userId);

    /**
     * 踢出用户
     * @param token 用户id
     */
    void loginOutByToken(String token);

    ResponseResultVO captcha();
}
