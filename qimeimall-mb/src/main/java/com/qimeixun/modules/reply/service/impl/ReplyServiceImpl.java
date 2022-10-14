package com.qimeixun.modules.reply.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.mapper.ReplyMapper;
import com.qimeixun.modules.order.service.OrderService;
import com.qimeixun.modules.reply.service.ReplyService;
import com.qimeixun.po.ReplyDTO;
import com.qimeixun.ro.QuerytProductReplyRO;
import com.qimeixun.ro.ReplyRO;
import com.qimeixun.util.MD5Util;
import com.qimeixun.util.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ReplyServiceImpl implements ReplyService {


    @Resource
    ReplyMapper replyMapper;

    @Resource
    OrderService orderService;

    @Resource
    TokenUtil tokenUtil;

    @Override
    public void createReply(ReplyRO replyRO) {

        //查询订单的购买参数
        String attrValue = orderService.selectOrderAttrValue(replyRO.getCartId());
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setComment(replyRO.getComment());
        replyDTO.setOrderId(replyRO.getOrderId());
        replyDTO.setUserId(tokenUtil.getUserIdByToken());
        replyDTO.setPics(JSONUtil.toJsonStr(replyRO.getPics()));
        replyDTO.setProductId(replyRO.getProductId());
        replyDTO.setProductScore(replyRO.getProductScore());
        replyDTO.setServiceScore(replyRO.getServiceScore());
        replyDTO.setAttrValue(attrValue);
        replyDTO.setUnique(MD5Util.md5(replyRO.getOrderId() + replyRO.getProductId(), ""));
        int i = replyMapper.insert(replyDTO);

        if (i > 0) {
            orderService.updateOrderReplyStatus(replyRO.getProductId(), replyRO.getOrderId());
        }
    }

    @Override
    public IPage<ReplyDTO> selectReplyList(Page page, QuerytProductReplyRO querytProductReplyRO) {
        return replyMapper.selectReplyList(page, querytProductReplyRO);
    }
}
