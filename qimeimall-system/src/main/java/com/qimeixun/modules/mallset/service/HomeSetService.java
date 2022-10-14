package com.qimeixun.modules.mallset.service;

import com.qimeixun.po.HomeSetDTO;
import com.qimeixun.ro.HomeSetRO;
import com.qimeixun.vo.ResponseResultVO;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/1614:26
 */
public interface HomeSetService {
    void updateHomeSet(List<HomeSetRO> homeSetROS);

    List<HomeSetDTO> selectHomeSetList();
}
