package com.qimeixun.enums;

public enum PayType {

    WX_PAY("wx"),

    ALI_PAY("ali"),

    WXH5_PAY("wxh5"),

    WX_APP_PAY("wxapp"),

    WXAPP_PAY("app"),

    CASH_PAY("cash"),

    POINTS_PAY("points"),

    BALANCE_PAY("balance");

    private String type;

    PayType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
