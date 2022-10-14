package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_user_withdrawal")
public class WithdrawalDTO {

    private String id;
    private String userId;
    private String type;
    private String account;
    private String accountName;
    private BigDecimal money;
    private String status;
    private String remark;
    private Date createTime;

    @Version
    private Integer version;

    @TableField(exist = false)
    private String nickName;

}
