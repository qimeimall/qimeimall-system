package com.qimeixun.vo;

import lombok.Data;

@Data
public class WXSessionModelVO {
    private String errcode;
    private String session_key;
    private String openid;
}
