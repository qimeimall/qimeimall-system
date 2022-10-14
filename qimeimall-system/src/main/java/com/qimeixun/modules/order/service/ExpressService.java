package com.qimeixun.modules.order.service;

import com.qimeixun.po.ExpressDTO;
import com.qimeixun.ro.PageRO;
import com.qimeixun.vo.ResponseResultVO;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/6/315:39
 */
public interface ExpressService {
    List<ExpressDTO> selectExpressList(PageRO pageRO);

    int insertExpress(ExpressDTO expressDTO);

    int updateExpressById(ExpressDTO expressDTO);

    ExpressDTO selectExpressById(String id);

    int deleteExpressById(String id);
}
