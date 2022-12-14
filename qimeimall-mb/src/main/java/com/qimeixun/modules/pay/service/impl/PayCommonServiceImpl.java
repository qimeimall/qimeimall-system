package com.qimeixun.modules.pay.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.qimeixun.entity.Customer;
import com.qimeixun.enums.PayType;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.CouponUserMapper;
import com.qimeixun.mapper.CustomerMapper;
import com.qimeixun.mapper.OrderMapper;
import com.qimeixun.mapper.OrderPayMapper;
import com.qimeixun.modules.config.WxPayConfiguration;
import com.qimeixun.modules.pay.factory.PayServiceFactory;
import com.qimeixun.modules.pay.service.PayCommonService;
import com.qimeixun.modules.user.service.UserBillService;
import com.qimeixun.po.OrderDTO;
import com.qimeixun.po.OrderPayDTO;
import com.qimeixun.po.UserBillDTO;
import com.qimeixun.po.UserCouponDTO;
import com.qimeixun.ro.PayRO;
import com.qimeixun.util.DateUtil;
import com.qimeixun.util.IdUtils;
import com.qimeixun.util.TokenUtil;
import com.qimeixun.vo.ResponseResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
@Slf4j
public class PayCommonServiceImpl implements PayCommonService {

    @Resource
    OrderMapper orderMapper;

    @Resource
    OrderPayMapper orderPayMapper;

    @Resource
    TokenUtil tokenUtil;

    @Resource
    CustomerMapper customerMapper;

    @Resource
    UserBillService userBillService;

    @Resource
    CouponUserMapper couponUserMapper;


    @Override
    public ResponseResultVO orderPay(PayRO payRO) {
        BigDecimal payMoney = new BigDecimal(0);
        OrderDTO dto = new OrderDTO();

        if ("1".equals(payRO.getApplication())) {
            if (StrUtil.isBlank(payRO.getRechargeMoney())) {
                throw new ServiceException("?????????????????????");
            }
            try {
                payMoney = new BigDecimal(payRO.getRechargeMoney());
            } catch (Exception exception) {
                throw new ServiceException("???????????????????????????");
            }

        } else {
            QueryWrapper<OrderDTO> queryWrapper = new QueryWrapper<>();
            if (StrUtil.isBlank(payRO.getOrderId())) {
                throw new ServiceException("??????????????????");
            }
            queryWrapper.eq("order_id", payRO.getOrderId());
            dto = orderMapper.selectOne(queryWrapper);
            payMoney = dto.getPayMoney();
        }

        //outTradeNo = orderId;

        OrderPayDTO payDTO = new OrderPayDTO();
        payDTO.setPayId(IdUtils.getId());
        payDTO.setPayType("1");
        payDTO.setTotalMoney(payMoney);
        payDTO.setPayTime(new Date());
        if ("1".equals(payRO.getApplication())) {
            payDTO.setApplication("1"); //??????
        } else {
            payDTO.setApplication("0"); //????????????
            payDTO.setOrderId(payRO.getOrderId());
            payDTO.setPlatformCouponId(dto.getCouponId());
        }

        payDTO.setUserId(Long.parseLong(tokenUtil.getUserIdByToken()));
        orderPayMapper.insert(payDTO);


        if (payMoney.compareTo(new BigDecimal(0)) != 1) {
            payRO.setPayType(PayType.BALANCE_PAY.getType()); // ???????????????0?????????????????????
        }

        if ("1".equals(dto.getIsPoints())) {
            //?????????????????????
            payRO.setPayType(PayType.POINTS_PAY.getType());
        } else {
            if (PayType.POINTS_PAY.getType().equals(payRO.getPayType())) {
                throw new ServiceException("???????????????????????????????????????");
            }
        }

        return PayServiceFactory.getStrategy(payRO.getPayType()).pay(payDTO, payRO);
    }

    @Override
    public String wxPayNotify(String data) {
        try {
            log.info("??????================================??????");
            WxPayService wxPayService = WxPayConfiguration.getWxAppPayService();
            WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(data);
            log.info(JSON.toJSONString(notifyResult));
            String payId = notifyResult.getOutTradeNo();
            Date payTime = new Date();
            try {
                payTime = DateUtil.parseStrToDate(notifyResult.getTimeEnd(), DateUtil.DATE_TIME_FORMAT_YYYYMMDDHHMISS);
            } catch (Exception exception) {
                log.error("?????????????????????");
            }
            if (updateOrderPayStatus(payId, payTime)) return WxPayNotifyResponse.success("????????????!");
            return WxPayNotifyResponse.success("????????????!");
        } catch (Exception exception) {
            log.error("????????????", exception);
            return WxPayNotifyResponse.fail(exception.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderPayStatus(String payId, Date payTime) {
        OrderPayDTO orderPayDTO = orderPayMapper.selectOne(new QueryWrapper<OrderPayDTO>().eq("pay_id", payId).last("limit 1"));
        if ("0".equals(orderPayDTO.getIsPay())) {
            if ("0".equals(orderPayDTO.getApplication())) {
                //?????????????????????
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setStatus("1");
                orderMapper.update(orderDTO, new QueryWrapper<OrderDTO>().eq("order_id", orderPayDTO.getOrderId()));

            } else if ("1".equals(orderPayDTO.getApplication())) {
                //??????
                Customer customer = customerMapper.selectById(orderPayDTO.getId());
                customer.setBalance(customer.getBalance().add(orderPayDTO.getTotalMoney()));
                customerMapper.updateById(customer);
                //????????????????????????
                insertUserBill(orderPayDTO.getApplication(), orderPayDTO.getTotalMoney(), orderPayDTO.getUserId());
            }
            orderPayDTO.setIsPay("1");
            orderPayMapper.updateById(orderPayDTO);

            //????????????
        }

        return true;
    }

    @Override
    public void insertUserBill(String application, BigDecimal money, Long userId) {
        UserBillDTO userBillDTO = new UserBillDTO();
        String type = "1".equals(application) ? "2" : "0";
        String remark = "1".equals(application) ? "????????????" : "????????????";
        userBillDTO.setType(type);
        userBillDTO.setMoney(money);
        userBillDTO.setUserId(userId);
        userBillDTO.setRemark(remark);
        userBillDTO.setCreateTime(new Date());
        userBillService.insertUserBill(userBillDTO);
    }
}
