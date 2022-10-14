package com.qimeixun.modules.pay.service;

import com.qimeixun.po.OrderDTO;
import com.qimeixun.po.OrderPayDTO;
import com.qimeixun.ro.PayRO;
import com.qimeixun.vo.ResponseResultVO;

/**
 * @author chenshouyang
 * @date 2020/5/2715:50
 */
public interface PayService {

    ResponseResultVO pay(OrderPayDTO orderPayDTO, PayRO payRO);
}
