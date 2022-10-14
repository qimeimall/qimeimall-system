package com.qimeixun.modules.mallset.controller;

import com.qimeixun.base.BaseController;
import com.qimeixun.modules.mallset.service.NoticeService;
import com.qimeixun.po.NoticeDTO;
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
 * @date 2020/5/199:49
 */
@Api(tags = "公告管理")
@RestController
@RequestMapping(value = "/notice", produces = "application/json;charset=UTF-8")
public class NoticeController extends BaseController {

    @Resource
    NoticeService noticeService;

    @ApiOperation(value = "查询公告列表", notes = "查询公告列表")
    @RequestMapping(value = "/selectNoticeList", method = RequestMethod.POST)
    public ResponseResultVO selectNoticeList(@RequestBody PageRO pageRO) {
        return getPageObject(noticeService.selectNoticeList(pageRO), pageRO);
    }

    @CacheEvict(value = "home", allEntries = true)
    @ApiOperation(value = "删除公告", notes = "删除公告")
    @RequestMapping(value = "/deleteNoticeById", method = RequestMethod.GET)
    public ResponseResultVO deleteNoticeById(@RequestParam String id) {
        return deleteResult(noticeService.deleteNoticeById(id));
    }

    @CacheEvict(value = "home", allEntries = true)
    @ApiOperation(value = "修改公告", notes = "修改公告")
    @RequestMapping(value = "/updateNotice", method = RequestMethod.POST)
    public ResponseResultVO updateNotice(@RequestBody NoticeDTO navbarDTO) {
        return updateResult(noticeService.updateNotice(navbarDTO));
    }

    @CacheEvict(value = "home", allEntries = true)
    @ApiOperation(value = "新增公告", notes = "新增公告")
    @RequestMapping(value = "/insertNotice", method = RequestMethod.POST)
    public ResponseResultVO insertNotice(@RequestBody NoticeDTO navbarDTO) {
        if (StringUtils.isEmpty(navbarDTO.getContent())) {
            return ResponseResultVO.failResult("内容不能为空");
        }
        return insertResult(noticeService.insertNotice(navbarDTO));
    }

    @ApiOperation(value = "查询公告通过id", notes = "查询公告通过id")
    @RequestMapping(value = "/selectNoticeById", method = RequestMethod.GET)
    public ResponseResultVO selectNoticeById(@RequestParam String id) {
        return ResponseResultVO.successResult(noticeService.selectNoticeById(id));
    }
}
