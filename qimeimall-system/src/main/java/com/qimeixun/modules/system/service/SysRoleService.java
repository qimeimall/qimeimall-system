package com.qimeixun.modules.system.service;

import com.qimeixun.ro.SysRoleRO;
import com.qimeixun.ro.SysRoleResourceRO;
import com.qimeixun.vo.ResponseResultVO;

/**
 * 系统角色接口
 * @author wangdaqiang
 * @date 2019-08-24 15:53
 */
public interface SysRoleService {

    /**
     * 新增
     * @param sysRoleRO 新增的数据
     * @return
     */
    ResponseResultVO insert(SysRoleRO sysRoleRO);


    /**
     * 修改
     * @param sysRoleRO 修改的数据
     * @return
     */
    ResponseResultVO update(SysRoleRO sysRoleRO);


    /**
     * 删除
     * @param id 主键id
     * @return
     */
    ResponseResultVO delete(String id);


    /**
     * 查询列表
     * @param sysRoleRO 查询条件
     * @return
     */
    ResponseResultVO selectList(SysRoleRO sysRoleRO);


    /**
     * 修改角色权限
     * @param ro 请求参数
     * @return
     */
    ResponseResultVO updateSysRoleResource(SysRoleResourceRO ro);



    /**
     * 查询角色权限列表
     * @param id 角色id
     * @return
     */
    ResponseResultVO selectSysRoleResourceList(String id);


    /**
     * 修改角色停车场权限
     * @param ro 请求参数
     * @return
     */
//    ResponseResultVO updateRoleParkPermission(SysRoleParkRO ro);

}
