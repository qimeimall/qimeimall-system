package com.qimeixun.modules.market.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.po.WithdrawalDTO;
import com.qimeixun.ro.RetailRO;
import com.qimeixun.vo.RetailRecordVO;

public interface MarketConfigService {
    IPage<RetailRecordVO> retailList(Page page, RetailRO retailRO);
}
