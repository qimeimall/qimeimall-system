package com.qimeixun.modules.product.controller;


import cn.hutool.core.util.StrUtil;
import com.qimeixun.base.BaseController;
import com.qimeixun.modules.product.service.SysReplyService;
import com.qimeixun.po.ReplyDTO;
import com.qimeixun.ro.SysReplayRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "评论管理")
@RestController
@RequestMapping(value = "/sysReply", produces = "application/json;charset=UTF-8")
public class SysReplyController extends BaseController {

    @Resource
    SysReplyService sysReplayService;

    @ApiOperation(value = "查询评论列表", notes = "查询评论列表")
    @RequestMapping(value = "/selectSysReplyList", method = RequestMethod.POST)
    //@LoginCheck
    public ResponseResultVO selectSysReplyList(@RequestBody SysReplayRO sysReplayRO) {
        return getPageObject(sysReplayService.selectSysReplyList(sysReplayRO), sysReplayRO);
    }

    @ApiOperation(value = "回复评论", notes = "回复评论")
    @RequestMapping(value = "/updateSysReply", method = RequestMethod.POST)
    //@LoginCheck
    public ResponseResultVO updateSysReply(@RequestBody ReplyDTO replyDTO) {
        if (replyDTO.getId() == null) {
            return ResponseResultVO.failResult("id不能为空");
        }
        if (StrUtil.isBlank(replyDTO.getMerchantReplyContent())) {
            return ResponseResultVO.failResult("回复内容不能为空");
        }
        return updateResult(sysReplayService.updateSysReply(replyDTO));
    }

    @ApiOperation(value = "删除评论", notes = "删除评论")
    @RequestMapping(value = "/deleteReply", method = RequestMethod.GET)
    //@LoginCheck
    public ResponseResultVO deleteReply(@RequestParam String id) {
        return updateResult(sysReplayService.deleteReply(id));
    }
}
