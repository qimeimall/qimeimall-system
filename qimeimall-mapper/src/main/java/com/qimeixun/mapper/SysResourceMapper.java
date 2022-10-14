package com.qimeixun.mapper;

import com.qimeixun.entity.SysResource;
import com.qimeixun.enums.TerminalType;
import com.qimeixun.vo.MenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统资源mapper
 * @author wangdaqiang
 * @date 2019-08-23 18:58
 */
@Mapper
public interface SysResourceMapper {


    /**
     * 新增资源
     * @param sysResource 新增的数据
     */
    int insert(SysResource sysResource);


    /**
     * 修改资源
     * @param sysResource 修改的数据
     * @return 受影响行数
     */
    int update(SysResource sysResource);


    /**
     * 删除
     * @param id 主键id
     * @return 受影响行数
     */
    int delete(String id);


    /**
     * 根据查询资源详情
     * @param id 主键id
     * @return
     */
    SysResource selectById(String id);


    /**
     * 查询资源列表(支持分页)
     * @param params 查询条件
     * @return
     */
    List<SysResource> selectByParams(Map<String, Object> params);


    /**
     * 查询资源列表总条数(支持分页)
     * @param params 查询条件
     * @return
     */
    int selectByParamsCount(Map<String, Object> params);


    /**
     * 验证用户是否有指定的资源权限
     * @param userId 用户id
     * @param permission 资源权限字符串
     * @return
     */
    int selectByPermissionCount(@Param("userId") String userId, @Param("permission") String permission);


    /**
     * 查询角色的资源信息
     * @param roleId 角色id
     * @return
     */
    List<SysResource> selectByRoleId(String roleId);


    /**
     * 查询用户拥有权限的菜单数据
     * @param id 用户id
     * @param terminalType 终端类型({@link TerminalType})
     * @return
     */
    List<MenuVO> menuInit(@Param("userId") String id, @Param("terminalType") TerminalType terminalType);


    /**
     * 查询所有菜单数据
     * @param terminalType 终端类型({@link TerminalType})
     * @return
     */
    List<MenuVO> selectMenuAll(@Param("terminalType") TerminalType terminalType);


    /**
     * 根据id查询菜单
     * @param id 菜单id
     * @return
     */
    MenuVO selectMenuById(String id);


    /**
     * 查询所有菜单数据
     * @param params 查询条件
     * @return
     */
    List<MenuVO> selectMenuByParams(Map<String, Object> params);


    /**
     *
     * 查询用户拥有权限的资源的权限字符串集合
     * @param userId 用户id
     * @param terminalType 终端类型({@link TerminalType})
     * @return
     */
    List<String> selectPermissionByUserId(@Param("userId") String userId, @Param("terminalType") TerminalType terminalType);


    /**
     * 查询用户指定菜单下的按钮权限列表
     * @param params 查询条件
     * @return
     */
    List<SysResource> selectMenuBtnList(Map<String, Object> params);



}