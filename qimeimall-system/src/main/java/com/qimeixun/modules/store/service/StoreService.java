package com.qimeixun.modules.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qimeixun.entity.Store;
import com.qimeixun.ro.StoreRO;
import com.qimeixun.vo.ResponseResultVO;

/**
 * @author chenshouyang
 * @date 2020/5/414:00
 */
public interface StoreService {
    IPage<Store> selectStoreList(StoreRO storeRO);

    int deleteStore(Long id);

    int insertStore(Store store);

    Store selectStoreById(String id);

    int updateStore(Store store);
}
