package com.qimeixun.exceptions;


/**
 * 操作数据或库出现异常
 */
public class DataDoException extends RuntimeException{

    public DataDoException(String msg) {
        super(msg);
    }
}