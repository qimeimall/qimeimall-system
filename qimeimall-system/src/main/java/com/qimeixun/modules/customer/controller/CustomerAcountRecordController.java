package com.qimeixun.modules.customer.controller;

import com.qimeixun.base.BaseController;
import com.qimeixun.modules.customer.service.CustomerAcountRecordService;
import com.qimeixun.ro.CustomerAcountRecordRO;
import com.qimeixun.ro.CustomerLevelRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenshouyang
 * @date 2020/5/1219:32
 */
@Api(tags = "余额积分记录列表")
@RestController
@RequestMapping(value = "/customerAcountRecordController", produces = "application/json;charset=UTF-8")
public class CustomerAcountRecordController extends BaseController {

    @Resource
    CustomerAcountRecordService customerAcountRecordService;

    @ApiOperation(value = "查询会员等级列表", notes = "查询会员等级列表")
    @RequestMapping(value = "/selectCustomerAcountRecord", method = RequestMethod.POST)
    public ResponseResultVO selectCustomerAcountRecord(@RequestBody CustomerAcountRecordRO customerLevelRO) {
        if (StringUtils.isEmpty(customerLevelRO.getType())) {
            return ResponseResultVO.failResult("type能为空");
        }
        return getPageObject(customerAcountRecordService.selectCustomerAcountRecord(customerLevelRO), customerLevelRO);
    }
}
