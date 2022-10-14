package com.qimeixun.modules.customer.controller;

import com.qimeixun.base.BaseController;
import com.qimeixun.entity.CustomerLevel;
import com.qimeixun.modules.customer.service.CustomerLevelService;
import com.qimeixun.ro.CustomerLevelRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/5/109:56
 */
@Api(tags = "会员等级管理")
@RestController
@RequestMapping(value = "/customerLevel", produces = "application/json;charset=UTF-8")
public class CustomerLevelController extends BaseController {

    @Resource
    CustomerLevelService customerLevelService;

    @ApiOperation(value = "查询会员等级列表", notes = "查询会员等级列表")
    @RequestMapping(value = "/selectCustomerLevelList", method = RequestMethod.POST)
    public ResponseResultVO selectCustomerLevelList(@RequestBody CustomerLevelRO customerLevelRO) {
        return getPageObject(customerLevelService.selectCustomerList(customerLevelRO), customerLevelRO);
    }

    @ApiOperation(value = "查询会员等级", notes = "查询会员等级列表")
    @RequestMapping(value = "/selectCustomerLevelById", method = RequestMethod.GET)
    public ResponseResultVO selectCustomerLevelById(@RequestParam String id) {
        return ResponseResultVO.successResult(customerLevelService.selectCustomerLevelById(id));
    }

    @ApiOperation(value = "新增会员等级", notes = "新增会员等级")
    @RequestMapping(value = "/insertCustomerLevel", method = RequestMethod.POST)
    public ResponseResultVO insertCustomerLevel(@RequestBody CustomerLevel customerLevel) {
        return updateResult(customerLevelService.insertCustomerLevel(customerLevel));
    }

    @ApiOperation(value = "修改会员等级", notes = "修改会员等级")
    @RequestMapping(value = "/updateCustomerLevel", method = RequestMethod.POST)
    public ResponseResultVO updateCustomerLevel(@RequestBody CustomerLevel customerLevel) {
        return updateResult(customerLevelService.updateCustomerLevel(customerLevel));
    }

    @ApiOperation(value = "删除会员等级", notes = "删除会员等级")
    @RequestMapping(value = "/deleteCustomerLevel", method = RequestMethod.GET)
    public ResponseResultVO deleteCustomerLevel(@RequestParam String id) {
        return deleteResult(customerLevelService.deleteCustomerLevel(id));
    }

    @ApiOperation(value = "查询能用的会员等级", notes = "查询能用的会员等级")
    @RequestMapping(value = "/selectAllCustomerLevel", method = RequestMethod.GET)
    public ResponseResultVO selectAllCustomerLevel() {
        return ResponseResultVO.successResult(customerLevelService.selectCustomerLevel());
    }
}
