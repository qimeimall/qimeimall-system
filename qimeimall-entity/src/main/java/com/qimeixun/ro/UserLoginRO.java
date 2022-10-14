package com.qimeixun.ro;

import lombok.Data;

/**
 * @author chenshouyang
 * @date 2020/5/2218:19
 */
@Data
public class UserLoginRO {

    private String code;

    private String headImg;

    private String sex;

    private String phone;

    private String nickName;

    private String type;

    private String referrerCode;
}
