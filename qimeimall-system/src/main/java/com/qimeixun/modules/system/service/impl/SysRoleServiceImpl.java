package com.qimeixun.modules.system.service.impl;

import com.qimeixun.entity.SysResource;
import com.qimeixun.entity.SysRole;
import com.qimeixun.mapper.SysResourceMapper;
import com.qimeixun.mapper.SysRoleMapper;
import com.qimeixun.ro.PageRO;
import com.qimeixun.ro.SysRoleRO;
import com.qimeixun.ro.SysRoleResourceRO;
import com.qimeixun.modules.system.service.SysRoleService;
import com.qimeixun.util.Tool;
import com.qimeixun.vo.ResponseResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统角色接口实现
 *
 * @author wangdaqiang
 * @date 2019-08-24 15:55
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    private static final Logger logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);


    @Resource
    private SysRoleMapper sysRoleMapper;
    //    @Resource
//    private SysOperateLogService sysOperateLogService;
    @Resource
    private SysResourceMapper sysResourceMapper;


    @Override
    public ResponseResultVO insert(SysRoleRO sysRoleRO) {
        try {
            if (StringUtils.isBlank(sysRoleRO.getRoleName())) {
                return ResponseResultVO.failResult("角色名称不能为空");
            }
            SysRole sysRole = new SysRole();
            sysRole.setId(Tool.getPrimaryKey());
            sysRole.setRoleName(sysRoleRO.getRoleName());
            sysRole.setDescription(sysRoleRO.getDescription());
            this.sysRoleMapper.insert(sysRole);
            // 记录操作日志
            //this.sysOperateLogService.insertAddLog(SysModuleType.SYS_ROLE, sysRole.getId(), "新增-系统角色");

            return ResponseResultVO.successResult();
        } catch (Exception e) {
            logger.error("新增-系统角色-异常", e);
            return ResponseResultVO.errorResult();
        }
    }

    @Override
    public ResponseResultVO update(SysRoleRO sysRoleRO) {
        try {
            if (StringUtils.isBlank(sysRoleRO.getId())) {
                return ResponseResultVO.failResult("角色id不能为空");
            }
            SysRole oldSysRole = this.sysRoleMapper.selectById(sysRoleRO.getId());
            if (oldSysRole == null) {
                return ResponseResultVO.failResult("角色不存在");
            }
            SysRole sysRole = new SysRole();
            sysRole.setId(sysRoleRO.getId());
            if (StringUtils.isNotBlank(sysRoleRO.getRoleName())) {
                sysRole.setRoleName(sysRoleRO.getRoleName());
            }
            if (StringUtils.isNotBlank(sysRoleRO.getDescription())) {
                sysRole.setDescription(sysRoleRO.getDescription());
            }
            int num = this.sysRoleMapper.update(sysRole);
            if (num <= 0) {
                return ResponseResultVO.failResult();
            }
            // 记录操作日志
            //this.sysOperateLogService.insertUpdateLog(SysModuleType.SYS_ROLE, sysRole.getId(), "修改-系统角色", oldSysRole, sysRole);

            return ResponseResultVO.successResult();
        } catch (Exception e) {
            logger.error("修改-系统角色-异常", e);
            return ResponseResultVO.errorResult();
        }
    }

    @Override
    public ResponseResultVO delete(String id) {
        try {
            int num = this.sysRoleMapper.delete(id);
            if (num <= 0) {
                return ResponseResultVO.failResult();
            }
            // 记录操作日志
            //this.sysOperateLogService.insertDeleteLog(SysModuleType.SYS_ROLE, id, "删除-系统角色");

            return ResponseResultVO.successResult();
        } catch (Exception e) {
            logger.error("删除-系统角色-异常", e);
            return ResponseResultVO.errorResult();
        }
    }

    @Override
    public ResponseResultVO selectList(SysRoleRO sysRoleRO) {
        try {
            PageRO page = new PageRO();
            page.setCurrentPage(sysRoleRO.getCurrentPage());
            page.setPageSize(sysRoleRO.getPageSize());

            Map<String, Object> params = new HashMap<>();
            params.put("page", page);

            if (StringUtils.isNotBlank(sysRoleRO.getRoleName())) {
                params.put("roleName", sysRoleRO.getRoleName());
            }
            if (StringUtils.isNotBlank(sysRoleRO.getDescription())) {
                params.put("description", sysRoleRO.getDescription());
            }

            List<SysRole> list = this.sysRoleMapper.selectByParams(params);
            int count = this.sysRoleMapper.selectByParamsCount(params);

            return ResponseResultVO.resultList(list, count, sysRoleRO);
        } catch (Exception e) {
            logger.error("查询-系统角色列表-异常", e);
            return ResponseResultVO.errorResult();
        }
    }

    @Override
    public ResponseResultVO updateSysRoleResource(SysRoleResourceRO ro) {
        try {
            if (StringUtils.isBlank(ro.getRoleId())) {
                return ResponseResultVO.failResult("角色id不能为空");
            }
            if (CollectionUtils.isEmpty(ro.getResourceIds())) {
                return ResponseResultVO.failResult("请勾选权限资源");
            }
            // 1.删除角色所有的权限资源
            this.sysRoleMapper.deleteRoleResource(ro.getRoleId());

            // 2.添加角色权限资源
            int num = this.sysRoleMapper.addRoleResource(ro);
            if (num > 0) {
                //this.sysOperateLogService.insertUpdateLog(SysModuleType.SYS_ROLE, ro.getRoleId(), "修改-角色权限资源", null, ro);
                return ResponseResultVO.successResult();
            }
            return ResponseResultVO.failResult();
        } catch (Exception e) {
            logger.error("修改-角色权限资源-异常", e);
            return ResponseResultVO.errorResult();
        }
    }

    @Override
    public ResponseResultVO selectSysRoleResourceList(String id) {
        try {
            if (StringUtils.isBlank(id)) {
                return ResponseResultVO.failResult("角色id不能为空");
            }
            List<SysResource> list = this.sysResourceMapper.selectByRoleId(id);

            return ResponseResultVO.successResult(list);
        } catch (Exception e) {
            logger.error("查询-角色权限列表-异常", e);
            return ResponseResultVO.errorResult();
        }
    }
}
