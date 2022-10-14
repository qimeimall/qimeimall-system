package com.qimeixun.modules.store.controller;

import com.qimeixun.base.BaseController;
import com.qimeixun.entity.Store;
import com.qimeixun.modules.store.service.StoreService;
import com.qimeixun.ro.StoreRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/5/414:00
 */
@Api(tags = "门店管理")
@RestController
@RequestMapping(value = "/store", produces = "application/json;charset=UTF-8")
public class StoreController extends BaseController {

    @Resource
    StoreService storeService;

    @ApiOperation(value = "查询门店列表", notes = "查询门店列表")
    @RequestMapping(value = "/selectStoreList", method = RequestMethod.POST)
    public ResponseResultVO selectStoreList(@RequestBody StoreRO storeRO) {
        return getPageObject(storeService.selectStoreList(storeRO), storeRO);
    }

    @ApiOperation(value = "删除门店", notes = "删除门店")
    @RequestMapping(value = "/deleteStore", method = RequestMethod.GET)
    public ResponseResultVO deleteStore(@RequestParam Long id) {
        return deleteResult(storeService.deleteStore(id));
    }

    @ApiOperation(value = "新增门店", notes = "新增门店")
    @RequestMapping(value = "/insertStore", method = RequestMethod.POST)
    public ResponseResultVO insertStore(@RequestBody Store store) {
        return insertResult(storeService.insertStore(store));
    }

    @ApiOperation(value = "通过id查询门店", notes = "查询门店")
    @RequestMapping(value = "/selectStoreById", method = RequestMethod.GET)
    public ResponseResultVO selectStoreById(@RequestParam String id) {
        return ResponseResultVO.successResult(storeService.selectStoreById(id));
    }

    @ApiOperation(value = "编辑门店", notes = "编辑门店")
    @RequestMapping(value = "/updateStore", method = RequestMethod.POST)
    public ResponseResultVO updateStore(@RequestBody Store store) {
        return updateResult(storeService.updateStore(store));
    }
}
