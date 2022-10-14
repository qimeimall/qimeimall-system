package com.qimeixun.modules.address.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.AddressMapper;
import com.qimeixun.modules.address.service.AddressService;
import com.qimeixun.po.AddressDTO;
import com.qimeixun.ro.AddressRO;
import com.qimeixun.util.TokenUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/2515:04
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    AddressMapper addressMapper;

    @Resource
    TokenUtil tokenUtil;

    @Override
    @Transactional
    public int insertAddress(AddressRO addressRO) {
        AddressDTO addressDTO = new AddressDTO();
        BeanUtils.copyProperties(addressRO, addressDTO);
        addressDTO.setUserId(tokenUtil.getUserIdByToken());

        if ("1".equals(addressRO.getIsDefault())) {
            //该用户其他地址 默认置0
            addressMapper.updateAddressDefault(tokenUtil.getUserIdByToken());
        }
        int i = addressMapper.insert(addressDTO);
        return i;
    }

    @Override
    public List<AddressDTO> selectAddressList() {
        QueryWrapper<AddressDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", tokenUtil.getUserIdByToken());
        queryWrapper.eq("is_delete", "0");
        queryWrapper.orderByDesc("is_default");
        queryWrapper.orderByDesc("create_time");
        return addressMapper.selectList(queryWrapper);
    }

    @Override
    public AddressDTO selectAddressById(String id) {
        AddressDTO addressDTO = addressMapper.selectById(id);
        String userId = tokenUtil.getUserIdByToken();
        if (ObjectUtil.isNotNull(addressDTO) && userId.equals(addressDTO.getUserId())) {
            return addressDTO;
        } else {
            throw new ServiceException("查询地址不存在");
        }
    }

    @Override
    @Transactional
    public int updateAddress(AddressRO addressRO) {
        QueryWrapper<AddressDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", tokenUtil.getUserIdByToken());
        queryWrapper.eq("id", addressRO.getId());
        AddressDTO addressDTO = new AddressDTO();
        BeanUtils.copyProperties(addressRO, addressDTO);
        if ("1".equals(addressRO.getIsDefault())) {
            //该用户其他地址 默认置0
            addressMapper.updateAddressDefault(tokenUtil.getUserIdByToken());
        }
        return addressMapper.update(addressDTO, queryWrapper);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public int deleteAddressById(String id) {
        AddressDTO addressDTO = addressMapper.selectById(id);
        if (ObjectUtil.isNull(addressDTO)) {
            throw new ServiceException("该地址不存在");
        }
        if (!tokenUtil.getUserIdByToken().equals(addressDTO.getUserId())) {
            throw new ServiceException("无权操作");
        }
        addressDTO.setIsDelete(1);
        return addressMapper.updateById(addressDTO);

    }
}
