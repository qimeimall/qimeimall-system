package com.qimeixun.modules.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qimeixun.po.ReplyDTO;
import com.qimeixun.ro.SysReplayRO;

public interface SysReplyService {
    IPage selectSysReplyList(SysReplayRO sysReplayRO);

    int updateSysReply(ReplyDTO replyDTO);

    int deleteReply(String id);
}
