package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author chenshouyang
 * @date 2020/5/2516:02
 */
@Data
@TableName("tb_user_address")
public class AddressDTO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String phone;

    private String address;

    private String isDefault;

    private String userId;

    private int isDelete;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String  region;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 省市区索引
     */
    private String multiIndex;
}
