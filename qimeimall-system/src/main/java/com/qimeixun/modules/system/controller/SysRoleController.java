package com.qimeixun.modules.system.controller;

import com.qimeixun.ro.SysRoleRO;
import com.qimeixun.ro.SysRoleResourceRO;
import com.qimeixun.modules.system.service.SysRoleService;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 系统角色
 * @author wangdaqiang
 * @date 2019-08-23 20:01
 */
@Api(tags = "后台-角色接口")
@Controller
@RequestMapping(value = "/sys/role")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;


    ////@LoginCheck(permission = "sys:role:insert")
    @ApiOperation(value = "新增角色", notes = "新增角色")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
   @ResponseBody
    public ResponseResultVO insert(@RequestBody SysRoleRO pageRO) {
        return this.sysRoleService.insert(pageRO);
    }


    ////@LoginCheck(permission = "sys:role:update")
    @ApiOperation(value = "修改角色", notes = "修改角色")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResultVO update(@RequestBody SysRoleRO pageRO) {
        return this.sysRoleService.update(pageRO);
    }


    ////@LoginCheck(permission = "sys:role:delete")
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResultVO delete(@RequestParam(name = "id") String id) {
        return this.sysRoleService.delete(id);
    }


    ////@LoginCheck(permission = "sys:role:select")
    @ApiOperation(value = "查询角色列表", notes = "查询角色列表")
    @RequestMapping(value = "/selectList", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResultVO selectList(@RequestBody SysRoleRO pageRO) {
        return this.sysRoleService.selectList(pageRO);
    }


    ////@LoginCheck(permission = "sys:role:update")
    @ApiOperation(value = "修改角色权限", notes = "修改角色权限")
    @RequestMapping(value = "/updateSysRoleResource", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResultVO updateSysRoleResource(@RequestBody SysRoleResourceRO ro) {
        return this.sysRoleService.updateSysRoleResource(ro);
    }


    ////@LoginCheck(permission = "sys:role:select")
    @ApiOperation(value = "查询角色权限列表", notes = "查询角色权限列表")
    @RequestMapping(value = "/selectSysRoleResourceList", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResultVO selectSysRoleResourceList(@RequestParam(name = "id") String id) {
        return this.sysRoleService.selectSysRoleResourceList(id);
    }


    ////@LoginCheck(permission = "sys:role:update")
//    @ApiOperation(value = "修改角色停车场权限", notes = "修改角色停车场权限")
//    @RequestMapping(value = "/updateRoleParkPermission", method = RequestMethod.POST)
//    @ResponseBody
//    public ResponseResultVO updateRoleParkPermission(@RequestBody SysRoleParkRO ro) {
//        return this.sysRoleService.updateRoleParkPermission(ro);
//    }



}
