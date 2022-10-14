package com.qimeixun.modules.system.controller;

import com.qimeixun.entity.SysResource;
import com.qimeixun.ro.MenuRO;
import com.qimeixun.ro.SysResourcePageRO;
import com.qimeixun.ro.SysResourceRO;
import com.qimeixun.modules.system.service.SysResourceService;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 系统资源
 * @author wangdaqiang
 * @date 2019-08-23 20:01
 */
@Api(tags = "后台-系统资源接口(菜单、按钮、权限)")
@Controller
@RequestMapping(value = "/sys/resource")
public class SysResourceController {

    @Resource
    private SysResourceService sysResourceService;


    //@LoginCheck(permission = "sys:resource:insert")
    @ApiOperation(value = "新增资源", notes = "新增资源")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResultVO insert(@RequestBody SysResource sysResource) {
        return this.sysResourceService.insert(sysResource);
    }


    //@LoginCheck(permission = "sys:resource:update")
    @ApiOperation(value = "修改资源", notes = "修改资源")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResultVO update(@RequestBody SysResource sysResource) {
        return this.sysResourceService.update(sysResource);
    }


    //@LoginCheck(permission = "sys:resource:delete")
    @ApiOperation(value = "删除资源", notes = "删除资源")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResultVO delete(@RequestParam(name = "id") String id) {
        return this.sysResourceService.delete(id);
    }


    //@LoginCheck(permission = "sys:resource:select")
    @ApiOperation(value = "查询资源列表", notes = "查询资源列表")
    @RequestMapping(value = "/selectSysResourceList", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResultVO selectSysResourceList(@RequestBody SysResourcePageRO pageRO) {
        return this.sysResourceService.selectSysResourceList(pageRO);
    }


    //@LoginCheck(permission = "sys:resource:select")
    @ApiOperation(value = "查询资源树形列表", notes = "查询资源树形列表")
    @RequestMapping(value = "/selectTreeList", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResultVO selectTreeList(@RequestBody(required = false) SysResourceRO ro) {
        return this.sysResourceService.selectTreeList(ro);
    }


    //@LoginCheck
    @ApiOperation(value = "菜单初始化", notes = "菜单初始化")
    @RequestMapping(value = "/menuInit", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResultVO menuInit() {
        return this.sysResourceService.menuInit();
    }


    //@LoginCheck
    @ApiOperation(value = "查询菜单按钮列表", notes = "查询菜单下的按钮列表")
    @RequestMapping(value = "/selectMenuBtnList", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResultVO selectMenuBtnList(@RequestBody MenuRO ro) {
        return this.sysResourceService.selectMenuBtnList(ro);
    }


}
