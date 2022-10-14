package com.qimeixun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/5/414:05
 */
@Data
@TableName("tb_store")
public class Store {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String storeName;
    private String storeLogo;
    private String storeTell;
    private String storeAddress;
    private double longitude;
    private double latitude;
    private Date writeOffTime;
    private String businessTime;
    private Date createTime;
    private Date updateTime;
    private Date settledInTime;
    private String status;
    private String isDelete;
    private String isHot;

    @TableField(exist = false)
    private double distance;

}
