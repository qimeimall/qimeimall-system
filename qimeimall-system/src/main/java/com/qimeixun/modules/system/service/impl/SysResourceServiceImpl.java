package com.qimeixun.modules.system.service.impl;

import com.qimeixun.entity.SysResource;
import com.qimeixun.entity.SysUser;
import com.qimeixun.enums.TerminalType;
import com.qimeixun.mapper.SysResourceMapper;
import com.qimeixun.ro.MenuRO;
import com.qimeixun.ro.PageRO;
import com.qimeixun.ro.SysResourcePageRO;
import com.qimeixun.ro.SysResourceRO;
import com.qimeixun.modules.system.service.SysResourceService;
import com.qimeixun.util.TokenUtil;
import com.qimeixun.util.Tool;
import com.qimeixun.vo.MenuVO;
import com.qimeixun.vo.ResponseResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 系统资源接口实现
 *
 * @author wangdaqiang
 * @date 2019-08-23 19:49
 */
@Service
public class SysResourceServiceImpl implements SysResourceService {

    private static final Logger logger = LoggerFactory.getLogger(SysResourceServiceImpl.class);

    @Resource
    private SysResourceMapper sysResourceMapper;
    //    @Resource
//    private SysOperateLogService sysOperateLogService;
    @Resource
    private TokenUtil tokenUtil;


    @Override
    public ResponseResultVO insert(SysResource sysResource) {
        try {
            if (sysResource.getResourceType() == null) {
                return ResponseResultVO.failResult("资源类型不能为空");
            }
            if (StringUtils.isBlank(sysResource.getNameCn())) {
                return ResponseResultVO.failResult("菜单名称不能为空");
            }
            if (StringUtils.isBlank(sysResource.getParentId())) {
                sysResource.setParentId("0");
            }
            if (StringUtils.isBlank(sysResource.getNameEn())) {
                sysResource.setNameEn(sysResource.getNameCn());
            }
            sysResource.setId(Tool.getPrimaryKey());
            // 根据接口路径自动转换权限字符串
            if (StringUtils.isNotBlank(sysResource.getUrl())) {
                String uri = sysResource.getUrl();
                if (uri.startsWith("/")) {
                    uri = uri.substring(1);
                }
                String permissionStr = uri.replace("/", ":");
                sysResource.setPermission(permissionStr);
            }
            int num = this.sysResourceMapper.insert(sysResource);
            if (num <= 0) {
                return ResponseResultVO.failResult("新增失败");
            }
            // 记录操作日志
            //this.sysOperateLogService.insertAddLog(SysModuleType.SYS_RESOURCE, sysResource.getId(), "新增-系统资源");

            return ResponseResultVO.successResult();
        } catch (Exception e) {
            logger.error("新增-系统资源-异常", e);
            return ResponseResultVO.errorResult();
        }
    }

    @Override
    public ResponseResultVO update(SysResource sysResource) {
        try {
            if (StringUtils.isBlank(sysResource.getId())) {
                return ResponseResultVO.failResult("资源id不能为空");
            }
            sysResource.setTerminalType("web");
            SysResource oldResource = this.sysResourceMapper.selectById(sysResource.getId());
            if (oldResource == null) {
                return ResponseResultVO.failResult("资源不存在");
            }
            int num = this.sysResourceMapper.update(sysResource);
            if (num <= 0) {
                return ResponseResultVO.failResult();
            }
            // 记录操作日志
            //this.sysOperateLogService.insertUpdateLog(SysModuleType.SYS_RESOURCE, sysResource.getId(), "修改-系统资源", oldResource, sysResource);

            return ResponseResultVO.successResult();
        } catch (Exception e) {
            logger.error("修改-系统资源-异常", e);
            return ResponseResultVO.errorResult();
        }
    }


    @Override
    public ResponseResultVO delete(String id) {
        try {
            if (StringUtils.isBlank(id)) {
                return ResponseResultVO.failResult("资源id不能为空");
            }
            int num = this.sysResourceMapper.delete(id);
            if (num <= 0) {
                return ResponseResultVO.failResult();
            }
            // 记录操作日志
            //this.sysOperateLogService.insertDeleteLog(SysModuleType.SYS_RESOURCE, id, "删除-系统资源");

            return ResponseResultVO.successResult();
        } catch (Exception e) {
            logger.error("删除-系统资源-异常", e);
            return ResponseResultVO.errorResult();
        }
    }


    @Override
    public ResponseResultVO selectSysResourceList(SysResourcePageRO pageRO) {
        try {
            PageRO page = new PageRO();
            page.setCurrentPage(pageRO.getCurrentPage());
            page.setPageSize(pageRO.getPageSize());

            Map<String, Object> params = new HashMap<>();
            params.put("page", page);

            List<SysResource> list = this.sysResourceMapper.selectByParams(params);
            int count = this.sysResourceMapper.selectByParamsCount(params);

            return ResponseResultVO.resultList(list, count, pageRO);
        } catch (Exception e) {
            logger.error("查询-系统资源列表-异常", e);
            return ResponseResultVO.errorResult();
        }
    }

    @Override
    public ResponseResultVO selectTreeList(SysResourceRO ro) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("terminalType", TerminalType.WEB);

            if (StringUtils.isNotBlank(ro.getId())) {
                params.put("id", ro.getId());
            }
            if (ro.getResourceType() != null) {
                params.put("resourceType", ro.getResourceType());
            }
            if (StringUtils.isNotBlank(ro.getParentId())) {
                params.put("parentId", ro.getParentId());
            }
            if (StringUtils.isNotBlank(ro.getNameCn())) {
                params.put("nameCn", ro.getNameCn());
            }
            if (StringUtils.isNotBlank(ro.getNameEn())) {
                params.put("nameEn", ro.getNameEn());
            }
            if (StringUtils.isNotBlank(ro.getPermission())) {
                params.put("permission", ro.getPermission());
            }
            if (StringUtils.isNotBlank(ro.getRoleId())) {
                params.put("roleId", ro.getRoleId());
            }

            List<MenuVO> list = this.sysResourceMapper.selectMenuByParams(params);
            List<MenuVO> treeList = this.listToTreeList(list);

            return ResponseResultVO.successResult(treeList);
        } catch (Exception e) {
            logger.error("查询-资源树形列表-异常", e);
            return ResponseResultVO.errorResult();
        }
    }

    @Override
    public ResponseResultVO menuInit() {

        SysUser sysUser = this.tokenUtil.getSysUserInfo();

        if (sysUser.getIsSuper() != null && sysUser.getIsSuper() == 1) {
            // 超级管理员查询拥有所有菜单权限
            List<MenuVO> list = this.sysResourceMapper.selectMenuAll(TerminalType.WEB);
            // 将集合转换成树形集合(前端需要的数据格式)
            List<MenuVO> treeList = this.listToTreeList(list);

            return ResponseResultVO.successResult(treeList);
        }
        // 根据权限查询菜单
        List<MenuVO> list = this.sysResourceMapper.menuInit(sysUser.getId(), TerminalType.WEB);

        List<String> keyList = new ArrayList<>();
        for (MenuVO menuVo : list) {
            keyList.add(menuVo.getId());
        }
        List<MenuVO> newList = new ArrayList<>();
        for (MenuVO menuVo : list) {
            // 递归查询到顶级节点
            this.findParentMenu(menuVo, keyList, newList);
        }
        if (!CollectionUtils.isEmpty(newList)) {
            list.addAll(newList);
        }
        // 将集合转换成树形集合(前端需要的数据格式)
        List<MenuVO> treeList = this.listToTreeList(list);

        return ResponseResultVO.successResult(treeList);

    }

    @Override
    public ResponseResultVO selectMenuBtnList(MenuRO ro) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(ro.getMenuId())) {
            params.put("id", ro.getMenuId());
        }
        if (StringUtils.isNotBlank(ro.getMenuUrl())) {
            params.put("url", ro.getMenuUrl());
        }
        params.put("resourceType", 1);

        SysUser sysUser = this.tokenUtil.getSysUserInfo();
        if (sysUser.getIsSuper() != null && sysUser.getIsSuper() != 1) {
            // 不是超级管理员，则根据权限过滤
            params.put("userId", sysUser.getId());
        }
        List<SysResource> list = this.sysResourceMapper.selectMenuBtnList(params);

        return ResponseResultVO.successResult(list);
    }


    /**
     * 递归查询上级菜单，一直查到一级菜单
     *
     * @param menuVO   需要递归查询的菜单对象
     * @param keyList  已查询到的菜单id集合
     * @param menuList 递归查询到的数据存放集合
     */
    private void findParentMenu(MenuVO menuVO, List<String> keyList, List<MenuVO> menuList) {
        if (StringUtils.isBlank(menuVO.getParentId()) || "0".equals(menuVO.getParentId())
                || keyList.contains(menuVO.getParentId())) {
            // 一级节点、已存在节点 则跳过
            return;
        }
        MenuVO menu = this.sysResourceMapper.selectMenuById(menuVO.getParentId());
        if (menu != null) {
            menuList.add(menu);
            keyList.add(menuVO.getParentId());

            // 递归查询到顶级节点
            this.findParentMenu(menu, keyList, menuList);
        }
    }


    /**
     * 将list转换成树形结构集合
     *
     * @param list 需要转化的数据
     * @return 转换后的树形结构集合
     */
    private List<MenuVO> listToTreeList(List<MenuVO> list) {
        List<MenuVO> treeList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return treeList;
        }
        /*
         * 先对集合排序
         * 排序后，最终的树形集合、各节点下的子节点集合，都会与初始集合排序规则一致
         */
        Comparator<MenuVO> ageComparator = (o1, o2) -> o1.getSortNo().compareTo(o2.getSortNo());
        // 按配置的顺序取值
        list.sort(ageComparator);

        Map<String, MenuVO> map = new HashMap<>();
        // 将菜单集合数据存放到map集合中，key为菜单id，后续判断父节点用
        for (MenuVO menuVO : list) {
            map.put(menuVO.getId(), menuVO);
            // 将一级节点添加到树形集合中
            if (StringUtils.isBlank(menuVO.getParentId()) || "0".equals(menuVO.getParentId())) {
                treeList.add(menuVO);
            }
        }
        // 遍历结果集
        for (MenuVO menuVO : list) {
            if (StringUtils.isBlank(menuVO.getParentId()) || "0".equals(menuVO.getParentId())) {
                // 跳过一级节点
                continue;
            }
            // 判断父节点是否存在
            MenuVO parentMenu = map.get(menuVO.getParentId());
            if (parentMenu != null) {
                // 父节点存在，则将该节点添加到父节点的子节点集合中
                parentMenu.getChildren().add(menuVO);
            }
        }
        return treeList;
    }


}
