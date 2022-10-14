package com.qimeixun.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统操作日志
 * @author wangdaqiang
 * @date 2019-09-27 11:22
 */
@Data
public class SysOperateLog implements Serializable {
    private static final long serialVersionUID = 1329669571004003114L;


    /**
     * 主键id
     */
    private String id;

    /**
     * 操作类型({@link com.fpd.enums.SysOperateType})
     */
    private Integer operateType;

    /**
     * 所属模块({@link com.fpd.enums.SysModuleType})
     */
    private String moduleId;

    /**
     * 操作目标数据的主键id
     */
    private String targetId;

    /**
     * 操作内容描述
     */
    private String description;

    /**
     * 更改前数据(json字符串)
     */
    private String beforeData;

    /**
     * 更改后数据(json字符串)
     */
    private String afterData;

    /**
     * 操作用户id
     */
    private String userId;

    /**
     * 操作用户IP地址
     */
    private String userIp;

    /**
     * 创建时间
     */
    private Date createdAt;


}
