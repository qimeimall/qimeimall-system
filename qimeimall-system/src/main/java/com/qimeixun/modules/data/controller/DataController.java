package com.qimeixun.modules.data.controller;


import com.qimeixun.modules.data.service.DataService;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "数据分析")
@RestController
@RequestMapping(value = "/data", produces = "application/json;charset=UTF-8")
public class DataController {

    @Resource
    DataService dataService;

    @ApiOperation(value = "数据分析", notes = "数据分析")
    @RequestMapping(value = "/selectData", method = RequestMethod.GET)
    public ResponseResultVO selectData() {
        return ResponseResultVO.successResult(dataService.selectData());
    }

    @ApiOperation(value = "数据分析 查询图表", notes = "数据分析 查询图表")
    @RequestMapping(value = "/selectChart", method = RequestMethod.GET)
    public ResponseResultVO selectChart() {
        return ResponseResultVO.successResult(dataService.selectChart());
    }
}
