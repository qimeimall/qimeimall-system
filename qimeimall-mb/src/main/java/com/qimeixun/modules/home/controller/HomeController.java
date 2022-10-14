package com.qimeixun.modules.home.controller;

import com.qimeixun.annotation.JwtIgnore;
import com.qimeixun.modules.home.service.HomeService;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenshouyang
 * @date 2020/5/2015:22
 */
@Api(tags = "首页")
@RestController
@RequestMapping(value = "/home", produces = "application/json;charset=UTF-8")
public class HomeController {

    @Resource
    HomeService homeService;

//    @Cacheable("home")
    @ApiOperation(value = "查询首页配置列表", notes = "查询首页配置列表")
    @RequestMapping(value = "/selectHomeSetMenuList", method = RequestMethod.GET)
    @JwtIgnore
    public ResponseResultVO selectHomeMenuList() {
        return ResponseResultVO.successResult(homeService.selectHomeMenuList());
    }
}
