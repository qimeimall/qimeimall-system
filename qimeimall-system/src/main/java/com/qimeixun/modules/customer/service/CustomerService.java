package com.qimeixun.modules.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qimeixun.entity.Customer;
import com.qimeixun.enums.PointsBillEnum;
import com.qimeixun.enums.UserBillEnum;
import com.qimeixun.ro.CustomerMoneyRO;
import com.qimeixun.ro.CustomerRO;
import com.qimeixun.vo.ResponseResultVO;

import java.math.BigDecimal;

/**
 * @author chenshouyang
 * @date 2020/5/517:09
 */
public interface CustomerService {
    IPage<Customer> selectCustomerList(CustomerRO customerRO);

    Customer selectCustomerById(String id);

    int updateCustomer(Customer customer);

    int updateCustomerBlanceOrPoints(CustomerMoneyRO customerMoneyRO);

    void addUserMoney(BigDecimal refundMoney, Long userId, UserBillEnum typeRefund);

    void addUserPoints(BigDecimal refundMoney, Long userId, PointsBillEnum typeRefund);
}
