package com.qimeixun.modules.pay.service;

import com.qimeixun.po.OrderPayDTO;
import com.qimeixun.ro.PayRO;
import com.qimeixun.vo.ResponseResultVO;

import java.math.BigDecimal;

public interface PayCommonService {
    ResponseResultVO orderPay(PayRO payRO);

    String wxPayNotify(String data);

    void insertUserBill(String application, BigDecimal money, Long userId);
}
