package com.qimeixun.modules.mallset.controller;

import com.qimeixun.base.BaseController;
import com.qimeixun.modules.mallset.service.MyServiceService;
import com.qimeixun.modules.mallset.service.MyServiceService;
import com.qimeixun.po.MyServiceDTO;
import com.qimeixun.ro.PageRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenshouyang
 * @date 2020/7/411:02
 */
@Api(tags = "个人中心服务")
@RestController
@RequestMapping(value = "/myservice", produces = "application/json;charset=UTF-8")
public class MyServiceController extends BaseController {

    @Resource
    MyServiceService myServiceService;

    @ApiOperation(value = "查询我的服务 list", notes = "查询我的服务 list")
    @RequestMapping(value = "/selectMyServiceList", method = RequestMethod.POST)
    public ResponseResultVO selectMyServiceList(@RequestBody PageRO pageRO) {
        return getPageObject(myServiceService.selectMyServiceList(pageRO), pageRO);
    }

    @ApiOperation(value = "删除我的服务", notes = "删除我的服务")
    @RequestMapping(value = "/deleteMyService", method = RequestMethod.GET)
    public ResponseResultVO deleteMyServiceById(@RequestParam String id) {
        return deleteResult(myServiceService.deleteMyServiceById(id));
    }

    @ApiOperation(value = "修改我的服务", notes = "修改我的服务")
    @RequestMapping(value = "/updateMyService", method = RequestMethod.POST)
    public ResponseResultVO updateMyService(@RequestBody MyServiceDTO myServiceDTO) {
        return updateResult(myServiceService.updateMyService(myServiceDTO));
    }

    @ApiOperation(value = "新增我的服务", notes = "新增我的服务")
    @RequestMapping(value = "/insertMyService", method = RequestMethod.POST)
    public ResponseResultVO insertMyService(@RequestBody MyServiceDTO myServiceDTO) {
        if (StringUtils.isEmpty(myServiceDTO.getImgUrl())) {
            return ResponseResultVO.failResult("图片不能为空");
        }
        return insertResult(myServiceService.insertMyService(myServiceDTO));
    }

    @ApiOperation(value = "查询我的服务通过id", notes = "查询我的服务通过id")
    @RequestMapping(value = "/selectMyServiceById", method = RequestMethod.GET)
    public ResponseResultVO selectMyServiceById(@RequestParam String id) {
        return ResponseResultVO.successResult(myServiceService.selectMyServiceById(id));
    }
}
