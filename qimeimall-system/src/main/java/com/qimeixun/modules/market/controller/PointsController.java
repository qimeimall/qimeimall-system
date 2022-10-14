package com.qimeixun.modules.market.controller;


import com.qimeixun.base.BaseController;
import com.qimeixun.modules.market.service.PointsService;
import com.qimeixun.ro.PointsRuleRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "积分管理")
@RestController
@RequestMapping(value = "/points", produces = "application/json;charset=UTF-8")
public class PointsController extends BaseController {

    @Resource
    PointsService pointsService;

    @ApiOperation(value = "积分配置列表", notes = "分销记录列表")
    @RequestMapping(value = "/selectPointsRuleList", method = RequestMethod.GET)
    //@LoginCheck
    public ResponseResultVO selectPointsRuleList() {
        return ResponseResultVO.successResult(pointsService.selectPointsRuleList());
    }

    @ApiOperation(value = "签到积分新增", notes = "分销记录列表")
    @RequestMapping(value = "/insertPointsRule", method = RequestMethod.POST)
    //@LoginCheck
    public ResponseResultVO insertPointsRule(@RequestBody PointsRuleRO pointsRuleRO) {
        return insertResult(pointsService.insertPointsRule(pointsRuleRO));
    }

    @ApiOperation(value = "签到积分修改", notes = "分销记录列表")
    @RequestMapping(value = "/updatePointsRule", method = RequestMethod.POST)
    //@LoginCheck
    public ResponseResultVO updatePointsRule(@RequestBody PointsRuleRO pointsRuleRO) {
        return updateResult(pointsService.updatePointsRule(pointsRuleRO));
    }

    @ApiOperation(value = "签到积分删除", notes = "分销记录列表")
    @RequestMapping(value = "/deletePointsRule", method = RequestMethod.GET)
    //@LoginCheck
    public ResponseResultVO deletePointsRule(@RequestParam Long id) {
        return deleteResult(pointsService.deletePointsRule(id));
    }

    @ApiOperation(value = "查询详情", notes = "查询详情")
    @RequestMapping(value = "/selectPointsRulesById", method = RequestMethod.GET)
    //@LoginCheck
    public ResponseResultVO selectPointsRulesById(@RequestParam Long id) {
        return ResponseResultVO.successResult(pointsService.selectPointsRulesById(id));
    }
}
