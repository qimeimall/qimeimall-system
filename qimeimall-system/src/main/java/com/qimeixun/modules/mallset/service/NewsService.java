package com.qimeixun.modules.mallset.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qimeixun.po.NewsDTO;
import com.qimeixun.ro.PageRO;

/**
 * @author chenshouyang
 * @date 2020/5/1912:04
 */
public interface NewsService {
    IPage<NewsDTO> selectNewsList(PageRO pageRO);

    int deleteNewsById(String id);

    int updateNews(NewsDTO newsDTO);

    int insertNews(NewsDTO newsDTO);

    NewsDTO selectNewsById(String id);
}
