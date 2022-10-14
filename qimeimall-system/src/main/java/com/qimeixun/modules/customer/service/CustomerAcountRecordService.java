package com.qimeixun.modules.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qimeixun.ro.CustomerAcountRecordRO;
import com.qimeixun.vo.CustomerAcountRecordVO;
import com.qimeixun.vo.ResponseResultVO;

/**
 * @author chenshouyang
 * @date 2020/5/1219:34
 */
public interface CustomerAcountRecordService {
    IPage<CustomerAcountRecordVO> selectCustomerAcountRecord(CustomerAcountRecordRO customerLevelRO);
}
