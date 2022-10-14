package com.qimeixun.modules.customer.controller;

import com.qimeixun.base.BaseController;
import com.qimeixun.entity.Customer;
import com.qimeixun.modules.customer.service.CustomerService;
import com.qimeixun.ro.CustomerMoneyRO;
import com.qimeixun.ro.CustomerRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author chenshouyang
 * @date 2020/5/517:08
 */
@Api(tags = "会员管理")
@RestController
@RequestMapping(value = "/customer", produces = "application/json;charset=UTF-8")
public class CustomerController extends BaseController {

    @Resource
    CustomerService customerService;

    @ApiOperation(value = "查询用户列表", notes = "查询用户列表")
    @RequestMapping(value = "/selectCustomerList", method = RequestMethod.POST)
    public ResponseResultVO selectCustomerList(@RequestBody CustomerRO customerRO) {
        return getPageObject(customerService.selectCustomerList(customerRO), customerRO);
    }

    @ApiOperation(value = "通过id查询用户", notes = "通过id查询用户")
    @RequestMapping(value = "/selectCustomerById", method = RequestMethod.GET)
    public ResponseResultVO selectCustomerById(@RequestParam String id) {
        return ResponseResultVO.successResult(customerService.selectCustomerById(id));
    }

    @ApiOperation(value = "更新用户信息", notes = "通过id查询用户")
    @RequestMapping(value = "/updateCustomer", method = RequestMethod.POST)
    public ResponseResultVO updateCustomer(@RequestBody Customer customer) {
        return updateResult(customerService.updateCustomer(customer));
    }

    @ApiOperation(value = "后台用户充值", notes = "通过id查询用户")
    @RequestMapping(value = "/updateCustomerBlanceOrPoints", method = RequestMethod.POST)
    public ResponseResultVO updateCustomerBlanceOrPoints(@RequestBody CustomerMoneyRO customerMoneyRO) {
        if (customerMoneyRO.getCount().compareTo(new BigDecimal(0)) == -1) {
            return ResponseResultVO.failResult("充值不能为负数");
        }
        return updateResult(customerService.updateCustomerBlanceOrPoints(customerMoneyRO));
    }
}
