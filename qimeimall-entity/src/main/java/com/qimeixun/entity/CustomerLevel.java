package com.qimeixun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/5/1010:06
 */
@Data
@TableName("tb_user_level")
public class CustomerLevel {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String levelName;
    private String levelImg;
    private String levelBackImg;
    private double discount;
    private double price;
    private Integer validity;
    private double upgradePrice;
    private String status;
    private Date createTime;
    private Integer level;

}
