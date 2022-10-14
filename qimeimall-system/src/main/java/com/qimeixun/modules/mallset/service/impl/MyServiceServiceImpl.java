package com.qimeixun.modules.mallset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.mapper.MyServiceMapper;
import com.qimeixun.modules.mallset.service.MyServiceService;
import com.qimeixun.modules.system.service.impl.CommonService;
import com.qimeixun.po.MyServiceDTO;
import com.qimeixun.ro.PageRO;
import com.qimeixun.modules.system.service.impl.CommonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/7/411:04
 */
@Service
public class MyServiceServiceImpl extends CommonService implements MyServiceService {

    @Resource
    MyServiceMapper myServiceMapper;

    @Override
    public IPage<MyServiceDTO> selectMyServiceList(PageRO pageRO) {
        QueryWrapper<MyServiceDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        IPage<MyServiceDTO> page = myServiceMapper.selectPage(new Page<>(pageRO.getCurrentPage(), pageRO.getPageSize()), queryWrapper);
        return page;
    }

    @Override
    public int deleteMyServiceById(String id) {
        return myServiceMapper.deleteById(id);
    }

    @Override
    public int updateMyService(MyServiceDTO myServiceDTO) {
        return myServiceMapper.updateById(myServiceDTO);
    }

    @Override
    public int insertMyService(MyServiceDTO myServiceDTO) {
        return myServiceMapper.insert(myServiceDTO);
    }

    @Override
    public MyServiceDTO selectMyServiceById(String id) {
        return myServiceMapper.selectById(id);
    }
}
