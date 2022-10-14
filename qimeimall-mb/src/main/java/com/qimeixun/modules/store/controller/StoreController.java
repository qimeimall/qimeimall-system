package com.qimeixun.modules.store.controller;

import com.qimeixun.modules.store.service.StoreService;
import com.qimeixun.ro.NearStoreRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

/**
 * @author chenshouyang
 * @date 2020/6/1110:31
 */
@Api(tags = "店铺")
@RestController
@RequestMapping(value = "/store", produces = "application/json;charset=UTF-8")
public class StoreController {

    @Resource
    StoreService storeService;

    @ApiOperation(value = "通过id获取店铺信息", notes = "通过id获取店铺信息")
    @RequestMapping(value = "/selectStoreInfoById", method = RequestMethod.GET)
    public ResponseResultVO selectStoreInfoById(@RequestParam String storeId){
        return ResponseResultVO.successResult(storeService.selectStoreInfoById(storeId));
    }

    @ApiOperation(value = "查询附近的门店", notes = "查询附近的门店")
    @RequestMapping(value = "/selectNearStoreList", method = RequestMethod.POST)
    public ResponseResultVO selectNearStoreList(@RequestBody NearStoreRO nearStoreRO){
        return ResponseResultVO.successResult(storeService.selectNearStoreList(nearStoreRO));
    }

}
