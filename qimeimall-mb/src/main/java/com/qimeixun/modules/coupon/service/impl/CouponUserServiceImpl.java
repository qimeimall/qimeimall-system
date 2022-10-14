package com.qimeixun.modules.coupon.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.CouponMapper;
import com.qimeixun.mapper.CouponUserMapper;
import com.qimeixun.modules.coupon.service.CouponUserService;
import com.qimeixun.po.CartOrderDTO;
import com.qimeixun.po.CouponDTO;
import com.qimeixun.po.UserCouponDTO;
import com.qimeixun.ro.UserCouponListRO;
import com.qimeixun.ro.UserCouponOrderRO;
import com.qimeixun.util.DateUtil;
import com.qimeixun.util.TokenUtil;
import com.qimeixun.vo.CouponListVO;
import com.qimeixun.vo.ShopCartListVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author chenshouyang
 * @date 2020/5/3015:40
 */
@Service
public class CouponUserServiceImpl implements CouponUserService {

    @Resource
    CouponMapper couponMapper;

    @Resource
    CouponUserMapper couponUserMapper;

    @Resource
    TokenUtil tokenUtil;

    /**
     * 用户领取优惠券
     *
     * @param couponId
     * @return
     */
    @Override
    @Transactional
    public int insertUserCoupon(String couponId) {
        CouponDTO couponDTO = couponMapper.selectById(couponId);
        if (couponDTO == null) {
            throw new ServiceException("优惠券不存在");
        }
        if ("1".equals(couponDTO.getStatus())) {
            throw new ServiceException("优惠券已下线");
        }
        if (couponDTO.getTotalCount() != 0) {
            if (couponDTO.getTotalCount() <= couponDTO.getSurplusCount()) {
                throw new ServiceException("优惠券已领取完");
            }
        }

        UserCouponDTO userCouponDTO = new UserCouponDTO();
        BeanUtils.copyProperties(couponDTO, userCouponDTO);
        userCouponDTO.setUserId(Long.valueOf(tokenUtil.getUserIdByToken()));
        if ("0".equals(couponDTO.getEffectType())) {
            //有效期天
            if (couponDTO.getEffectDays() == 0) {
                //天数为0表示长期有效  给他加个100年
                userCouponDTO.setEffectTime(DateUtil.addDateYear(new Date(), 100));
            } else {
                userCouponDTO.setEffectTime(DateUtil.addDateDay(new Date(), couponDTO.getEffectDays()));
            }

        }
        userCouponDTO.setCouponId(couponDTO.getId());
        int i = couponUserMapper.insert(userCouponDTO);

        couponDTO.setSurplusCount(couponDTO.getSurplusCount() + 1);
        couponMapper.updateById(couponDTO);
        return i;
    }

    @Override
    public List<CouponListVO> selectUserNotHaveCouponList(String storeId) {
        Map<String, Object> argMap = new HashMap<>();
        argMap.put("storeId", storeId);
        String userId = tokenUtil.getUnCheckUserIdByToken();
        if(StrUtil.isNotBlank(userId)){
            argMap.put("userId", userId);
        }
        return couponMapper.selectUserNotHaveCouponList(argMap);
    }

    @Override
    public List<UserCouponDTO> selectUserCouponByProduct(UserCouponOrderRO userCouponOrderRO) {
        List<CartOrderDTO> cartOrderDTOS = userCouponOrderRO.getCartOrderDTOS();
        List<UserCouponDTO> resultList = new ArrayList<>();
        QueryWrapper<UserCouponDTO> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", tokenUtil.getUserIdByToken());
        queryWrapper.eq("is_used", "0");
        queryWrapper.gt("effect_time", new Date());
        if ("1".equals(userCouponOrderRO.getType())) {
            //获取店铺可用优惠
            queryWrapper.eq("store_id", cartOrderDTOS.get(0).getStoreId());
            queryWrapper.eq("is_platform", "0");
        } else {
            //获取平台优惠券
            queryWrapper.eq("is_platform", "1");
        }
        List<UserCouponDTO> userCouponDTOS = couponUserMapper.selectList(queryWrapper);
        List<ShopCartListVO> shopCartListVOS = cartOrderDTOS.get(0).getShopCartListVOS();
        for (UserCouponDTO userCouponDTO : userCouponDTOS) {
            //可以使用的产品id集合
            List<ShopCartListVO> lists = new ArrayList<>();
            List<Integer> integers = JSON.parseArray(userCouponDTO.getEffectProductId(), Integer.class);
            if (CollectionUtils.isEmpty(integers)) {
                //表示该店全品类券
                lists = shopCartListVOS;
            }
            if ("0".equals(userCouponDTO.getType())) {
                //产品分类优惠券
                for (ShopCartListVO shopCartListVO : shopCartListVOS) {
                    //符合优惠的产品id
                    if (integers.contains(shopCartListVO.getCategoryId())) {
                        lists.add(shopCartListVO);
                    }
                }
            } else {
                //指定产品优惠券
                for (ShopCartListVO shopCartListVO : shopCartListVOS) {
                    //符合优惠的产品id
                    if (integers.contains(shopCartListVO.getProductId())) {
                        lists.add(shopCartListVO);
                    }
                }
            }

            //计算所有符合优惠的产品集合 计算价格是否满足
            BigDecimal totalMoney = BigDecimal.ZERO;
            for (ShopCartListVO shopCartListVO : lists) {
                totalMoney = totalMoney.add(NumberUtil.mul(shopCartListVO.getPrice(), shopCartListVO.getNum()));
            }
            if (!CollectionUtils.isEmpty(lists)) {
                if (totalMoney.compareTo(userCouponDTO.getMinConsume()) > 0) {
                    //商品总价格大于等于优惠券的最低限额
                    resultList.add(userCouponDTO);
                    if (totalMoney.compareTo(userCouponDTO.getCouponMoney()) >= 0) {
                        userCouponDTO.setOrderCouponMoney(userCouponDTO.getCouponMoney());
                    } else {
                        userCouponDTO.setOrderCouponMoney(totalMoney);
                    }
                }
            }
        }
        return resultList;
    }

    @Override
    public List<UserCouponDTO> selectUserCouponList(UserCouponListRO userCouponListRO) {

        LambdaQueryWrapper<UserCouponDTO> queryWrapper = new QueryWrapper<UserCouponDTO>().lambda();
        queryWrapper.eq(UserCouponDTO::getUserId, tokenUtil.getUserIdByToken());
        if ("0".equals(userCouponListRO.getType())) { //未使用
            queryWrapper.eq(UserCouponDTO::getIsUsed, 0);
        }
        if("1".equals(userCouponListRO.getType())){
            queryWrapper.eq(UserCouponDTO::getIsUsed, 1);
        }
        queryWrapper.orderByAsc(UserCouponDTO::getIsUsed).orderByDesc(UserCouponDTO::getCreateTime);

        List<UserCouponDTO> list = couponUserMapper.selectList(queryWrapper);

        List<UserCouponDTO> resultList = new ArrayList<>();

        for (UserCouponDTO userCouponDTO : list) {
            if (userCouponDTO.getEffectTime() != null && userCouponDTO.getEffectTime().getTime() > new Date().getTime()) {
                //有效期内
                userCouponDTO.setIsEffect("1");
                if(!"2".equals(userCouponListRO.getType())){
                    resultList.add(userCouponDTO);
                }
            } else {
                //已过期
                userCouponDTO.setIsEffect("0");
                if("2".equals(userCouponListRO.getType())){
                    resultList.add(userCouponDTO);
                }
            }
        }
        return resultList;
    }

}
