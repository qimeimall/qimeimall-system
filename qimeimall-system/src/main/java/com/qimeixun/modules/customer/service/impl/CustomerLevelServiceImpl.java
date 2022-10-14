package com.qimeixun.modules.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.entity.CustomerLevel;
import com.qimeixun.mapper.CustomerLevelMapper;
import com.qimeixun.modules.customer.service.CustomerLevelService;
import com.qimeixun.ro.CustomerLevelRO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/109:57
 */
@Service
public class CustomerLevelServiceImpl implements CustomerLevelService {

    @Resource
    CustomerLevelMapper customerLevelMapper;

    @Override
    public IPage<CustomerLevel> selectCustomerList(CustomerLevelRO customerLevelRO) {
        QueryWrapper<CustomerLevel> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(customerLevelRO.getLevelName())) {
            wrapper.like("level_name", customerLevelRO.getLevelName());
        }
        wrapper.orderByDesc("create_time");
        IPage<CustomerLevel> page = customerLevelMapper.selectPage(new Page<>(customerLevelRO.getCurrentPage(), customerLevelRO.getPageSize()),
                wrapper);
        return page;
    }

    @Override
    public int insertCustomerLevel(CustomerLevel customerLevel) {
        return customerLevelMapper.insert(customerLevel);
    }

    @Override
    public CustomerLevel selectCustomerLevelById(String id) {
        return customerLevelMapper.selectById(id);
    }

    @Override
    public int updateCustomerLevel(CustomerLevel customerLevel) {
        return customerLevelMapper.updateById(customerLevel);
    }

    @Override
    public int deleteCustomerLevel(String id) {
        return customerLevelMapper.deleteById(id);
    }

    @Override
    public List<CustomerLevel> selectCustomerLevel() {
        QueryWrapper<CustomerLevel> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "0");
        wrapper.orderByDesc("level");
        return customerLevelMapper.selectList(wrapper);
    }
}
