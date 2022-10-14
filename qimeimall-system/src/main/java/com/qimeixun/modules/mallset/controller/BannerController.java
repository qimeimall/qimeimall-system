package com.qimeixun.modules.mallset.controller;

import com.qimeixun.base.BaseController;
import com.qimeixun.modules.mallset.service.BannerService;
import com.qimeixun.po.BannerDTO;
import com.qimeixun.ro.PageRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/5/1718:52
 */
@Api(tags = "banner图")
@RestController
@RequestMapping(value = "/banner", produces = "application/json;charset=UTF-8")
public class BannerController extends BaseController {

    @Resource
    BannerService bannerService;

    @ApiOperation(value = "查询banner list", notes = "查询banner list")
    @RequestMapping(value = "/selectBannerList", method = RequestMethod.POST)
    public ResponseResultVO selectBannerList(@RequestBody PageRO pageRO) {
        return getPageObject(bannerService.selectBannerList(pageRO), pageRO);
    }

    @CacheEvict(value = "home", allEntries = true)
    @ApiOperation(value = "删除banner", notes = "删除banner")
    @RequestMapping(value = "/deleteBannerById", method = RequestMethod.GET)
    public ResponseResultVO deleteBannerById(@RequestParam String id) {
        return deleteResult(bannerService.deleteBannerById(id));
    }

    @CacheEvict(value = "home", allEntries = true)
    @ApiOperation(value = "修改banner", notes = "修改banner")
    @RequestMapping(value = "/updateBanner", method = RequestMethod.POST)
    public ResponseResultVO updateBanner(@RequestBody BannerDTO bannerDTO) {
        return updateResult(bannerService.updateBanner(bannerDTO));
    }

    @CacheEvict(value = "home", allEntries = true)
    @ApiOperation(value = "新增banner", notes = "新增banner")
    @RequestMapping(value = "/insertBanner", method = RequestMethod.POST)
    public ResponseResultVO insertBanner(@RequestBody BannerDTO bannerDTO) {
        if (StringUtils.isEmpty(bannerDTO.getImgUrl())) {
            return ResponseResultVO.failResult("图片不能为空");
        }
        return insertResult(bannerService.insertBanner(bannerDTO));
    }

    @ApiOperation(value = "查询banner通过id", notes = "查询banner通过id")
    @RequestMapping(value = "/selectBannerById", method = RequestMethod.GET)
    public ResponseResultVO selectBannerById(@RequestParam String id) {
        return ResponseResultVO.successResult(bannerService.selectBannerById(id));
    }
}
