package com.qimeixun.listen;

import com.qimeixun.constant.SystemConstant;
import com.qimeixun.service.OrderCommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisKeyExpirationListener implements MessageListener {


    private OrderCommonService orderCommonService;

    public RedisKeyExpirationListener(OrderCommonService orderCommonService) {
        this.orderCommonService = orderCommonService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        //获取redis操作类型: set、expire、expired等操作
        String action = new String(message.getChannel());
        action = action.split(":")[1];
        //获取redis操作的key
        String key = new String(message.getBody());

        try {
            //超时取消订单
            if (key.contains(SystemConstant.REDIS_CANCEL_KEY)) {
                String orderId = key.replace(SystemConstant.REDIS_CANCEL_KEY, "");
                orderCommonService.cancelOrder(orderId, null);
                return;
            }

            //超时自动收货
            if(key.contains(SystemConstant.AUTO_RECEIVE_KEY)){
                String orderId = key.replace(SystemConstant.AUTO_RECEIVE_KEY, "");
                orderCommonService.confirmReceiptGoods(orderId, null);
            }
        } catch (Exception exception) {
            log.error("订单操作失败", exception);
        }

    }
}
