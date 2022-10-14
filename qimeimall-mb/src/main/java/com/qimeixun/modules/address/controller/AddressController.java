package com.qimeixun.modules.address.controller;

import com.qimeixun.annotation.LoginCheck;
import com.qimeixun.constant.SystemConstant;
import com.qimeixun.modules.address.service.AddressService;
import com.qimeixun.ro.AddressRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenshouyang
 * @date 2020/5/2515:03
 */
@Api(tags = "发货地址")
@RestController
@RequestMapping(value = "/address", produces = "application/json;charset=UTF-8")
public class AddressController {

    @Resource
    AddressService addressService;

    @ApiOperation(value = "新增发货地址", notes = "新增发货地址")
    @RequestMapping(value = "/insertAddress", method = RequestMethod.POST)
    public ResponseResultVO insertAddress(@RequestBody AddressRO addressRO) {
        if (StringUtils.isEmpty(addressRO.getName())) {
            return ResponseResultVO.failResult("名字不能为空");
        }
        if (StringUtils.isEmpty(addressRO.getPhone())) {
            return ResponseResultVO.failResult("联系方式不能为空");
        }
        if (StringUtils.isEmpty(addressRO.getAddress())) {
            return ResponseResultVO.failResult("地址不能为空");
        }
        int i = addressService.insertAddress(addressRO);
        if (i > 0) {
            return ResponseResultVO.successResult();
        } else {
            return ResponseResultVO.failResult();
        }
    }

    @ApiOperation(value = "查询用户的收货地址列表", notes = "查询用户的收货地址列表")
    @RequestMapping(value = "/selectAddressList", method = RequestMethod.GET)
    public ResponseResultVO selectAddressList() {
        return ResponseResultVO.successResult(addressService.selectAddressList());
    }

    @ApiOperation(value = "通过id查询收货地址", notes = "通过id查询收货地址")
    @RequestMapping(value = "/selectAddressById", method = RequestMethod.GET)
    public ResponseResultVO selectAddressById(@RequestParam String id) {
        return ResponseResultVO.successResult(addressService.selectAddressById(id));
    }

    @ApiOperation(value = "修改收货地址", notes = "修改收货地址")
    @RequestMapping(value = "/updateAddress", method = RequestMethod.POST)
    public ResponseResultVO updateAddress(@RequestBody AddressRO addressRO) {
        if (addressRO.getId() == null) {
            return ResponseResultVO.failResult("参数错误");
        }
        if (addressService.updateAddress(addressRO) > 0) {
            return ResponseResultVO.successResult(SystemConstant.successMessage);
        } else {
            return ResponseResultVO.failResult();
        }
    }

    @ApiOperation(value = "删除收货地址", notes = "删除收货地址")
    @RequestMapping(value = "/deleteAddressById", method = RequestMethod.GET)
    public ResponseResultVO deleteAddressById(@RequestParam String id) {
        if (addressService.deleteAddressById(id) > 0) {
            return ResponseResultVO.successResult();
        } else {
            return ResponseResultVO.failResult();
        }
    }
}
