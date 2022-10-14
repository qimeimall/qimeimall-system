package com.qimeixun.modules.config;


import com.alibaba.fastjson.JSON;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.google.common.collect.Maps;
import com.qimeixun.constant.SystemConstant;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.po.SysConfigDTO;
import com.qimeixun.util.RedisUtil;
import com.qimeixun.util.WeiXinUtils;
import com.qimeixun.vo.WXSessionModelVO;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Configuration
public class WxPayConfiguration {

    private static Map<String, WxMpService> mpServices = Maps.newHashMap();

    private static Map<String, WxPayService> payServices = Maps.newHashMap();

    private static RedisUtil redisUtil;

    @Autowired
    public WxPayConfiguration(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 获取WxPayService
     *
     * @return
     */
    public static WxPayService getPayService() {
        WxPayService wxPayService = payServices.get(SystemConstant.WEIXIN_PAY_SERVICE);
        if (wxPayService == null || redisUtil.get(SystemConstant.WEIXIN_PAY_SERVICE) == null) {
            WxPayConfig payConfig = new WxPayConfig();
            payConfig.setAppId(getRedisValue("wechat_appid"));
            payConfig.setMchId(getRedisValue("wxpay_mchId"));
            payConfig.setMchKey(getRedisValue("wxpay_mchKey"));
            payConfig.setKeyPath(getRedisValue("wxpay_keyPath"));
            // 可以指定是否使用沙箱环境
            payConfig.setUseSandboxEnv(false);
            wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(payConfig);
            payServices.put(SystemConstant.WEIXIN_PAY_SERVICE, wxPayService);

        }
        return wxPayService;
    }

    /**
     * 获取小程序WxAppPayService
     * wx9400e1e5699c4c27
     * 1604030712
     * whcsflkjyxgsWHCSFLKJYXGS0720csfl
     *
     * @return
     */
    public static WxPayService getWxAppPayService() {
        WxPayService wxPayService = payServices.get(SystemConstant.WEIXIN_MINI_PAY_SERVICE);
        if (wxPayService == null || redisUtil.get(SystemConstant.WEIXIN_MINI_PAY_SERVICE) == null) {
            WxPayConfig payConfig = new WxPayConfig();
            payConfig.setAppId(getRedisValue("wxapp_appId"));
            payConfig.setMchId(getRedisValue("wxpay_mchId"));
            payConfig.setMchKey(getRedisValue("wxpay_mchKey"));
            payConfig.setKeyPath(getRedisValue("wxpay_keyPath"));
            // 可以指定是否使用沙箱环境
            payConfig.setUseSandboxEnv(false);
            wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(payConfig);
            payServices.put(SystemConstant.WEIXIN_MINI_PAY_SERVICE, wxPayService);
        }
        return wxPayService;
    }

    public static String getMiniOpenId(String code) {
        try {
            return getWXSessionModelVO(code).getOpenid();
        } catch (Exception exception) {
            log.error("获取用户openId失败", exception);
            throw new ServiceException("获取用户openId失败");
        }
    }

    public static WXSessionModelVO getWXSessionModelVO(String code){
        //临时登录凭证
        RestTemplate restTemplate = WeiXinUtils.getInstance();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code";
        Map<String, Object> map = new HashMap<>();
        map.put("appid", getRedisValue("wxapp_appId"));
        map.put("secret", getRedisValue("wxapp_secret"));
        map.put("code", code);
        WXSessionModelVO wxSessionModelVO = restTemplate.getForObject(url, WXSessionModelVO.class, map);
        System.out.println(JSON.toJSONString(wxSessionModelVO));
        return wxSessionModelVO;
    }

    public static String getAppId(){
        return getRedisValue("wxapp_appId");
    }

    public static String getAppSecret(){
        return getRedisValue("wxapp_secret");
    }

    public static String getOpenId(String code) {
        try {
            //临时登录凭证
            RestTemplate restTemplate = WeiXinUtils.getInstance();
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code";
            Map<String, Object> map = new HashMap<>();
            map.put("appid", getRedisValue("wechat_appid"));
            map.put("secret", getRedisValue("wechat_secret"));
            map.put("code", code);
            WXSessionModelVO wxSessionModelVO = restTemplate.getForObject(url, WXSessionModelVO.class, map);
            System.out.println(JSON.toJSONString(wxSessionModelVO));
            return wxSessionModelVO.getOpenid();
        } catch (Exception exception) {
            log.error("获取用户openId失败", exception);
            throw new ServiceException("获取用户openId失败");
        }
    }

    /**
     * 获取WxMpService
     *
     * @return
     */
    public static WxMpService getWxMpService() {

        WxMpService wxMpService = mpServices.get(SystemConstant.WEIXIN_MP_SERVICE);
        //增加一个redis标识
        if (wxMpService == null || redisUtil.get(SystemConstant.WEIXIN_MP_SERVICE) == null) {
            WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
            configStorage.setAppId(getRedisValue("wechat_appid"));
            configStorage.setSecret(getRedisValue("wechat_appsecret"));
            configStorage.setToken(getRedisValue("wechat_token"));
            configStorage.setAesKey(getRedisValue("wechat_encodingaeskey"));
            wxMpService = new WxMpServiceImpl();
            wxMpService.setWxMpConfigStorage(configStorage);
            mpServices.put(SystemConstant.WEIXIN_MP_SERVICE, wxMpService);
        }
        return wxMpService;
    }

    /**
     * 获取接口地址
     *
     * @return
     */
    public static String getApiUrl() {
        return getRedisValue("api_url");
    }

    public static String getRedisValue(String key) {
        try {
            Object o = redisUtil.get(SystemConstant.SYS_CONFIG_HEAD + key);
            if (o == null) {
                return "";
            }
            return String.valueOf(o);
        } catch (Exception ex) {
            throw new ServiceException("获取微信支付信息失败");
        }
    }

    /**
     * 移除WxPayService
     */
    public static void removeWxPayService() {
        redisUtil.del(SystemConstant.WEIXIN_PAY_SERVICE);
        payServices.remove(SystemConstant.WEIXIN_PAY_SERVICE);
    }


}
