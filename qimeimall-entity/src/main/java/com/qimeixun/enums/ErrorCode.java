package com.qimeixun.enums;

/**
 * 异常编码常量
 * @author wangdaqiang
 * @date 2019-08-24 11:32
 */
public enum ErrorCode {

    /**
     * 002：系统异常
     */
    SYSTEM_ERROR("002", "系统异常"),
    /**
     * 003：无操作权限
     */
    NO_PERMISSION("003", "无操作权限"),
    /**
     * 005：请重新登录
     */
    TOKEN_INVALID("005", "请重新登录"),
    /**
     * 006：余额不足
     */
    INSUFFICIENT_BALANCE("006", "余额不足");







    private String code;
    private String message;


    private ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
