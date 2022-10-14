package com.qimeixun.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.entity.Store;
import com.qimeixun.entity.SysUser;
import com.qimeixun.mapper.StoreMapper;
import com.qimeixun.modules.store.service.StoreService;
import com.qimeixun.ro.StoreRO;
import com.qimeixun.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/5/414:01
 */
@Service
public class StoreServiceImpl implements StoreService {

    @Resource
    StoreMapper storeMapper;

    @Resource
    TokenUtil tokenUtil;


    @Override
    public IPage<Store> selectStoreList(StoreRO storeRO) {

        SysUser sysUserInfo = tokenUtil.getSysUserInfo();
        if (!"0".equals(sysUserInfo.getStoreId())) {
            storeRO.setStoreId(String.valueOf(sysUserInfo.getStoreId()));
        }

        QueryWrapper<Store> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(storeRO.getStoreName())) {
            wrapper.like("store_name", storeRO.getStoreName());
        }
        if (StringUtils.isNotEmpty(storeRO.getStoreId())) {
            wrapper.eq("id", sysUserInfo.getStoreId());
        }
        wrapper.eq("is_delete", "0");
        wrapper.orderByDesc("create_time");
        IPage<Store> storeIPage = new Page<>(storeRO.getCurrentPage(), storeRO.getPageSize());
        IPage<Store> page = storeMapper.selectPage(storeIPage, wrapper);
        return page;
    }

    @Override
    public int deleteStore(Long id) {
        Store store = new Store();
        store.setId(id);
        store.setIsDelete("1");
        return storeMapper.updateById(store);
    }

    @Override
    public int insertStore(Store store) {
        return storeMapper.insert(store);
    }

    @Override
    public Store selectStoreById(String id) {
        return storeMapper.selectById(id);
    }

    @Override
    public int updateStore(Store store) {
        return storeMapper.updateById(store);
    }
}
