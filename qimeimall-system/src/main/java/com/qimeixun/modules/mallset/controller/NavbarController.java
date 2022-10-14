package com.qimeixun.modules.mallset.controller;

import com.qimeixun.base.BaseController;
import com.qimeixun.modules.mallset.service.NavbarService;
import com.qimeixun.po.NavbarDTO;
import com.qimeixun.po.NavbarDTO;
import com.qimeixun.ro.PageRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenshouyang
 * @date 2020/5/1722:57
 */
@Api(tags = "导航图标")
@RestController
@RequestMapping(value = "/navbar", produces = "application/json;charset=UTF-8")
public class NavbarController extends BaseController {

    @Resource
    NavbarService navbarService;

    @ApiOperation(value = "查询导航图标 list", notes = "查询导航图标 list")
    @RequestMapping(value = "/selectNavbarList", method = RequestMethod.POST)
    public ResponseResultVO selectNavbarList(@RequestBody PageRO pageRO) {
        return getPageObject(navbarService.selectNavbarList(pageRO), pageRO);
    }

    @CacheEvict(value = "home", allEntries = true)
    @ApiOperation(value = "删除导航图标", notes = "删除导航图标")
    @RequestMapping(value = "/deleteNavbarById", method = RequestMethod.GET)
    public ResponseResultVO deleteNavbarById(@RequestParam String id) {
        return deleteResult(navbarService.deleteNavbarById(id));
    }

    @CacheEvict(value = "home", allEntries = true)
    @ApiOperation(value = "修改导航图标", notes = "修改导航图标")
    @RequestMapping(value = "/updateNavbar", method = RequestMethod.POST)
    public ResponseResultVO updateNavbar(@RequestBody NavbarDTO navbarDTO) {
        return updateResult(navbarService.updateNavbar(navbarDTO));
    }

    @CacheEvict(value = "home", allEntries = true)
    @ApiOperation(value = "新增导航图标", notes = "新增导航图标")
    @RequestMapping(value = "/insertNavbar", method = RequestMethod.POST)
    public ResponseResultVO insertNavbar(@RequestBody NavbarDTO navbarDTO) {
        if (StringUtils.isEmpty(navbarDTO.getImgUrl())) {
            return ResponseResultVO.failResult("图片不能为空");
        }
        return insertResult(navbarService.insertNavbar(navbarDTO));
    }

    @ApiOperation(value = "查询导航图标通过id", notes = "查询导航图标通过id")
    @RequestMapping(value = "/selectNavbarById", method = RequestMethod.GET)
    public ResponseResultVO selectNavbarById(@RequestParam String id) {
        return ResponseResultVO.successResult(navbarService.selectNavbarById(id));
    }
}
