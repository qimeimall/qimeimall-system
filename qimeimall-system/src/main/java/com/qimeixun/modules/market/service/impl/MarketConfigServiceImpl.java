package com.qimeixun.modules.market.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.mapper.WithdrawalMapper;
import com.qimeixun.modules.market.service.MarketConfigService;
import com.qimeixun.ro.RetailRO;
import com.qimeixun.vo.RetailRecordVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MarketConfigServiceImpl implements MarketConfigService {

    @Resource
    WithdrawalMapper withdrawalMapper;

    @Override
    public IPage<RetailRecordVO> retailList(Page page, RetailRO retailRO) {
        return withdrawalMapper.retailList(page, retailRO);
    }
}
