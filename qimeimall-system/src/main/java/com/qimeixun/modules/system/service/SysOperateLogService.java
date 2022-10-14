package com.qimeixun.modules.system.service;

import com.qimeixun.entity.SysUser;
import com.qimeixun.enums.SysModuleType;
import com.qimeixun.enums.SysOperateType;
import com.qimeixun.ro.SysOperateLogRO;
import com.qimeixun.vo.ResponseResultVO;

/**
 * 系统操作日志接口
 * @author wangdaqiang
 * @date 2019-08-22
 */
public interface SysOperateLogService {


    /**
     * 记录操作日志
     * @param operateType 操作类型
     * @param moduleType 日志所属功能模块类型
     * @param targetId 操作数据的主键id
     * @param description 操作内容描述(例：修改xxx)
     * @param beforeData 变更前数据
     * @param afterData 变更后数据
     */
    void insertLog(SysOperateType operateType, SysModuleType moduleType, String targetId, String description, Object beforeData, Object afterData);


    /**
     * 记录新增类型的操作日志
     * @param moduleType 模块类型
     * @param targetId 操作数据的主键id
     * @param description 操作内容描述(例：新增用户)
     */
    void insertAddLog(SysModuleType moduleType, String targetId, String description);


    /**
     * 记录新增类型的操作日志
     * @param targetId 操作数据的主键id
     * @param description 操作内容描述(例：新增用户)
     */
    void insertAddLog(String targetId, String description);


    /**
     * 记录更新类型的操作日志
     * @param moduleType 模块类型
     * @param targetId 操作数据的主键id
     * @param description 操作内容描述(例：修改用户信息)
     * @param beforeData 变更前数据
     * @param afterData 变更后数据
     */
    void insertUpdateLog(SysModuleType moduleType, String targetId, String description, Object beforeData, Object afterData);


    /**
     * 记录更新类型的操作日志
     * @param targetId 操作数据的主键id
     * @param description 操作内容描述(例：修改用户信息)
     * @param beforeData 变更前数据
     * @param afterData 变更后数据
     */
    void insertUpdateLog(String targetId, String description, Object beforeData, Object afterData);


    /**
     * 记录删除类型的操作日志
     * @param moduleType 模块类型
     * @param targetId 操作数据的主键id
     * @param description 操作内容描述(例：删除用户)
     */
    void insertDeleteLog(SysModuleType moduleType, String targetId, String description);


    /**
     * 记录删除类型的操作日志
     * @param targetId 操作数据的主键id
     * @param description 操作内容描述(例：删除用户)
     */
    void insertDeleteLog(String targetId, String description);


    /**
     * 记录登录日志
     * @param sysUser 登录用户
     * @param description 操作内容描述(例：登录系统)
     */
    void insertLoginLog(SysUser sysUser, String description);


    /**
     * 查询操作日志(分页)
     * @param logRO 查询条件
     * @return
     */
    ResponseResultVO selectLogList(SysOperateLogRO logRO);

}
