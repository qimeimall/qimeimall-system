package com.qimeixun.modules.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qimeixun.mapper.CouponMapper;
import com.qimeixun.modules.coupon.service.CouponService;
import com.qimeixun.po.CouponDTO;
import com.qimeixun.ro.CouponPageRO;
import com.qimeixun.ro.CouponRO;
import com.qimeixun.util.TokenUtil;
import com.qimeixun.vo.CouponListVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/2921:28
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, CouponDTO> implements CouponService {

    @Resource
    CouponMapper couponMapper;

    @Resource
    TokenUtil tokenUtil;

    @Override
    public int insertCoupon(CouponRO couponRO) {
        CouponDTO couponDTO = getCouponDTO(couponRO);
        return couponMapper.insert(couponDTO);
    }

    @Override
    public IPage<CouponListVO> selectCouponList(CouponPageRO couponPageRO) {
        couponPageRO.setStoreId(tokenUtil.getSysUserInfo().getStoreId());
        Page page = new Page<CouponListVO>(couponPageRO.getCurrentPage(), couponPageRO.getPageSize());
        IPage<CouponListVO> iPage = couponMapper.selectCouponList(page, couponPageRO);
        return iPage;
    }

    @Override
    public CouponDTO selectCouponById(String id) {
        CouponDTO couponDTO = couponMapper.selectById(id);
        List<Integer> ids = null;
        if (StringUtils.isNotEmpty(couponDTO.getEffectProductId())) {
            ids = JSON.parseArray(couponDTO.getEffectProductId(), Integer.class);
        }
        if ("0".equals(couponDTO.getType())) {
            //对产品分类有效
            if (ids != null) couponDTO.setProductClass(ids);
        }
        if ("1".equals(couponDTO.getType())) {
            //对部分产品有效
            if (ids != null) couponDTO.setProduct(ids);
        }
        return couponDTO;
    }

    @Override
    public int updateCoupon(CouponRO couponRO) {
        CouponDTO couponDTO = getCouponDTO(couponRO);
        return couponMapper.updateById(couponDTO);
    }

    private CouponDTO getCouponDTO(CouponRO couponRO) {
        CouponDTO couponDTO = new CouponDTO();
        BeanUtils.copyProperties(couponRO, couponDTO);
        couponDTO.setStoreId(tokenUtil.getSysUserInfo().getStoreId());
        if ("0".equals(couponRO.getType())) {
            couponDTO.setEffectProductId(JSON.toJSONString(couponRO.getProductClass()));
        } else if ("1".equals(couponRO.getType())) {
            couponDTO.setEffectProductId(JSON.toJSONString(couponRO.getProduct()));
        }else{
            couponDTO.setEffectProductId(JSON.toJSONString(new ArrayList<>()));
        }
        return couponDTO;
    }
}
