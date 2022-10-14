package com.qimeixun.modules.mallset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.mapper.NavbarMapper;
import com.qimeixun.modules.mallset.service.NavbarService;
import com.qimeixun.po.NavbarDTO;
import com.qimeixun.ro.PageRO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/5/1723:11
 */
@Service
public class NavbarServiceImpl implements NavbarService {

    @Resource
    NavbarMapper navbarMapper;

    @Override
    public IPage<NavbarDTO> selectNavbarList(PageRO pageRO) {
        QueryWrapper<NavbarDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        IPage<NavbarDTO> page = navbarMapper.selectPage(new Page<>(pageRO.getCurrentPage(), pageRO.getPageSize()),
                queryWrapper);
        return page;
    }

    @Override
    public int deleteNavbarById(String id) {
        return navbarMapper.deleteById(id);
    }

    @Override
    public int updateNavbar(NavbarDTO navbarDTO) {
        return navbarMapper.updateById(navbarDTO);
    }

    @Override
    public int insertNavbar(NavbarDTO navbarDTO) {
        return navbarMapper.insert(navbarDTO);
    }

    @Override
    public NavbarDTO selectNavbarById(String id) {
        return navbarMapper.selectById(id);
    }
}
