package com.qimeixun.modules.mallset.controller;

import com.qimeixun.base.BaseController;
import com.qimeixun.modules.mallset.service.NewsService;
import com.qimeixun.po.NewsDTO;
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
 * @date 2020/5/1912:02
 */
@Api(tags = "新闻管理")
@RestController
@RequestMapping(value = "/news", produces = "application/json;charset=UTF-8")
public class NewsController extends BaseController {
    @Resource
    NewsService newsService;

    @ApiOperation(value = "查询新闻列表", notes = "查询新闻列表")
    @RequestMapping(value = "/selectNewsList", method = RequestMethod.POST)
    public ResponseResultVO selectNewsList(@RequestBody PageRO pageRO) {
        return getPageObject(newsService.selectNewsList(pageRO), pageRO);
    }

    @CacheEvict(value = "home", allEntries = true)
    @ApiOperation(value = "删除新闻", notes = "删除新闻")
    @RequestMapping(value = "/deleteNewsById", method = RequestMethod.GET)
    public ResponseResultVO deleteNewsById(@RequestParam String id) {
        return deleteResult(newsService.deleteNewsById(id));
    }

    @CacheEvict(value = "home", allEntries = true)
    @ApiOperation(value = "修改新闻", notes = "修改新闻")
    @RequestMapping(value = "/updateNews", method = RequestMethod.POST)
    public ResponseResultVO updateNews(@RequestBody NewsDTO navbarDTO) {
        return updateResult(newsService.updateNews(navbarDTO));
    }

    @CacheEvict(value = "home", allEntries = true)
    @ApiOperation(value = "新增新闻", notes = "新增新闻")
    @RequestMapping(value = "/insertNews", method = RequestMethod.POST)
    public ResponseResultVO insertNews(@RequestBody NewsDTO newsDTO) {
        if (StringUtils.isEmpty(newsDTO.getTitle())) {
            return ResponseResultVO.failResult("标题不能为空");
        }
        if (StringUtils.isEmpty(newsDTO.getContent())) {
            return ResponseResultVO.failResult("内容不能为空");
        }
        return insertResult(newsService.insertNews(newsDTO));
    }

    @ApiOperation(value = "查询新闻通过id", notes = "查询新闻通过id")
    @RequestMapping(value = "/selectNewsById", method = RequestMethod.GET)
    public ResponseResultVO selectNewsById(@RequestParam String id) {
        return ResponseResultVO.successResult(newsService.selectNewsById(id));
    }
}
