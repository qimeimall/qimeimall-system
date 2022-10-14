package com.qimeixun.modules.market.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.base.BaseController;
import com.qimeixun.modules.market.service.MarketConfigService;
import com.qimeixun.po.WithdrawalDTO;
import com.qimeixun.ro.RetailRO;
import com.qimeixun.vo.ResponseResultVO;
import com.qimeixun.vo.RetailRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/6/1922:28
 */
@Api(tags = "营销管理")
@RestController
@RequestMapping(value = "/retail", produces = "application/json;charset=UTF-8")
public class MarketConfigController extends BaseController {

    @Resource
    MarketConfigService marketConfigService;

    @ApiOperation(value = "分销记录列表", notes = "分销记录列表")
    @RequestMapping(value = "/retailList", method = RequestMethod.POST)
    //@LoginCheck
    public ResponseResultVO retailList(@RequestBody RetailRO retailRO) {
        Page page = new Page<Map<String, Object>>(retailRO.getCurrentPage(), retailRO.getPageSize());
        IPage<RetailRecordVO> iPage = marketConfigService.retailList(page, retailRO);
        return getPageObject(iPage, retailRO);
    }

}
