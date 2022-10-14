package com.qimeixun.modules.system.controller;

import com.qimeixun.annotation.JwtIgnore;
import com.qimeixun.modules.system.service.SysLoginService;
import com.qimeixun.ro.SysLoginRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author wangdaqiang
 * @date 2019-08-23 15:36
 */
@Api(tags = "后台-管理员登录")
@RestController
@RequestMapping(value = "/sys/login")
public class SysLoginController {

    @Resource
    private SysLoginService sysLoginService;


    @ApiOperation(value = "管理员登录", notes = "管理员登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @JwtIgnore
    public ResponseResultVO login(@Validated @RequestBody SysLoginRO sysLoginRO) {
        return this.sysLoginService.login(sysLoginRO);
    }


    ////@LoginCheck
    @ApiOperation(value = "管理员注销登录", notes = "管理员注销登录")
    @RequestMapping(value = "/loginOut", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResultVO loginOut() {
        return this.sysLoginService.loginOut();
    }

    @ApiOperation(value = "获取图片验证码", notes = "获取图片验证码")
    @GetMapping(value = "/captcha")
    @JwtIgnore
    public ResponseResultVO captcha() {
        return this.sysLoginService.captcha();
    }


}
