package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/5/1723:05
 */
@Data
@TableName("tb_mall_navbar")
public class NavbarDTO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String imgUrl;
    private String wxLink;
    private String h5Link;
    private String status;
    private String sort;
    private Date createTime;
    private String type;
    private String navbarDetail;

    private Integer menuId;
}
