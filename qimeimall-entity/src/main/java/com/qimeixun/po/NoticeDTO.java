package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/5/1910:03
 */
@Data
@TableName("tb_mall_notice")
public class NoticeDTO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String content;
    private String wxLink;
    private String h5Link;
    private String sort;
    private Date effectTime;
    private Date createTime;
    private String status;

}
