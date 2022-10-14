package com.qimeixun.modules.customer.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.entity.UserAccountRecord;
import com.qimeixun.mapper.UserAccountRecordMapper;
import com.qimeixun.modules.customer.service.CustomerAcountRecordService;
import com.qimeixun.ro.CustomerAcountRecordRO;
import com.qimeixun.vo.CustomerAcountRecordVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/5/1219:35
 */
@Service
public class CustomerAcountRecordServiceImpl implements CustomerAcountRecordService {

    @Resource
    UserAccountRecordMapper userAccountRecordMapper;

    @Override
    public IPage<CustomerAcountRecordVO> selectCustomerAcountRecord(CustomerAcountRecordRO customerLevelRO) {
        Page page = new Page<UserAccountRecord>(customerLevelRO.getCurrentPage(), customerLevelRO.getPageSize());
        IPage<CustomerAcountRecordVO> iPage = userAccountRecordMapper.selectCustomerAcountRecord(page, customerLevelRO);
        return iPage;
    }
}
