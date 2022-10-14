package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("tb_user_collection")
@Data
public class UserCollectionDTO {

    private Long id;

    private Long productId;

    private String userId;

    @TableField("`unque`")
    private String unque;

    private Date createTime;

}
