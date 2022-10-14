package com.qimeixun.modules.system.service;

import com.qimeixun.entity.SysResource;
import com.qimeixun.ro.MenuRO;
import com.qimeixun.ro.SysResourcePageRO;
import com.qimeixun.ro.SysResourceRO;
import com.qimeixun.vo.ResponseResultVO;

/**
 * 系统资源接口
 * @author wangdaqiang
 * @date 2019-08-23 19:39
 */
public interface SysResourceService {


    /**
     * 新增资源
     * @param sysResource 新增的数据实体
     * @return
     */
    ResponseResultVO insert(SysResource sysResource);


    /**
     * 修改资源
     * @param sysResource 修改的数据实体
     * @return
     */
    ResponseResultVO update(SysResource sysResource);


    /**
     * 删除资源
     * @param id 主键id
     * @return
     */
    ResponseResultVO delete(String id);


    /**
     * 查询资源列表
     * @param pageRO 查询条件
     * @return
     */
    ResponseResultVO selectSysResourceList(SysResourcePageRO pageRO);


    /**
     * 查询资源树形列表
     * @param ro 查询条件
     * @return
     */
    ResponseResultVO selectTreeList(SysResourceRO ro);



    /**
     * 菜单初始化
     * @return
     */
    ResponseResultVO menuInit();


    /**
     * 查询菜单按钮列表
     * @param ro 查询条件
     * @return
     */
    ResponseResultVO selectMenuBtnList(MenuRO ro);

}
