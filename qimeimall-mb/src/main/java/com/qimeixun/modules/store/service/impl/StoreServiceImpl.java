package com.qimeixun.modules.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.entity.Store;
import com.qimeixun.mapper.StoreMapper;
import com.qimeixun.modules.store.service.StoreService;
import com.qimeixun.ro.NearStoreRO;
import com.qimeixun.util.PositionUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/6/1110:32
 */
@Service
public class StoreServiceImpl implements StoreService {

    @Resource
    StoreMapper storeMapper;

    @Override
    public Store selectStoreInfoById(String storeId) {
        return storeMapper.selectById(storeId);
    }

    @Override
    public List selectNearStoreList(NearStoreRO nearStoreRO) {
        List<Store> stores = storeMapper.selectList(new QueryWrapper<Store>().eq("is_delete", 0).eq("is_delete", 0).eq("status", 0));
        for (Store store : stores) {
            double dist = PositionUtil.getDistance(nearStoreRO.getLatitude(), nearStoreRO.getLongitude(), store.getLatitude(), store.getLongitude());
            store.setDistance(dist);
        }
        return stores;
    }
}
