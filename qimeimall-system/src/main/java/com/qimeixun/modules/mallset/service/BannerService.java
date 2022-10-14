package com.qimeixun.modules.mallset.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qimeixun.po.BannerDTO;
import com.qimeixun.ro.PageRO;
import com.qimeixun.vo.ResponseResultVO;

/**
 * @author chenshouyang
 * @date 2020/5/1718:53
 */
public interface BannerService {
    IPage<BannerDTO> selectBannerList(PageRO pageRO);

    int deleteBannerById(String id);

    int updateBanner(BannerDTO bannerDTO);

    int insertBanner(BannerDTO bannerDTO);

    BannerDTO selectBannerById(String id);
}
