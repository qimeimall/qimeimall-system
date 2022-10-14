package com.qimeixun.modules.market.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.base.BaseController;
import com.qimeixun.modules.market.service.FinancialService;
import com.qimeixun.po.WithdrawalDTO;
import com.qimeixun.ro.FailWithdrawalRO;
import com.qimeixun.ro.QueryWithdrawalRO;
import com.qimeixun.ro.SeckillConfigRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Api(tags = "财务")
@RestController
@RequestMapping(value = "/financial", produces = "application/json;charset=UTF-8")
public class FinancialController extends BaseController {

    @Resource
    FinancialService financialService;

    @ApiOperation(value = "查询提现申请", notes = "返佣比例设置")
    @RequestMapping(value = "/selectWithdrawalList", method = RequestMethod.POST)
    //@LoginCheck
    public ResponseResultVO selectWithdrawalList(@RequestBody QueryWithdrawalRO queryWithdrawalRO) {
        Page page = new Page<Map<String, Object>>(queryWithdrawalRO.getCurrentPage(), queryWithdrawalRO.getPageSize());
        IPage<WithdrawalDTO> iPage = financialService.selectMyWithdrawalList(page, queryWithdrawalRO);
        return getPageObject(iPage, queryWithdrawalRO);
    }

    @ApiOperation(value = "提现通过", notes = "提现通过")
    @RequestMapping(value = "/updateWithdrawalSuccess", method = RequestMethod.GET)
    //@LoginCheck
    public ResponseResultVO updateWithdrawalSuccess(@RequestParam String id) {
        return updateResult(financialService.updateWithdrawalSuccess(id));
    }

    @ApiOperation(value = "提现拒绝", notes = "提现拒绝")
    @RequestMapping(value = "/updateWithdrawalFail", method = RequestMethod.POST)
    //@LoginCheck
    public ResponseResultVO updateWithdrawalFail(@RequestBody FailWithdrawalRO failWithdrawalRO) {
        return updateResult(financialService.updateWithdrawalFail(failWithdrawalRO));
    }
}
