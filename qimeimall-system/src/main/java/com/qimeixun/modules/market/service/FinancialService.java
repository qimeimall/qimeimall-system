package com.qimeixun.modules.market.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.po.WithdrawalDTO;
import com.qimeixun.ro.FailWithdrawalRO;
import com.qimeixun.ro.QueryWithdrawalRO;

public interface FinancialService {

    /**
     * 提现申请
     * @param page
     * @param queryWithdrawalRO
     * @return
     */
    IPage<WithdrawalDTO> selectMyWithdrawalList(Page page, QueryWithdrawalRO queryWithdrawalRO);

    int updateWithdrawalSuccess(String id);

    int updateWithdrawalFail(FailWithdrawalRO failWithdrawalRO);
}
