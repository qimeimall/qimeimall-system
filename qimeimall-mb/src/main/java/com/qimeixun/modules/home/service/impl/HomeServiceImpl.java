package com.qimeixun.modules.home.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.mapper.*;
import com.qimeixun.modules.home.service.HomeService;
import com.qimeixun.modules.product.Service.ProductService;
import com.qimeixun.po.*;
import com.qimeixun.vo.HotStoreVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/5/2015:23
 */
@Service
public class HomeServiceImpl implements HomeService {

    @Resource
    HomeSetMapper homeSetMapper;

    @Resource
    NavbarMapper navbarMapper;

    @Resource
    BannerMapper bannerMapper;

    @Resource
    NoticeMapper noticeMapper;

    @Resource
    NewsMapper newsMapper;

    @Resource
    StoreMapper storeMapper;

    @Resource
    ProductService productService;

    @Override
    public Map<String, Object> selectHomeMenuList() {
        //查询首页界面配置
        List<HomeSetDTO> list = homeSetMapper.selectList(new QueryWrapper<>());
        //查询首页功能图标
        List<NavbarDTO> navbars = getNavbar();
        //查询轮播图
        List<BannerDTO> banners = getBanner();
        //查询新闻
        List<NewsDTO> news = getNews();
        //查询公告
        List<NoticeDTO> notices = getNotice();

        //查询热门产品
        List<ProductDTO> hotProducts = productService.selectProductHot("0");

        //查询推荐商品
        List<ProductDTO> recommendProducts = productService.selectProductRecommend("0");

        //查询热门店铺列表
        List<HotStoreVO> hotStoreList = getHotStoreList();

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("navbars", navbars);
        map.put("banners", banners);
        map.put("news", news);
        map.put("notices", notices);
        map.put("hotProducts", hotProducts);
        map.put("recommendProducts", recommendProducts);
        map.put("hotStoreList", hotStoreList);
        return map;
    }

    private List<NavbarDTO> getNavbar() {
        QueryWrapper<NavbarDTO> navbarDTOQueryWrapper = new QueryWrapper<>();
        navbarDTOQueryWrapper.eq("status", "0");
        navbarDTOQueryWrapper.orderByAsc("sort");
        List<NavbarDTO> navbarDTOS = navbarMapper.selectList(navbarDTOQueryWrapper);
        return navbarDTOS;
    }

    private List<BannerDTO> getBanner() {
        QueryWrapper<BannerDTO> bannerDTOQueryWrapper = new QueryWrapper<>();
        bannerDTOQueryWrapper.eq("status", "0");
        bannerDTOQueryWrapper.orderByAsc("sort");
        List<BannerDTO> banners = bannerMapper.selectList(bannerDTOQueryWrapper);
        return banners;
    }

    private List<NewsDTO> getNews() {
        QueryWrapper<NewsDTO> bannerDTOQueryWrapper = new QueryWrapper<>();
        bannerDTOQueryWrapper.eq("status", "0");
        bannerDTOQueryWrapper.orderByAsc("sort");
        List<NewsDTO> news = newsMapper.selectList(bannerDTOQueryWrapper);
        return news;
    }

    private List<NoticeDTO> getNotice() {
        QueryWrapper<NoticeDTO> bannerDTOQueryWrapper = new QueryWrapper<>();
        bannerDTOQueryWrapper.eq("status", "0");
        bannerDTOQueryWrapper.orderByAsc("sort");
        List<NoticeDTO> notices = noticeMapper.selectList(bannerDTOQueryWrapper);
        return notices;
    }

    private List<HotStoreVO> getHotStoreList() {
        return storeMapper.getHotStoreList();
    }

}
