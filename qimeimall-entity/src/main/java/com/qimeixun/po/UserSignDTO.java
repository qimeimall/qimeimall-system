package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_user_sign")
public class UserSignDTO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private BigDecimal number;
    private BigDecimal balance;
    private Date createTime;
    private Date updateTime;

    @Version
    private Long version;

}
