package com.qimeixun.modules.system.service;

import com.qimeixun.ro.SysUserRO;
import com.qimeixun.ro.SysUserRoleRO;
import com.qimeixun.vo.ResponseResultVO;

/**
 * 管理员用户接口
 * @author wangdaqiang
 * @date 2019-08-23 09:10
 */
public interface SysUserService {

    /**
     * 新增管理员用户信息
     * @param sysUserRO 新增的数据
     * @return
     */
    ResponseResultVO insert(SysUserRO sysUserRO);


    /**
     * 删除
     * @param id 主键id
     * @return
     */
    ResponseResultVO delete(String id);


    /**
     * 修改管理员用户信息
     * @param sysUserRO 修改的数据
     * @return
     */
    ResponseResultVO update(SysUserRO sysUserRO);

    /**
     * 根据id查询管理员用户信息
     * @param id 主键id
     * @return
     */
    ResponseResultVO selectById(String id);

    /**
     * 查询管理员用户数据列表(分页)
     * @param userRO 查询条件
     * @return
     */
    ResponseResultVO selectSysUserList(SysUserRO userRO);


    /**
     * 修改管理员用户角色
     * @param ro 请求参数
     * @return
     */
    ResponseResultVO updateSysUserRole(SysUserRoleRO ro);


    /**
     * 查询管理员用户角色列表
     * @param id 用户id
     * @return
     */
    ResponseResultVO selectSysUserRoleList(String id);


}
