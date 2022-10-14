package com.qimeixun.util;

import com.alibaba.fastjson.JSON;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.vo.WXSessionModelVO;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class WeiXinUtils {

    public static RestTemplate getInstance() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    public static WXSessionModelVO getMiniOpenId(String code) {
        try {
            //临时登录凭证
            RestTemplate restTemplate = getInstance();
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";
            Map<String, Object> map = new HashMap<>();
            map.put("appid", "wx2f27b5d170401608");
            map.put("secret", "04ec05a2381008ddbd87f4316d7ef3c5");
            map.put("code", code);
            WXSessionModelVO wxSessionModelVO = restTemplate.getForObject(url, WXSessionModelVO.class, map);
            System.out.println(JSON.toJSONString(wxSessionModelVO));
            return wxSessionModelVO;
        } catch (Exception exception) {
            throw new ServiceException("获取用户openId失败");
        }
    }
}
