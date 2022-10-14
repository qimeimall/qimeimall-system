package com.qimeixun.modules.mallset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.mapper.NoticeMapper;
import com.qimeixun.modules.mallset.service.NoticeService;
import com.qimeixun.po.NoticeDTO;
import com.qimeixun.ro.PageRO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/5/199:58
 */
@Service
public class NoticeServiceImpl implements NoticeService {
    @Resource
    NoticeMapper noticeMapper;

    @Override
    public IPage<NoticeDTO> selectNoticeList(PageRO pageRO) {
        QueryWrapper<NoticeDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        IPage<NoticeDTO> page = noticeMapper.selectPage(new Page<>(pageRO.getCurrentPage(), pageRO.getPageSize()),
                queryWrapper);
        return page;
    }

    @Override
    public int deleteNoticeById(String id) {
        return noticeMapper.deleteById(id);
    }

    @Override
    public int updateNotice(NoticeDTO noticeDTO) {
        return noticeMapper.updateById(noticeDTO);
    }

    @Override
    public int insertNotice(NoticeDTO navbarDTO) {
        return noticeMapper.insert(navbarDTO);
    }

    @Override
    public NoticeDTO selectNoticeById(String id) {
        return noticeMapper.selectById(id);
    }
}
