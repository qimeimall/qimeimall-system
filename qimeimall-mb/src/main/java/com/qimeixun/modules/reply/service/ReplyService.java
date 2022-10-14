package com.qimeixun.modules.reply.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.po.ReplyDTO;
import com.qimeixun.ro.QuerytProductReplyRO;
import com.qimeixun.ro.ReplyRO;

import java.util.List;

public interface ReplyService {
    void createReply(ReplyRO replyRO);

    IPage<ReplyDTO> selectReplyList(Page page, QuerytProductReplyRO querytProductReplyRO);
}
