package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qimeixun.entity.Store;
import com.qimeixun.vo.HotStoreVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/414:14
 */
@Mapper
public interface StoreMapper extends BaseMapper<Store> {
    List<HotStoreVO> getHotStoreList();
}
