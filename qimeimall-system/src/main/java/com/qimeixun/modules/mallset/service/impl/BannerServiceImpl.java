package com.qimeixun.modules.mallset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.mapper.BannerMapper;
import com.qimeixun.modules.mallset.service.BannerService;
import com.qimeixun.modules.system.service.impl.CommonService;
import com.qimeixun.po.BannerDTO;
import com.qimeixun.ro.PageRO;
import com.qimeixun.modules.system.service.impl.CommonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chenshouyang
 * @date 2020/5/1718:54
 */
@Service
public class BannerServiceImpl extends CommonService implements BannerService {

    @Resource
    BannerMapper bannerMapper;

    @Override
    public IPage<BannerDTO> selectBannerList(PageRO pageRO) {
        QueryWrapper<BannerDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        IPage<BannerDTO> page = bannerMapper.selectPage(new Page<>(pageRO.getCurrentPage(), pageRO.getPageSize()),
                queryWrapper);
        return page;
    }

    @Override
    public int deleteBannerById(String id) {
        return bannerMapper.deleteById(id);
    }

    @Override
    public int updateBanner(BannerDTO bannerDTO) {
        return bannerMapper.updateById(bannerDTO);
    }

    @Override
    public int insertBanner(BannerDTO bannerDTO) {
        return bannerMapper.insert(bannerDTO);
    }

    @Override
    public BannerDTO selectBannerById(String id) {
        return bannerMapper.selectById(id);
    }
}
