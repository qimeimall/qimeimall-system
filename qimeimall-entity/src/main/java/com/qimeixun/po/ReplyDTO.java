package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_product_reply")
public class ReplyDTO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String userId;
    private String orderId;
    @TableField("`unique`")
    private String unique;
    private String productId;
    private String replyType;
    private String productScore;
    private String serviceScore;
    @TableField("`comment`")
    private String comment;
    private String pics;
    private Date createTime;
    private Date updateTime;
    private String merchantReplyContent;
    private Date merchantReplyTime;
    private String isDel;
    private String isReply;

    private String attrValue;

    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private String headImg;

}
