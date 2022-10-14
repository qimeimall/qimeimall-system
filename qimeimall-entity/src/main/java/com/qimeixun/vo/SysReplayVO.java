package com.qimeixun.vo;

import lombok.Data;

@Data
public class SysReplayVO {

    private Long id;
    private String userId;
    private String orderId;
    private String unique;
    private String productId;
    private String replyType;
    private String productScore;
    private String serviceScore;
    private String comment;
    private String pics;
    private String createTime;
    private String updateTime;
    private String merchantReplyContent;
    private String merchantReplyTime;
    private String isDel;
    private String isReply;

    private String attrValue;

    private String nickName;

    private String headImg;

    private String productName;
}
