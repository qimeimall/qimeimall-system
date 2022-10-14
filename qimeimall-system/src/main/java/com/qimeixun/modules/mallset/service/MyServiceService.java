package com.qimeixun.modules.mallset.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qimeixun.po.MyServiceDTO;
import com.qimeixun.ro.PageRO;
import com.qimeixun.vo.ResponseResultVO;

/**
 * @author chenshouyang
 * @date 2020/7/411:04
 */
public interface MyServiceService {
    IPage<MyServiceDTO> selectMyServiceList(PageRO pageRO);

    int deleteMyServiceById(String id);

    int updateMyService(MyServiceDTO myServiceDTO);

    int insertMyService(MyServiceDTO myServiceDTO);

    MyServiceDTO selectMyServiceById(String id);
}
