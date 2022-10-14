package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/5/1911:58
 */
@Data
@TableName("tb_mall_news")
public class NewsDTO implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private String auth;
    private String imgUrl;
    private int readCount;
    private String status;
    private Date createTime;
    private String sort;

}
