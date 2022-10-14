package com.qimeixun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/1312:46
 */
@Data
@TableName("tb_product_category")
public class Category {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer pid;

    private String categoryName;

    private String sort;

    private String categoryImg;

    private String advertImg;

    private String status;

    private String storeId;

    private String advertUrl;

    private Date createTime;

    @TableField(exist = false)
    private List<Category> children = new ArrayList<>();
}
