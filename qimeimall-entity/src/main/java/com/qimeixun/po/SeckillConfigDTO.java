package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author chenshouyang
 * @date 2020/6/1922:38
 */
@Data
@TableName("tb_seckill_config")
public class SeckillConfigDTO {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private int beginTime;

    private int continueTime;
}
