package com.qimeixun.modules.reply.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.base.BaseController;
import com.qimeixun.modules.reply.service.ReplyService;
import com.qimeixun.po.ReplyDTO;
import com.qimeixun.ro.QuerytProductReplyRO;
import com.qimeixun.ro.ReplyRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Api(tags = "评价")
@RestController
@RequestMapping(value = "/reply", produces = "application/json;charset=UTF-8")
public class ReplyController extends BaseController {


    @Resource
    ReplyService replyService;

    @ApiOperation(value = "添加评价", notes = "添加评价")
    @RequestMapping(value = "/createReply", method = RequestMethod.POST)
    public ResponseResultVO createReply(@RequestBody ReplyRO replyRO) {
        if (StrUtil.isBlank(replyRO.getOrderId()) || StrUtil.isBlank(replyRO.getProductId()) || StrUtil.isBlank(replyRO.getCartId())) {
            return ResponseResultVO.failResult("参数异常");
        }
        replyService.createReply(replyRO);
        return ResponseResultVO.successResult();
    }

    @ApiOperation(value = "查看商品的评价列表", notes = "查看商品的评价列表")
    @RequestMapping(value = "/selectReplyList", method = RequestMethod.POST)
    public ResponseResultVO selectReplyList(@RequestBody QuerytProductReplyRO querytProductReplyRO) {
        if (StrUtil.isBlank(querytProductReplyRO.getProductId())) {
            return ResponseResultVO.failResult("该商品不存在");
        }
        Page page = new Page<Map<String, Object>>(querytProductReplyRO.getCurrentPage(), querytProductReplyRO.getPageSize());
        IPage<ReplyDTO> iPage = replyService.selectReplyList(page, querytProductReplyRO);
        return getPageObject(iPage, querytProductReplyRO);
    }
}
