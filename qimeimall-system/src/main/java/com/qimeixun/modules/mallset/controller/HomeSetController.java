package com.qimeixun.modules.mallset.controller;

import com.qimeixun.modules.mallset.service.HomeSetService;
import com.qimeixun.ro.HomeSetRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/5/1614:01
 */
@Api(tags = "首页装修设置")
@RestController
@RequestMapping(value = "/homeSet", produces = "application/json;charset=UTF-8")
public class HomeSetController {

    @Resource
    HomeSetService homeSetService;

    @CacheEvict(value = "home", allEntries = true)
    @ApiOperation(value = "保存首页装修设置", notes = "保存首页装修设置")
    @RequestMapping(value = "/updateHomeSet", method = RequestMethod.POST)
    public ResponseResultVO updateHomeSet(@RequestBody List<HomeSetRO> homeSetROS) {
        homeSetService.updateHomeSet(homeSetROS);
        return ResponseResultVO.successResult();
    }

    @ApiOperation(value = "查询首页装修设置", notes = "查询首页装修设置")
    @RequestMapping(value = "/selectHomeSetList", method = RequestMethod.GET)
    public ResponseResultVO selectHomeSetList() {
        return ResponseResultVO.successResult(homeSetService.selectHomeSetList());
    }
}
