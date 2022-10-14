package com.qimeixun.modules.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.entity.Customer;
import com.qimeixun.enums.PayType;
import com.qimeixun.enums.PointsBillEnum;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.CustomerMapper;
import com.qimeixun.mapper.OrderMapper;
import com.qimeixun.mapper.OrderPayMapper;
import com.qimeixun.mapper.UserPointsBillMapper;
import com.qimeixun.modules.pay.factory.PayServiceFactory;
import com.qimeixun.modules.pay.service.PayCommonService;
import com.qimeixun.modules.pay.service.PayService;
import com.qimeixun.po.OrderDTO;
import com.qimeixun.po.OrderPayDTO;
import com.qimeixun.po.UserPointsBillDTO;
import com.qimeixun.ro.PayRO;
import com.qimeixun.vo.ResponseResultVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PointsPayServiceImpl  implements PayService, InitializingBean {

    @Resource
    OrderMapper orderMapper;

    @Resource
    CustomerMapper customerMapper;

    @Resource
    OrderPayMapper orderPayMapper;
    @Resource
    UserPointsBillMapper userPointsBillMapper;

    @Override
    public ResponseResultVO pay(OrderPayDTO orderPayDTO, PayRO payRO) {
        //扣减用于积分
        Customer customer = customerMapper.selectById(orderPayDTO.getUserId());
        if(customer.getPoints().compareTo(orderPayDTO.getTotalMoney()) < 1 ){
            //余额小于支付金额
            throw new ServiceException("积分不足");
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStatus("1");
        orderDTO.setPayType("4");
        orderMapper.update(orderDTO, new QueryWrapper<OrderDTO>().eq("order_id", orderPayDTO.getOrderId()));

        customer.setPoints(customer.getBalance().subtract(orderPayDTO.getTotalMoney()));
        customerMapper.updateById(customer);

        //修改订单状态
        OrderPayDTO payDTO = new OrderPayDTO();
        payDTO.setPayId(orderPayDTO.getPayId());
        payDTO.setIsPay("1");
        orderDTO.setPayType("4");
        orderPayMapper.update(payDTO, new QueryWrapper<OrderPayDTO>().eq("pay_id", orderPayDTO.getPayId()));

        //增加积分账单记录
        UserPointsBillDTO pointsBillDTO = new UserPointsBillDTO();
        pointsBillDTO.setUserId(Long.valueOf(orderPayDTO.getUserId()));
        pointsBillDTO.setMoney(orderPayDTO.getTotalMoney());
        pointsBillDTO.setType(PointsBillEnum.TYPE_EXCHANGE.getType());
        pointsBillDTO.setRemark(PointsBillEnum.TYPE_EXCHANGE.getRemark());
        userPointsBillMapper.insert(pointsBillDTO);

        return ResponseResultVO.successResult("支付成功");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        PayServiceFactory.register(PayType.POINTS_PAY.getType(), this);
    }
}
