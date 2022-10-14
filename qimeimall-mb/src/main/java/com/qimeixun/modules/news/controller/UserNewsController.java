package com.qimeixun.modules.news.controller;

import com.qimeixun.annotation.JwtIgnore;
import com.qimeixun.modules.news.service.UserNewsService;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "新闻")
@RestController
@RequestMapping(value = "/news", produces = "application/json;charset=UTF-8")
public class UserNewsController {

    @Resource
    UserNewsService userNewsService;

    @ApiOperation(value = "查看新闻列表", notes = "查看新闻列表")
    @RequestMapping(value = "/selectNewsList", method = RequestMethod.GET)
    @JwtIgnore
    public ResponseResultVO selectNewsList() {
        return ResponseResultVO.successResult(userNewsService.selectNewsList());
    }

    @ApiOperation(value = "查看单个新闻", notes = "查看单个新闻")
    @RequestMapping(value = "/selectNewsById", method = RequestMethod.GET)
    @JwtIgnore
    public ResponseResultVO selectNewsById(@RequestParam String id) {
        return ResponseResultVO.successResult(userNewsService.selectNewsById(id));
    }
}
