package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/6/315:35
 */
@Data
@TableName("tb_express")
public class ExpressDTO {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String expressName;
    private String expressCode;
    private String expressApi;
    private Date createTime;
    private int isDelete;

}
