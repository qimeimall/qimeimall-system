package com.qimeixun.modules.mallset.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qimeixun.po.NoticeDTO;
import com.qimeixun.ro.PageRO;

/**
 * @author chenshouyang
 * @date 2020/5/199:58
 */
public interface NoticeService {

    IPage<NoticeDTO> selectNoticeList(PageRO pageRO);

    int deleteNoticeById(String id);

    int updateNotice(NoticeDTO navbarDTO);

    int insertNotice(NoticeDTO navbarDTO);

    NoticeDTO selectNoticeById(String id);
}
