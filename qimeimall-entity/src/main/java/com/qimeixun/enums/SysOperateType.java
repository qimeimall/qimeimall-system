package com.qimeixun.enums;

/**
 * 系统操作类型枚举
 * @author wangdaqiang
 * @date 2019-08-22
 */
public enum SysOperateType {
    /**
     * 新增
     */
    ADD(0, "新增"),
    /**
     * 修改
     */
    UPDATE(1, "修改"),
    /**
     * 删除
     */
    DELETE(2, "删除"),
    /**
     * 登录日志
     */
    LOGIN_LOG(3, "登录日志");


    /**
     * 操作类型id
     */
    private Integer id;
    /**
     * 操作类型名称
     */
    private String name;


    private SysOperateType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
