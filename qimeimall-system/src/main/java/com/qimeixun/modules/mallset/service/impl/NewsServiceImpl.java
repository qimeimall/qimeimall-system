package com.qimeixun.modules.mallset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.mapper.NewsMapper;
import com.qimeixun.modules.mallset.service.NewsService;
import com.qimeixun.po.NewsDTO;
import com.qimeixun.ro.PageRO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/5/1912:05
 */
@Service
public class NewsServiceImpl implements NewsService {

    @Resource
    NewsMapper newsMapper;

    @Override
    public IPage<NewsDTO> selectNewsList(PageRO pageRO) {
        QueryWrapper<NewsDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        IPage<NewsDTO> page = newsMapper.selectPage(new Page<>(pageRO.getCurrentPage(), pageRO.getPageSize()), queryWrapper);
        return page;
    }

    @Override
    public int deleteNewsById(String id) {
        return newsMapper.deleteById(id);
    }

    @Override
    public int updateNews(NewsDTO newsDTO) {
        return newsMapper.updateById(newsDTO);
    }

    @Override
    public int insertNews(NewsDTO newsDTO) {
        return newsMapper.insert(newsDTO);
    }

    @Override
    public NewsDTO selectNewsById(String id) {
        return newsMapper.selectById(id);
    }
}
