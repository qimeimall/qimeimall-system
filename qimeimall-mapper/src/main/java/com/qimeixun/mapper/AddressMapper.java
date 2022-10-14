package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qimeixun.po.AddressDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chenshouyang
 * @date 2020/5/2516:04
 */
@Mapper
public interface AddressMapper extends BaseMapper<AddressDTO> {
    void updateAddressDefault(String userId);
}
