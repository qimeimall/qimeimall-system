package com.qimeixun.modules.news.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.NewsMapper;
import com.qimeixun.modules.news.service.UserNewsService;
import com.qimeixun.po.NewsDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserNewsServiceImpl implements UserNewsService {

    @Resource
    NewsMapper newsMapper;

    @Override
    public List<NewsDTO> selectNewsList() {
        return newsMapper.selectList(new QueryWrapper<NewsDTO>().lambda().eq(NewsDTO::getStatus, "0").orderByDesc(NewsDTO::getCreateTime));
    }

    @Override
    public NewsDTO selectNewsById(String id) {
        if(StrUtil.isBlank(id)){
            throw new ServiceException("id不能为空");
        }
        NewsDTO newsDTO = newsMapper.selectById(id);
        if(newsDTO == null){
            throw new ServiceException("新闻不存在");
        }
        NewsDTO dto = new NewsDTO();
        dto.setId(newsDTO.getId());
        dto.setReadCount(newsDTO.getReadCount() + 1);
        newsMapper.updateById(dto);
        return newsDTO;
    }
}
