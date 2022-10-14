package com.qimeixun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author chenshouyang
 * @date 2020/5/317:31
 */
@Data
@TableName("tb_material_group")
public class MaterialGroup {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer isDelete;

    private String name;

}
