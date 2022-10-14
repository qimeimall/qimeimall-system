package com.qimeixun.modules.news.service;

import com.qimeixun.po.NewsDTO;

import java.util.List;

public interface UserNewsService {
    List<NewsDTO> selectNewsList();

    NewsDTO selectNewsById(String id);
}
