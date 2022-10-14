package com.qimeixun.ro;

import lombok.Data;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/2515:07
 */
@Data
public class AddressRO {

    private Long id;

    private String name;

    private String phone;

    private String address;

    private String isDefault;

    private String userId;

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
