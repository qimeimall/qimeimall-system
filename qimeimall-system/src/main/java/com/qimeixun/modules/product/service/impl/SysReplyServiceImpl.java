package com.qimeixun.modules.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.ReplyMapper;
import com.qimeixun.modules.product.service.SysReplyService;
import com.qimeixun.po.ReplyDTO;
import com.qimeixun.ro.SysReplayRO;
import com.qimeixun.vo.SysReplayVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class SysReplyServiceImpl implements SysReplyService {

    @Resource
    ReplyMapper replyMapper;

    @Override
    public IPage<SysReplayVO> selectSysReplyList(SysReplayRO sysReplayRO) {
        Page page = new Page<SysReplayVO>(sysReplayRO.getCurrentPage(), sysReplayRO.getPageSize());
        IPage<SysReplayVO> list = replyMapper.selectSysReplayList(page, sysReplayRO);
        return list;
    }

    @Override
    public int updateSysReply(ReplyDTO replyDTO) {
        ReplyDTO reply = replyMapper.selectById(replyDTO.getId());
        if(reply == null){
            throw new ServiceException("评论不存在");
        }
        reply.setMerchantReplyContent(replyDTO.getMerchantReplyContent());
        reply.setMerchantReplyTime(new Date());
        return replyMapper.updateById(reply);
    }

    @Override
    public int deleteReply(String id) {
        ReplyDTO reply = replyMapper.selectById(id);
        if(reply == null){
            throw new ServiceException("评论不存在");
        }
        reply.setIsDel("1");
        return (replyMapper.updateById(reply));
    }
}
