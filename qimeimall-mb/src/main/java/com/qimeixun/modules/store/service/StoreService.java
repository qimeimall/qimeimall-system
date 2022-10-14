package com.qimeixun.modules.store.service;

import com.qimeixun.entity.Store;
import com.qimeixun.ro.NearStoreRO;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/6/1110:31
 */
public interface StoreService {
    Store selectStoreInfoById(String storeId);

    List selectNearStoreList(NearStoreRO nearStoreRO);
}
