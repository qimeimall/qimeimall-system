package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/5/1719:07
 */
@Data
@TableName("tb_mall_banner")
public class BannerDTO implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String title;

    private String imgUrl;

    private String wxLink;

    private String h5Link;

    private int sort;

    private int status;

    private Date createTime;
}
