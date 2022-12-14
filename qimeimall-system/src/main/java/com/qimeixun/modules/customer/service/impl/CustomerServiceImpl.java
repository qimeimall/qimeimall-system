package com.qimeixun.modules.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.constant.SystemConstant;
import com.qimeixun.entity.Customer;
import com.qimeixun.entity.CustomerLevel;
import com.qimeixun.entity.UserAccountRecord;
import com.qimeixun.enums.PointsBillEnum;
import com.qimeixun.enums.UserBillEnum;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.*;
import com.qimeixun.modules.customer.service.CustomerService;
import com.qimeixun.modules.system.service.impl.CommonService;
import com.qimeixun.po.UserBillDTO;
import com.qimeixun.po.UserPointsBillDTO;
import com.qimeixun.ro.CustomerMoneyRO;
import com.qimeixun.ro.CustomerRO;
import com.qimeixun.util.DateUtil;
import com.qimeixun.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/5/517:10
 */
@Service
public class CustomerServiceImpl extends CommonService implements CustomerService {

    @Resource
    CustomerMapper customerMapper;

    @Resource
    CustomerLevelMapper customerLevelMapper;

    @Resource
    UserAccountRecordMapper userAccountRecordMapper;

    @Resource
    UserBillMapper userBillMapper;

    @Resource
    UserPointsBillMapper userPointsBillMapper;

    @Resource
    TokenUtil tokenUtil;

    @Override
    public IPage<Customer> selectCustomerList(CustomerRO customerRO) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(customerRO.getNickName())) {
            wrapper.like("nick_name", customerRO.getNickName());
        }
        if (StringUtils.isNotEmpty(customerRO.getPhone())) {
            wrapper.like("phone", customerRO.getPhone());
        }
        wrapper.orderByDesc("create_time");

        IPage<Customer> page = customerMapper.selectPage(new Page<>(customerRO.getCurrentPage(), customerRO.getPageSize()), wrapper);
        return page;
    }

    @Override
    public Customer selectCustomerById(String id) {
        Customer customer = customerMapper.selectById(id);
        return customer;
    }

    @Override
    public int updateCustomer(Customer customer) {
        Customer cus = customerMapper.selectById(customer.getId());
        if (customer.getLevelId() != null && customer.getLevelId() != 0 && cus.getLevelId() != customer.getLevelId()) {
            //?????????????????????
            CustomerLevel customerLevel = customerLevelMapper.selectById(customer.getLevelId());
            Date date = new Date();
            customer.setLevelStartTime(date);
            if (customerLevel.getValidity() != 0) {
                customer.setLevelEndTime(DateUtil.addDateDay(date, customerLevel.getValidity()));
            } else {
                //?????????0 ???????????? ????????????100???????????????
                customer.setLevelEndTime(DateUtil.addDateYear(date, 100));
            }
        }
        return customerMapper.updateById(customer);
    }

    @Override
    @Transactional
    public int updateCustomerBlanceOrPoints(CustomerMoneyRO customerMoneyRO) {
        //??????????????????
        Customer customer = customerMapper.selectById(customerMoneyRO.getId());
        if (SystemConstant.USER_CHARGE_MONEY.equals(customerMoneyRO.getType())) {
            //????????????
            if (SystemConstant.USER_CHARGE_ADD.equals(customerMoneyRO.getWay())) {
                customer.setBalance(customer.getBalance().add(customerMoneyRO.getCount()));

                //??????????????????
                UserBillDTO userBillDTO = new UserBillDTO();
                userBillDTO.setType("3");
                userBillDTO.setRemark("????????????");
                userBillDTO.setMoney(customerMoneyRO.getCount());
                userBillDTO.setUserId(customerMoneyRO.getId());
                userBillDTO.setCreateTime(new Date());
                userBillMapper.insert(userBillDTO);
            } else {
                //??????
//                if (customerMoneyRO.getCount() > customer.getBalance()) {
//                    throw new ServiceException("????????????");
//                } else {
//                    customer.setBalance(customer.getBalance() - customerMoneyRO.getCount());
//                }
                throw new ServiceException("??????????????????????????????");
            }
        }
        if (SystemConstant.USER_CHARGE_POINTS.equals(customerMoneyRO.getType())) {

            UserPointsBillDTO pointsBillDTO = new UserPointsBillDTO();

            //????????????
            if (SystemConstant.USER_CHARGE_ADD.equals(customerMoneyRO.getWay())) {
                customer.setPoints(customer.getPoints().add(customerMoneyRO.getCount()));

                pointsBillDTO.setType(PointsBillEnum.TYPE_SYSTEM_ADD.getType());
                pointsBillDTO.setRemark(PointsBillEnum.TYPE_SYSTEM_ADD.getRemark());
            } else {

                pointsBillDTO.setType(PointsBillEnum.TYPE_SYSTEM_SUB.getType());
                pointsBillDTO.setRemark(PointsBillEnum.TYPE_SYSTEM_SUB.getRemark());
                //??????
                if (customerMoneyRO.getCount().compareTo(customer.getPoints()) == 1) {
                    throw new ServiceException("????????????");
                } else {
                    customer.setPoints(customer.getPoints().subtract(customerMoneyRO.getCount()));
                }
            }

            //????????????????????????
            pointsBillDTO.setUserId(customerMoneyRO.getId());
            pointsBillDTO.setMoney(customerMoneyRO.getCount());
            userPointsBillMapper.insert(pointsBillDTO);
        }

        //??????????????????
        UserAccountRecord userAccountRecord = getUserAccountRecord(customerMoneyRO.getCount(), customerMoneyRO.getType(), customerMoneyRO.getWay(),
                customer.getId(), tokenUtil.getUserIdByToken());
        userAccountRecordMapper.insert(userAccountRecord);
        return customerMapper.updateById(customer);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserMoney(BigDecimal refundMoney, Long userId, UserBillEnum typeRefund) {
        UserBillDTO userBillDTO = new UserBillDTO();
        userBillDTO.setType(typeRefund.getType());
        userBillDTO.setRemark(typeRefund.getRemark());
        userBillDTO.setMoney(refundMoney);
        userBillDTO.setUserId(userId);
        userBillDTO.setCreateTime(new Date());
        userBillMapper.insert(userBillDTO);

        Customer customer = customerMapper.selectById(userId);
        customer.setBalance(customer.getBalance().add(refundMoney));
        customerMapper.updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserPoints(BigDecimal refundMoney, Long userId, PointsBillEnum typeRefund) {
        UserPointsBillDTO pointsBillDTO = new UserPointsBillDTO();
        pointsBillDTO.setType(typeRefund.getType());
        pointsBillDTO.setRemark(typeRefund.getRemark());
        //????????????????????????
        pointsBillDTO.setUserId(userId);
        pointsBillDTO.setMoney(refundMoney);
        userPointsBillMapper.insert(pointsBillDTO);

        Customer customer = customerMapper.selectById(userId);
        customer.setPoints(customer.getPoints().add(refundMoney));
        customerMapper.updateById(customer);
    }

    /**
     * ??????????????????
     *
     * @param count     ??????
     * @param type      ??????
     * @param way       ??????
     * @param userId    ??????id
     * @param sysUserId ?????????id
     * @return
     */
    private UserAccountRecord getUserAccountRecord(BigDecimal count, String type, String way, Long userId, String sysUserId) {
        UserAccountRecord userAccountRecord = new UserAccountRecord();
        userAccountRecord.setCount(count);
        userAccountRecord.setUserId(userId);
        userAccountRecord.setSysUserId(sysUserId);
        userAccountRecord.setType(type);
        userAccountRecord.setWay(way);
        return userAccountRecord;
    }
}
