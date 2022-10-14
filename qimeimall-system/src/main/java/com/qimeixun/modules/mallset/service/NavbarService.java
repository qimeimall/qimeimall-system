package com.qimeixun.modules.mallset.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qimeixun.po.NavbarDTO;
import com.qimeixun.ro.PageRO;
import com.qimeixun.vo.ResponseResultVO;

/**
 * @author chenshouyang
 * @date 2020/5/1723:09
 */
public interface NavbarService {
    IPage<NavbarDTO> selectNavbarList(PageRO pageRO);

    int deleteNavbarById(String id);

    int updateNavbar(NavbarDTO navbarDTO);

    int insertNavbar(NavbarDTO navbarDTO);

    NavbarDTO selectNavbarById(String id);
}
