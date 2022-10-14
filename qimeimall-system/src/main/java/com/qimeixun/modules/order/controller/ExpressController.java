package com.qimeixun.modules.order.controller;

import com.qimeixun.base.BaseController;
import com.qimeixun.modules.order.service.ExpressService;
import com.qimeixun.po.ExpressDTO;
import com.qimeixun.ro.PageRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/6/315:31
 */
@Api(tags = "快递公司管理")
@RestController
@RequestMapping(value = "/express", produces = "application/json;charset=UTF-8")
public class ExpressController extends BaseController {

    @Resource
    ExpressService expressService;

    @ApiOperation(value = "查询快递公司列表", notes = "查询快递公司列表")
    @RequestMapping(value = "/selectExpressList", method = RequestMethod.POST)
    public ResponseResultVO selectExpressList(@RequestBody(required = false) PageRO pageRO) {
        return ResponseResultVO.successResult(expressService.selectExpressList(pageRO));
    }

    @ApiOperation(value = "新增快递公司", notes = "查询快递公司列表")
    @RequestMapping(value = "/insertExpress", method = RequestMethod.POST)
    public ResponseResultVO insertExpress(@RequestBody ExpressDTO expressDTO) {
        return insertResult(expressService.insertExpress(expressDTO));
    }

    @ApiOperation(value = "修改快递公司列表", notes = "查询快递公司列表")
    @RequestMapping(value = "/updateExpressById", method = RequestMethod.POST)
    public ResponseResultVO updateExpressById(@RequestBody ExpressDTO expressDTO) {
        return updateResult(expressService.updateExpressById(expressDTO));
    }

    @ApiOperation(value = "查询单个快递公司", notes = "查询快递公司列表")
    @RequestMapping(value = "/selectExpressById", method = RequestMethod.GET)
    public ResponseResultVO selectExpressById(@RequestParam String id) {
        return ResponseResultVO.successResult(expressService.selectExpressById(id));
    }

    @ApiOperation(value = "删除单个快递公司", notes = "查询快递公司列表")
    @RequestMapping(value = "/deleteExpressById", method = RequestMethod.GET)
    public ResponseResultVO deleteExpressById(@RequestParam String id) {
        return deleteResult(expressService.deleteExpressById(id));
    }
}
