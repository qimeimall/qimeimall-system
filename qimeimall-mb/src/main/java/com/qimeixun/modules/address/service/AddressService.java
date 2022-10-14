package com.qimeixun.modules.address.service;

import com.qimeixun.po.AddressDTO;
import com.qimeixun.ro.AddressRO;
import com.qimeixun.vo.ResponseResultVO;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/2515:04
 */
public interface AddressService {
    int insertAddress(AddressRO addressRO);

    List<AddressDTO> selectAddressList();

    AddressDTO selectAddressById(String id);

    int updateAddress(AddressRO addressRO);

    int deleteAddressById(String id);
}
