package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chenshouyang
 * @date 2020/5/1614:28
 */
@Data
@TableName("tb_mall_home_set")
public class HomeSetDTO implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String nodeId;

    private String title;

    private String height;

    private String url;
}
