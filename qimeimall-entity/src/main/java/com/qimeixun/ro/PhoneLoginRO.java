package com.qimeixun.ro;

import lombok.Data;

@Data
public class PhoneLoginRO {

    private String phone;

    private String code;

    private String loginType;

    private String encryptedData;

    private String iv;

    private String wxCode;

    private String headImg;

    private String nickName;

    /**
     * 分享码
     */
    private String referrerCode;
}
