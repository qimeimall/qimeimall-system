package com.qimeixun.modules.pay.service.impl;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.qimeixun.enums.BillDetailEnum;
import com.qimeixun.enums.PayType;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.modules.config.WxPayConfiguration;
import com.qimeixun.modules.pay.factory.PayServiceFactory;
import com.qimeixun.modules.pay.service.PayService;
import com.qimeixun.po.OrderPayDTO;
import com.qimeixun.ro.PayRO;
import com.qimeixun.vo.ResponseResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WxMiniPayServiceImpl implements PayService, InitializingBean {

    @Override
    public ResponseResultVO pay(OrderPayDTO orderPayDTO, PayRO payRO) {
        WxPayMpOrderResult wxPayMpOrderResult;
        try {
            wxPayMpOrderResult = wxPay(orderPayDTO, payRO.getCode());
        } catch (WxPayException e) {
            throw new ServiceException("支付失败");
        }
        return ResponseResultVO.successResult(wxPayMpOrderResult);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        PayServiceFactory.register(PayType.WX_PAY.getType(), this);
    }

    private WxPayMpOrderResult wxPay(OrderPayDTO orderPayDTO, String code) throws WxPayException {

        WxPayService wxPayService = WxPayConfiguration.getWxAppPayService();
        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();

        orderRequest.setTradeType("JSAPI");
        orderRequest.setOpenid(WxPayConfiguration.getMiniOpenId(code)); //支付的时候传code
        orderRequest.setBody("订单支付");
        orderRequest.setOutTradeNo(orderPayDTO.getPayId());
        orderRequest.setTotalFee(1);
        //orderRequest.setTotalFee(new BigDecimal(100).multiply(payOrderDTO.getTotalPayMoney()).intValue());
        //测试设置支付一分钱
        //orderRequest.setTotalFee(100);
        orderRequest.setAttach(orderPayDTO.getApplication());
        orderRequest.setSpbillCreateIp("127.0.0.1");
        orderRequest.setNotifyUrl(WxPayConfiguration.getApiUrl() + "/pay/wx/getNotifyInfo");
        orderRequest.setAttach(BillDetailEnum.TYPE_3.getValue());

        WxPayMpOrderResult orderResult = wxPayService.createOrder(orderRequest);

        return orderResult;

    }
}
