package com.qimeixun.modules.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.mapper.ExpressMapper;
import com.qimeixun.modules.order.service.ExpressService;
import com.qimeixun.po.ExpressDTO;
import com.qimeixun.ro.PageRO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/6/315:39
 */
@Service
public class ExpressServiceImpl implements ExpressService {

    @Resource
    ExpressMapper expressMapper;

    @Override
    public List<ExpressDTO> selectExpressList(PageRO pageRO) {
        QueryWrapper<ExpressDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0);
        List<ExpressDTO> expressDTOS = expressMapper.selectList(queryWrapper);
        return expressDTOS;
    }

    @Override
    public int insertExpress(ExpressDTO expressDTO) {
        return expressMapper.insert(expressDTO);
    }

    @Override
    public int updateExpressById(ExpressDTO expressDTO) {
        return expressMapper.updateById(expressDTO);
    }

    @Override
    public ExpressDTO selectExpressById(String id) {
        return expressMapper.selectById(id);
    }

    @Override
    public int deleteExpressById(String id) {
        ExpressDTO expressDTO = expressMapper.selectById(id);
        expressDTO.setIsDelete(1);
        return expressMapper.updateById(expressDTO);
    }
}
