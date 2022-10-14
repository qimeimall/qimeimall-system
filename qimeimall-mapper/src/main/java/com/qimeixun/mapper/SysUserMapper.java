package com.qimeixun.mapper;

import com.qimeixun.entity.SysUser;
import com.qimeixun.ro.SysLoginRO;
import com.qimeixun.ro.SysUserRO;
import com.qimeixun.ro.SysUserRoleRO;
import com.qimeixun.vo.SysUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 管理员用户Mapper
 * @author wangdaqiang
 * @date 2019-08-22
 */
@Mapper
public interface SysUserMapper {


    /**
     * 新增管理员用户信息
     * @param sysUser 新增的数据
     * @return 受影响行数
     */
    int insert(SysUser sysUser);


    /**
     * 删除
     * @param id 主键id
     * @return 受影响行数
     */
    int delete(String id);


    /**
     * 修改管理员用户信息
     * @param sysUser 修改的数据
     * @return
     */
    int update(SysUser sysUser);


    /**
     * 根据id查询管理员用户信息
     * @param id 主键id
     * @return
     */
    SysUser selectById(String id);


    /**
     * 查询管理员用户数据列表(分页)
     * @param userRO 查询条件
     * @return
     */
    List<SysUserVO> selectSysUserList(SysUserRO userRO);


    /**
     * 查询管理员用户数据总条数(分页)
     * @param userRO 查询条件
     * @return
     */
    int selectSysUserListCount(SysUserRO userRO);


    /**
     * 根据登录名查询
     * @param loginName 登录名
     * @param id 需要过滤掉的id(可以为空)
     * @return
     */
    SysUser selectByLoginName(@Param("loginName") String loginName, @Param("id") String id);


    /**
     * 根据手机号查询
     * @param mobile 手机号
     * @param id 需要过滤掉的id(可以为空)
     * @return
     */
    SysUser selectByMobile(@Param("mobile") String mobile, @Param("id") String id);


    /**
     * 管理员登录
     * @param sysLoginRO 登录信息
     * @return
     */
    SysUser selectByLogin(SysLoginRO sysLoginRO);


    /**
     * 添加管理员用户角色
     * @param ro
     */
    void addSysUserRole(SysUserRoleRO ro);



    /**
     * 删除管理员用户角色
     * @param ro
     */
    int deleteSysUserRole(SysUserRoleRO ro);


    /**
     * 批量添加管理员用户角色
     * @param userId 用户id
     * @param roleIds 角色id集合
     */
    void addSysUserRoles(@Param("userId") String userId, @Param("roleIds") List<String> roleIds);

}
