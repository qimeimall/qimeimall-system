package com.qimeixun.wxPayConfig;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author chenshouyang
 * @date 2020/5/2714:48
 */
public class WxPayConfig implements WXPayConfig {

    private byte[] certData;

//    public WxPayConfig() throws Exception {
//
//        try{
//            String certPath = "/path/to/apiclient_cert.p12";
//            File file = new File(certPath);
//            InputStream certStream = new FileInputStream(file);
//            this.certData = new byte[(int) file.length()];
//            certStream.read(this.certData);
//            certStream.close();
//        }catch (Exception ex){
//
//        }
//
//    }

    public String getAppID() {
        return "wx2f27b5d170401608";
    }

    public String getMchID() {
        return "1593947541";
    }

    public String getKey() {
        return "9oFvskLH1XFoHJ9EINzkPNbC0anso4qY";
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
