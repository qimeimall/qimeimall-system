package com.qimeixun.modules.pay.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.entity.Customer;
import com.qimeixun.enums.PayType;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.CouponUserMapper;
import com.qimeixun.mapper.CustomerMapper;
import com.qimeixun.mapper.OrderMapper;
import com.qimeixun.mapper.OrderPayMapper;
import com.qimeixun.modules.pay.factory.PayServiceFactory;
import com.qimeixun.modules.pay.service.PayCommonService;
import com.qimeixun.modules.pay.service.PayService;
import com.qimeixun.po.OrderDTO;
import com.qimeixun.po.OrderPayDTO;
import com.qimeixun.po.UserCouponDTO;
import com.qimeixun.ro.PayRO;
import com.qimeixun.vo.ResponseResultVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.rowset.serial.SerialException;

@Service
public class BalancePayServiceImpl implements PayService, InitializingBean {

    @Resource
    OrderMapper orderMapper;

    @Resource
    CustomerMapper customerMapper;

    @Resource
    OrderPayMapper orderPayMapper;

    @Resource
    PayCommonService payCommonService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResultVO pay(OrderPayDTO orderPayDTO, PayRO payRO) {

        //扣减用于余额
        Customer customer = customerMapper.selectById(orderPayDTO.getUserId());
        if(customer.getBalance().compareTo(orderPayDTO.getTotalMoney()) < 1 ){
            //余额小于支付金额
            throw new ServiceException("余额不足");
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStatus("1");
        orderDTO.setIsPay("1");
        orderDTO.setPayType("3"); //1:微信支付  2：支付宝支付 3：余额支付 4:积分支付
        orderMapper.update(orderDTO, new QueryWrapper<OrderDTO>().eq("order_id", orderPayDTO.getOrderId()));

        customer.setBalance(customer.getBalance().subtract(orderPayDTO.getTotalMoney()));
        customerMapper.updateById(customer);

        //修改订单状态
        OrderPayDTO payDTO = new OrderPayDTO();
        payDTO.setPayId(orderPayDTO.getPayId());
        payDTO.setIsPay("1");
        payDTO.setPayType("3");
        orderPayMapper.update(payDTO, new QueryWrapper<OrderPayDTO>().eq("pay_id", orderPayDTO.getPayId()));

        payCommonService.insertUserBill("0", orderPayDTO.getTotalMoney(), orderPayDTO.getUserId());
        return ResponseResultVO.successResult("支付成功");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        PayServiceFactory.register(PayType.BALANCE_PAY.getType(), this);
    }
}
