package com.qimeixun.enums;

public enum ResultCode {

    // 成功
    SUCCESS("200"),

    // 失败
    FAIL("201"),

    // 未认证（签名错误）
    UNAUTHORIZED("401"),

    // 接口不存在
    NOT_FOUND("404"),

    // 服务器内部错误
    INTERNAL_SERVER_ERROR("500");



    public String code;

    ResultCode(String code) {
        this.code = code;
    }
}
