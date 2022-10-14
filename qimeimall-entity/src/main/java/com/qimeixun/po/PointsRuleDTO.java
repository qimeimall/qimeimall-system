package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_sign_points_rule")
public class PointsRuleDTO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String daysKey;
    private BigDecimal points;
    private Integer sortNo;
    private Integer isShow;
    private Date createTime;
}
