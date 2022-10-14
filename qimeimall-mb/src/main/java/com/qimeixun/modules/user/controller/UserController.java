package com.qimeixun.modules.user.controller;

import com.qimeixun.annotation.JwtIgnore;
import com.qimeixun.base.BaseController;
import com.qimeixun.modules.user.service.UserService;
import com.qimeixun.ro.PageRO;
import com.qimeixun.ro.PhoneLoginRO;
import com.qimeixun.ro.UserLoginRO;
import com.qimeixun.util.RegexUtils;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/5/2218:03
 */
@Api(tags = "用户")
@RestController
@RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")
@CrossOrigin
public class UserController extends BaseController {

    @Resource
    UserService userService;

    @ApiOperation(value = "获取用户基本信息", notes = "获取用户基本信息")
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public ResponseResultVO getUserInfo(@RequestBody UserLoginRO userLoginRO) {
        return ResponseResultVO.successResult(userService.getUserInfo(userLoginRO));
    }

    @ApiOperation(value = "发送验证码", notes = "发送验证码")
    @RequestMapping(value = "/sendCode", method = RequestMethod.GET)
    @JwtIgnore
    public ResponseResultVO sendCode(@RequestParam String phone) {
        if (!RegexUtils.checkMobile(phone)) {
            return ResponseResultVO.failResult("手机号不正确");
        }
        userService.sendCode(phone);
        return ResponseResultVO.successResult();
    }

    @ApiOperation(value = "手机号和验证码登录", notes = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @JwtIgnore
    public ResponseResultVO login(@RequestBody PhoneLoginRO phoneLoginRO) {
        return ResponseResultVO.successResult(userService.login(phoneLoginRO));
    }

    @ApiOperation(value = "查询我的", notes = "查询我的")
    @RequestMapping(value = "/selectMyInfo", method = RequestMethod.GET)
    public ResponseResultVO selectMyInfo() {
        return ResponseResultVO.successResult(userService.selectMyInfo());
    }

    @ApiOperation(value = "查询我的服务列表", notes = "查询我的服务列表")
    @RequestMapping(value = "/getMyServiceList", method = RequestMethod.GET)
    public ResponseResultVO getMyServiceList() {
        return ResponseResultVO.successResult(userService.getMyServiceList());
    }

    @ApiOperation(value = "查询我的余额", notes = "查询我的余额")
    @RequestMapping(value = "/selectUserBalance", method = RequestMethod.GET)
    public ResponseResultVO selectUserBalance() {
        return ResponseResultVO.successResult(userService.selectUserBalance());
    }

    @ApiOperation(value = "查询签到配置列表", notes = "查询签到配置列表")
    @RequestMapping(value = "/selectUserSignList", method = RequestMethod.GET)
    public ResponseResultVO selectUserSignList() {
        return ResponseResultVO.successResult(userService.selectUserSignList());
    }

    @ApiOperation(value = "用户签到的积分", notes = "用户签到的积分")
    @RequestMapping(value = "/addSignRecord", method = RequestMethod.GET)
    public ResponseResultVO addSignRecord() {
        userService.addSignRecord();
        return ResponseResultVO.successResult();
    }

    @ApiOperation(value = "查询用户的积分记录", notes = "查询用户的积分记录")
    @RequestMapping(value = "/selectUserPointsList", method = RequestMethod.POST)
    public ResponseResultVO selectUserPointsList(@RequestBody PageRO pageRO) {
        return getPageObject(userService.selectUserPointsList(pageRO), pageRO);
    }

}
