package com.qimeixun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/5/319:54
 */
@Data
@TableName("tb_material_img")
public class MaterialImg {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String imgUrl;
    private int isDelete;
    private Integer groupId;
    private Date createTime;

}
