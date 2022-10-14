package com.qimeixun.modules.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qimeixun.entity.CustomerLevel;
import com.qimeixun.ro.CustomerLevelRO;
import com.qimeixun.vo.ResponseResultVO;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/109:57
 */
public interface CustomerLevelService {
    IPage<CustomerLevel> selectCustomerList(CustomerLevelRO customerLevelRO);

    int insertCustomerLevel(CustomerLevel customerLevel);

    CustomerLevel selectCustomerLevelById(String id);

    int updateCustomerLevel(CustomerLevel customerLevel);

    int deleteCustomerLevel(String id);

    List<CustomerLevel> selectCustomerLevel();
}
