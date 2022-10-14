package com.qimeixun.modules.pay.factory;

import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.modules.pay.service.PayService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PayServiceFactory {

    private static final Map<String, PayService> strategyMap = new ConcurrentHashMap<>();


    public static PayService getStrategy(String payType) {
        PayService loginStrategy = strategyMap.get(payType);
        if (loginStrategy == null) {
            throw new ServiceException("获取支付方式失败");
        }
        return strategyMap.get(payType);
    }

    public static void register(String payType, PayService payService) {
        strategyMap.put(payType, payService);
    }
}
