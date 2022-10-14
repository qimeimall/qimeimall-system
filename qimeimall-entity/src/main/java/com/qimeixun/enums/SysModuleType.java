package com.qimeixun.enums;

/**
 * 模块类型枚举
 *
 * @author wangdaqiang
 * @date 2019-08-22 15:41
 */
public enum SysModuleType {

    /**
     * 系统用户
     */
    SYS_USER("系统用户"),
    /**
     * 系统角色
     */
    SYS_ROLE("系统角色"),
    /**
     * 菜单资源
     */
    SYS_RESOURCE("菜单资源"),
    /**
     * 订单模块
     */
    ORDER("订单模块"),
    /**
     * 系统参数配置
     */
    PARKING_SYSTEM_SET("系统参数配置"),
    /**
     * 登录日志
     */
    LOGIN_LOG("登录日志"),
    /**
     * 后台系统
     */
    BACKGROUND("后台系统");


    private String name;


    private SysModuleType(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

}
