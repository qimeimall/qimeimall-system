package com.qimeixun.modules.system.controller;

import com.qimeixun.ro.SysUserRO;
import com.qimeixun.ro.SysUserRoleRO;
import com.qimeixun.modules.system.service.SysUserService;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 管理员用户
 *
 * @author wangdaqiang
 * @date 2019-08-23 09:55
 */
@Api(tags = "后台-管理员用户接口")
@Controller
@RequestMapping(value = "/sys/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;


    ////@LoginCheck(permission = "sys:user:insert")
    @ApiOperation(value = "新增管理员用户", notes = "新增管理员用户")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResultVO insert(@RequestBody SysUserRO sysUserRO) {
        return this.sysUserService.insert(sysUserRO);
    }


    ////@LoginCheck(permission = "sys:user:delete")
    @ApiOperation(value = "删除管理员用户", notes = "删除管理员用户")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResultVO delete(@RequestParam(value = "id") String id) {
        return this.sysUserService.delete(id);
    }


    ////@LoginCheck(permission = "sys:user:update")
    @ApiOperation(value = "修改管理员用户", notes = "修改管理员用户")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResultVO update(@RequestBody SysUserRO sysUserRO) {
        return this.sysUserService.update(sysUserRO);
    }


    ////@LoginCheck(permission = "sys:user:select")
    @ApiOperation(value = "查询管理员用户详情", notes = "查询管理员用户详情")
    @RequestMapping(value = "/selectById", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResultVO selectById(@RequestParam(value = "id") String id) {
        return this.sysUserService.selectById(id);
    }


    ////@LoginCheck(permission = "sys:user:select")
    @ApiOperation(value = "查询管理员用户列表", notes = "查询管理员用户")
    @RequestMapping(value = "/selectSysUserList", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResultVO selectSysUserList(@RequestBody SysUserRO ro) {
        return this.sysUserService.selectSysUserList(ro);
    }


    ////@LoginCheck(permission = "sys:user:update")
    @ApiOperation(value = "修改管理员用户角色", notes = "修改管理员用户角色")
    @RequestMapping(value = "/updateSysUserRole", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResultVO updateSysUserRole(@RequestBody SysUserRoleRO ro) {
        return this.sysUserService.updateSysUserRole(ro);
    }


    ////@LoginCheck(permission = "sys:user:select")
    @ApiOperation(value = "查询管理员用户角色列表", notes = "查询管理员用户角色列表")
    @RequestMapping(value = "/selectSysUserRoleList", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResultVO selectSysUserRoleList(@RequestParam(value = "id") String id) {
        return this.sysUserService.selectSysUserRoleList(id);
    }



}
