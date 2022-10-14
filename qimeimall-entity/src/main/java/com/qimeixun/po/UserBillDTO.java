package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_user_bill")
public class UserBillDTO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private BigDecimal money;
    private String type;
    private String remark;
    private Date createTime;

}
