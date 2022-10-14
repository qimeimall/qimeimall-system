package com.qimeixun.mapper;

import com.qimeixun.entity.SysRole;
import com.qimeixun.ro.SysRoleResourceRO;
import com.qimeixun.vo.SysRoleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 系统角色dao接口
 * @author wangdaqiang
 * @date 2019-08-24 15:39
 */
@Mapper
public interface SysRoleMapper {

    /**
     * 新增
     * @param sysRole 新增的数据
     */
    void insert(SysRole sysRole);


    /**
     * 修改
     * @param sysRole 修改的数据
     * @return 受影响行数
     */
    int update(SysRole sysRole);


    /**
     * 删除
     * @param id 主键id
     * @return 受影响行数
     */
    int delete(String id);


    /**
     * 查询详情
     * @param id 主键id
     * @return
     */
    SysRole selectById(String id);


    /**
     * 查询列表(支持分页)
     * @param params 查询条件
     * @return
     */
    List<SysRole> selectByParams(Map<String, Object> params);


    /**
     * 查询总条数
     * @param params 查询条件
     * @return
     */
    int selectByParamsCount(Map<String, Object> params);


    /**
     * 查询用户的角色信息
     * @param userId 用户id
     * @return
     */
    List<SysRoleVO> selectByUserId(String userId);


    /**
     * 删除角色所有的权限资源
     * @param roleId 角色id
     * @return
     */
    int deleteRoleResource(String roleId);


    /**
     * 添加角色权限资源
     * @param ro 添加的数据
     */
    int addRoleResource(SysRoleResourceRO ro);


    /**
     * 删除角色所有的停车场权限
     * @param roleId 角色id
     * @return
     */
    int deleteRoleParkPermission(String roleId);


    /**
     * 添加角色停车场权限
     * @param ro 添加的数据
     * @return
     */
//    int addRoleParkPermission(SysRoleParkRO ro);



}
