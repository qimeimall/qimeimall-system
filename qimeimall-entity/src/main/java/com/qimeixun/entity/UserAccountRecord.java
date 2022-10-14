package com.qimeixun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/5/1218:05
 */
@TableName("tb_user_acount_record")
@Data
public class UserAccountRecord {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private BigDecimal count;
    private String way;
    private String type;
    private Long userId;
    private String sysUserId;
    private String mark;
    private Date createTime;

}
