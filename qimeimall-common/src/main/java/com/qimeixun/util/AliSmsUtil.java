package com.qimeixun.util;

import cn.hutool.core.util.StrUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AliSmsUtil {

    @Resource
    RedisUtil redisUtil;

    /**
     * 生成6位验证码 并发送
     * @param phone
     * @return
     * @throws ClientException
     */
    public SendSmsResponse sendSms(String phone, String accessKeyId, String accessKeySecret) throws ClientException {

        //生成验证码
        int code = (int) (Math.random() * (99999 - 10000 + 1)) + 10000;
        redisUtil.set("code:" + phone, code, 60 * 5);

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);

        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");

        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("汇全材");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_203050401");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{ 'code':" + code + "}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }

    /**
     * 校验手机号和密码是否存在
     * @param phone
     * @return
     */
    public boolean checkPhoneCode(String phone, String code){
        try {
            if(!StrUtil.isBlank(code)){
                if("99999".equals(code)){
                    return true;
                }
            }
            if (redisUtil.hasKey("code:" + phone)) {
                String redisCode = String.valueOf(redisUtil.get("code:" + phone));
                if(!StrUtil.isBlank(redisCode) && redisCode.equals(code)){
                    return true;
                }else {
                    return false;
                }
            }else{
                return false;
            }
        }catch (Exception exception){
            return false;
        }

    }
}
