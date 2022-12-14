package com.qimeixun.modules.order.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.constant.SystemConstant;
import com.qimeixun.entity.Customer;
import com.qimeixun.entity.Store;
import com.qimeixun.enums.OrderTypeEnum;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.*;
import com.qimeixun.modules.order.service.OrderService;
import com.qimeixun.po.*;
import com.qimeixun.ro.*;
import com.qimeixun.service.OrderCommonService;
import com.qimeixun.service.SysConfigService;
import com.qimeixun.util.RedisUtil;
import com.qimeixun.util.TokenUtil;
import com.qimeixun.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenshouyang
 * @date 2020/5/2520:28
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    OrderMapper orderMapper;

    @Resource
    TokenUtil tokenUtil;

    @Resource
    AddressMapper addressMapper;

    @Resource
    ShopCartMapper shopCartMapper;

    @Resource
    CouponUserMapper couponUserMapper;

    @Resource
    CustomerMapper customerMapper;

    @Resource
    ProductMapper productMapper;

    @Resource
    OrderCartMapper orderCartMapper;

    @Resource
    ProductAttrDetailMapper productAttrDetailMapper;

    @Resource
    SeckillProductMapper seckillProductMapper;

    @Resource
    StoreMapper storeMapper;

    @Resource
    RedisUtil redisUtil;

    @Resource
    SysConfigService sysConfigService;

    @Resource
    OrderCommonService orderCommonService;

    @Resource
    OrderRefundMapper orderRefundMapper;

    @Resource
    OrderRefundProductMapper orderRefundProductMapper;

    @Override
    public Map<String, Object> selectOrderInfoCreateBefore(UserCouponOrderRO userCouponOrderRO) {

        //????????????
        BigDecimal storeCoupnMoney = new BigDecimal(0);
        //        //????????????
        BigDecimal totalGoodsMoney = new BigDecimal(0);
        //??????????????????
        BigDecimal totalPostMoney = new BigDecimal(0);
        //??????????????????
        BigDecimal platformCouponMoney = new BigDecimal(0);
        //??????????????????
        BigDecimal pointsMoney = new BigDecimal(0);
        //???????????????
        BigDecimal couponAfterMoney = new BigDecimal(0);

        List<ShopCartListVO> allShopCartListVOS = new ArrayList<>();
        //???????????????????????????id
        CartOrderDTO cartOrderDTO = getCartOrderDTOS(userCouponOrderRO.getIds());

        BigDecimal[] maxPostMoney = {new BigDecimal(0)};
        allShopCartListVOS.addAll(cartOrderDTO.getShopCartListVOS());
        for (ShopCartListVO shopCartListVO : cartOrderDTO.getShopCartListVOS()) {
            totalGoodsMoney = totalGoodsMoney.add(NumberUtil.mul(shopCartListVO.getNum(), shopCartListVO.getPrice()));
            //??????????????????????????????????????????????????????
            if (shopCartListVO.getPostPrice().compareTo(maxPostMoney[0]) == 1) {
                maxPostMoney[0] = shopCartListVO.getPostPrice();
            }
        }
        totalPostMoney = totalPostMoney.add(maxPostMoney[0]);
        for (CartOrderDTO c : userCouponOrderRO.getCartOrderDTOS()) {
            if (cartOrderDTO.getStoreId().equals(c.getStoreId())) {
                if (StringUtils.isNotEmpty(c.getRemark())) cartOrderDTO.setRemark(c.getRemark());
                if (c.getCouponId() != null) cartOrderDTO.setCouponId(c.getCouponId());
                if (StringUtils.isNotEmpty(c.getCouponName())) cartOrderDTO.setCouponName(c.getCouponName());
                //???????????????????????????????????????
                storeCoupnMoney = storeCoupnMoney.add(getMaxCouponMoney(c.getCouponId(), cartOrderDTO.getShopCartListVOS()));
            }
        }

        couponAfterMoney = totalGoodsMoney.subtract(storeCoupnMoney);
        //????????????????????????
        platformCouponMoney = platformCouponMoney.add(getMaxCouponMoney(userCouponOrderRO.getCouponId(), allShopCartListVOS));
        if (couponAfterMoney.compareTo(platformCouponMoney) == -1) {
            platformCouponMoney = couponAfterMoney;
        }
        couponAfterMoney = couponAfterMoney.subtract(platformCouponMoney); //??????????????????????????????
        Customer customer = customerMapper.selectById(tokenUtil.getUserIdByToken());
        if (userCouponOrderRO.isPoints()) {
            //??????????????????
            //100????????????1???
            pointsMoney = NumberUtil.div(((customer.getPoints())), Double.valueOf(100));
            if (couponAfterMoney.compareTo(pointsMoney) == -1) {
                pointsMoney = couponAfterMoney;
            }
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalGoodsMoney", totalGoodsMoney);
        resultMap.put("totalPostMoney", totalPostMoney);
        resultMap.put("storeCouponMoney", storeCoupnMoney);
        resultMap.put("platformCouponMoney", platformCouponMoney);
        resultMap.put("pointsMoney", pointsMoney);
        resultMap.put("cartOrderDTO", cartOrderDTO);
        resultMap.put("userPoints", customer.getPoints());
        //????????????????????????????????? ???????????? ?????????????????? ??????  ?????????????????? ?????????????????? ?????????
        return resultMap;

    }

    /**
     * ??????????????????????????????????????????
     *
     * @param couponId
     * @param shopCartListVOS
     * @return
     */
    private BigDecimal getMaxCouponMoney(Long couponId, List<ShopCartListVO> shopCartListVOS) {
        if (couponId == null) {
            return new BigDecimal(0);
        }
        QueryWrapper<UserCouponDTO> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", tokenUtil.getUserIdByToken()).eq("id", couponId);
        UserCouponDTO userCouponDTO = couponUserMapper.selectOne(queryWrapper);
        if (userCouponDTO == null) {
            throw new ServiceException("??????????????????????????????");
        }
        //?????????????????????id??????
        List<ShopCartListVO> lists = new ArrayList<>();
        List<Integer> integers = JSON.parseArray(userCouponDTO.getEffectProductId(), Integer.class);
        if ("2".equals(userCouponDTO.getType()) || CollectionUtils.isEmpty(integers)) {
            //????????????????????????
            lists = shopCartListVOS;
        }else{
            if ("0".equals(userCouponDTO.getType())) {
                //?????????????????????
                for (ShopCartListVO shopCartListVO : shopCartListVOS) {
                    //?????????????????????id
                    if (integers.contains(shopCartListVO.getCategoryId())) {
                        lists.add(shopCartListVO);
                    }
                }
            } else {
                //?????????????????????
                for (ShopCartListVO shopCartListVO : shopCartListVOS) {
                    //?????????????????????id
                    if (integers.contains(shopCartListVO.getProductId())) {
                        lists.add(shopCartListVO);
                    }
                }
            }
        }

        //??????????????????????????????????????? ????????????????????????
        BigDecimal totalMoney = new BigDecimal(0);
        for (ShopCartListVO shopCartListVO : lists) {
            totalMoney = totalMoney.add(NumberUtil.mul(shopCartListVO.getPrice(), shopCartListVO.getNum()));
        }

        if (!CollectionUtils.isEmpty(lists)) {
            if (totalMoney.compareTo(userCouponDTO.getMinConsume()) >= 0) {
                //???????????????????????????????????????????????????
                if (totalMoney.compareTo(userCouponDTO.getCouponMoney()) >= 0) {
                    return userCouponDTO.getCouponMoney();
                } else {
                    return totalMoney;
                }
            }
        }
        return new BigDecimal(0);
    }

    private CartOrderDTO getCartOrderDTOS(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("????????????");
        }
        return orderMapper.selectOrderInfoCreateBefore(ids);
    }

    @Override
    @Transactional
    public OrderDTO createOrder(UserCouponOrderRO userCouponOrderRO) {
        //??????ids?????????????????????????????????
        CartOrderDTO cartOrderDTO = getCartOrderDTOS(userCouponOrderRO.getIds());


        //List<OrderDTO> orderDTOS = new ArrayList<>();
        List<ShopCartListVO> allShopCartListVOS = new ArrayList<>();


        allShopCartListVOS.addAll(cartOrderDTO.getShopCartListVOS());
        //???????????? ????????????id
        String orderId = IdUtil.getSnowflake(1, 1).nextIdStr();
        BigDecimal totalMoney = new BigDecimal(0);
        int totalNum = 0;
        //??????????????????id????????????
        List<Map<String, Object>> orderCartsRelation = new ArrayList<>();
        //???????????????id
        List<Integer> cartIds = new ArrayList<>();
        //???????????????????????????
        for (ShopCartListVO shopCartListVOS : cartOrderDTO.getShopCartListVOS()) {
            totalMoney = totalMoney.add(NumberUtil.mul(shopCartListVOS.getNum(), shopCartListVOS.getPrice()));
            totalNum = totalNum + shopCartListVOS.getNum();

            Map<String, Object> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("cartId", shopCartListVOS.getId());
            cartIds.add(shopCartListVOS.getId());

            orderCartsRelation.add(map);

            //??????????????????
            ProductDTO productDTO = productMapper.selectById(shopCartListVOS.getProductId());
            if (productDTO != null) {
                shopCartListVOS.setProductInfo(JSON.toJSONString(productDTO));
                if (StringUtils.isEmpty(shopCartListVOS.getAttrValue())) {
                    shopCartListVOS.setPrice(productDTO.getPrice());
                }
            }
        }
        //???????????? ????????????
        OrderDTO orderDTO = new OrderDTO();
        BigDecimal storeCouponMoney = new BigDecimal(0);
        if (!CollectionUtils.isEmpty(userCouponOrderRO.getCartOrderDTOS())) {
            for (CartOrderDTO ca : userCouponOrderRO.getCartOrderDTOS()) {
                if (ca.getCouponId() != null && ca.getStoreId().equals(cartOrderDTO.getStoreId())) {
                    orderDTO.setCouponId(ca.getCouponId());
                    storeCouponMoney = getMaxCouponMoney(ca.getCouponId(), cartOrderDTO.getShopCartListVOS());
                }
            }
        }

        BigDecimal platformCouponMoney = new BigDecimal(0);
        if (userCouponOrderRO.getCouponId() != null) {
            //????????????????????????
            platformCouponMoney = getMaxCouponMoney(userCouponOrderRO.getCouponId(), cartOrderDTO.getShopCartListVOS());
            if (platformCouponMoney.compareTo(BigDecimal.ZERO) == 1) { //platformCouponMoney ??????0
                orderDTO.setCouponId(userCouponOrderRO.getCouponId());
                orderDTO.setPlatformCouponMoney(platformCouponMoney);
            }
        }


        orderDTO.setOrderId(orderId);
        orderDTO.setIsPay("0");
        orderDTO.setPayType("1");
        orderDTO.setStatus("0");
        orderDTO.setUserId(Long.valueOf(tokenUtil.getUserIdByToken()));
        orderDTO.setTotalMoney(totalMoney);
        orderDTO.setTotalNum(totalNum);
        orderDTO.setPayMoney(totalMoney.subtract(platformCouponMoney));
        orderDTO.setStoreId(cartOrderDTO.getStoreId());
        //????????????????????????
        orderDTO.setStoreCouponMoney(storeCouponMoney);
        //??????????????????

        setOrderAddress(orderDTO, userCouponOrderRO.getAddressId(), userCouponOrderRO.getBuyType(), userCouponOrderRO.getStoreId());
        orderDTO.setRemark(userCouponOrderRO.getRemark());

        orderMapper.insert(orderDTO);

        //?????????????????????????????????
        checkOrderCart(cartIds, tokenUtil.getUserIdByToken());
        orderMapper.insertOrderCartRelation(orderCartsRelation);

        //????????????
        subStock(cartOrderDTO.getShopCartListVOS());

        //??????????????????????????????????????????
        shopCartMapper.updateCartPayStatus(cartOrderDTO.getShopCartListVOS());

        orderCommonService.setCouponIsUsed(userCouponOrderRO.getCouponId()); //???????????????????????????

        //redis ????????????
        redisUtil.set(SystemConstant.REDIS_CANCEL_KEY + orderId, SystemConstant.REDIS_CANCEL_KEY + orderId, Integer.parseInt(sysConfigService.getSysConfigValueFromRedis(SystemConstant.CANCEL_ORDER_TIME)));

        return orderDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDTO createSingleOrder(UserSingleOrderRO userCouponOrderRO) {
        if (StrUtil.isNotBlank(userCouponOrderRO.getSeckillProductId())) {
            return createKillOrder(userCouponOrderRO);
        } else {
            return createNormalOrder(userCouponOrderRO);
        }

    }

    public OrderDTO createNormalOrder(UserSingleOrderRO userCouponOrderRO) {

        ProductDTO productDTO = productMapper.selectById(userCouponOrderRO.getProductId());
        if (productDTO == null) {
            throw new ServiceException("???????????????");
        }

        if(CollectionUtils.isEmpty(userCouponOrderRO.getCurNav()) || userCouponOrderRO.getCurNav().size() != 1){
            throw new ServiceException("??????????????????");
        }

        if ("1".equals(userCouponOrderRO.getProductType())) {
            //??????????????????
            if (!"1".equals(productDTO.getIsPoints())) {
                throw new ServiceException("?????????????????????");
            }
        } else {
            userCouponOrderRO.setProductType("0");
        }

        ProductAttrDetailDTO productAttrDetailDTO = getProductAttr(userCouponOrderRO.getCurNav(), userCouponOrderRO.getProductId());
        if (productAttrDetailDTO == null) {
            throw new ServiceException("??????????????????");
        }

        List<ShopCartListVO> allShopCartListVOS = new ArrayList<>();
        ShopCartListVO cartListVO = new ShopCartListVO();
        cartListVO.setProductId(productDTO.getId());
        cartListVO.setCategoryId(productDTO.getCategoryId());
        cartListVO.setNum(userCouponOrderRO.getNum());
        cartListVO.setPrice(productAttrDetailDTO.getPrice());
        allShopCartListVOS.add(cartListVO);
        BigDecimal couponMoney = getMaxCouponMoney(userCouponOrderRO.getCouponId(), allShopCartListVOS);


        //BigDecimal price = productAttrDetailDTO.getPrice();

        //???????????? ????????????
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(IdUtil.getSnowflake(1, 1).nextIdStr());
        orderDTO.setIsPay("0");
        orderDTO.setPayType("1");
        orderDTO.setStatus("0");
        orderDTO.setUserId(Long.valueOf(tokenUtil.getUserIdByToken()));
        orderDTO.setTotalMoney(NumberUtil.mul(userCouponOrderRO.getNum(), productAttrDetailDTO.getPrice()));
        orderDTO.setTotalNum(userCouponOrderRO.getNum());
        orderDTO.setPayMoney(NumberUtil.mul(userCouponOrderRO.getNum(), productAttrDetailDTO.getPrice()).subtract(couponMoney));
        orderDTO.setStoreId(productDTO.getStoreId());
        //????????????????????????
        orderDTO.setPlatformCouponMoney(couponMoney);
        orderDTO.setCouponId(userCouponOrderRO.getCouponId());

        if ("1".equals(userCouponOrderRO.getProductType())) {
            orderDTO.setOrderType(OrderTypeEnum.ORDER_TYPE_POINTS.getType());
        }
        //??????????????????
        setOrderAddress(orderDTO, userCouponOrderRO.getAddressId(), userCouponOrderRO.getBuyType(), userCouponOrderRO.getStoreId());

        orderDTO.setRemark(userCouponOrderRO.getRemark());
        orderDTO.setIsPoints(userCouponOrderRO.getProductType());

        orderMapper.insert(orderDTO);

        //?????????????????????????????????
        ShopCartDTO shopCartDTO = new ShopCartDTO();
        shopCartDTO.setUserId(Integer.valueOf(tokenUtil.getUserIdByToken()));
        shopCartDTO.setNum(userCouponOrderRO.getNum());
        shopCartDTO.setProductAttrId(productAttrDetailDTO.getId());
        shopCartDTO.setIsPay(1);
        shopCartDTO.setProductId(Long.valueOf(userCouponOrderRO.getProductId()));
        shopCartDTO.setProductInfo(JSON.toJSONString(productDTO));
        shopCartDTO.setAttrValue(ArrayUtil.join(userCouponOrderRO.getCurNav().toArray(), ",") + ",");
        shopCartDTO.setPrice(productAttrDetailDTO.getPrice());

        shopCartMapper.insert(shopCartDTO);

        //????????????
        subStock(allShopCartListVOS);

        //??????????????????id????????????
        List<Map<String, Object>> orderCartsRelation = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderDTO.getOrderId());
        map.put("cartId", shopCartDTO.getId());

        orderCartsRelation.add(map);

        orderMapper.insertOrderCartRelation(orderCartsRelation);

        orderCommonService.setCouponIsUsed(userCouponOrderRO.getCouponId()); //???????????????????????????

        //redis ????????????
        redisUtil.set(SystemConstant.REDIS_CANCEL_KEY + orderDTO.getOrderId(), SystemConstant.REDIS_CANCEL_KEY
                + orderDTO.getOrderId(), Integer.parseInt(sysConfigService.getSysConfigValueFromRedis(SystemConstant.CANCEL_ORDER_TIME)));

        return orderDTO;
    }

    /**
     * ????????????
     *
     * @param userCouponOrderRO
     * @return
     */
    private OrderDTO createKillOrder(UserSingleOrderRO userCouponOrderRO) {

        SeckillProductDTO seckillProductDTO = seckillProductMapper.selectById(userCouponOrderRO.getSeckillProductId());
        if (seckillProductDTO == null) {
            throw new ServiceException("???????????????");
        }

        int cacheProductCount = 0;
        if (redisUtil.hasKey(SystemConstant.SECKILL_PRODUCT + userCouponOrderRO.getSeckillProductId())) {
            Object o = redisUtil.get(SystemConstant.SECKILL_PRODUCT + userCouponOrderRO.getSeckillProductId());
            cacheProductCount = Integer.valueOf(String.valueOf(o));
            if (cacheProductCount < userCouponOrderRO.getNum()) {
                throw new ServiceException("????????????");
            }
        } else {
            throw new ServiceException("????????????????????????");
        }

        BigDecimal price = seckillProductDTO.getSeckillPrice(); //????????????????????????

        ProductDTO productDTO = productMapper.selectById(userCouponOrderRO.getProductId());

        ProductAttrDetailDTO productAttr = getProductAttr(userCouponOrderRO.getCurNav(), userCouponOrderRO.getProductId());


        List<ShopCartListVO> allShopCartListVOS = new ArrayList<>();
        ShopCartListVO cartListVO = new ShopCartListVO();
        cartListVO.setProductId(productDTO.getId());
        cartListVO.setCategoryId(productDTO.getCategoryId());
        cartListVO.setNum(userCouponOrderRO.getNum());
        allShopCartListVOS.add(cartListVO);

        //???????????? ????????????
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(IdUtil.getSnowflake(1, 1).nextIdStr());
        orderDTO.setIsPay("0");
        orderDTO.setPayType("1");
        orderDTO.setStatus("0");
        orderDTO.setUserId(Long.valueOf(tokenUtil.getUserIdByToken()));
        orderDTO.setTotalMoney(NumberUtil.mul(userCouponOrderRO.getNum(), price));
        orderDTO.setTotalNum(userCouponOrderRO.getNum());
        orderDTO.setPayMoney(NumberUtil.mul(userCouponOrderRO.getNum(), price));
        orderDTO.setStoreId(productDTO.getStoreId());
        orderDTO.setOrderType(OrderTypeEnum.ORDER_TYPE_SECKILL.getType());
        //????????????????????????
        orderDTO.setStoreCouponMoney(new BigDecimal(0)); //???????????????0
        //??????????????????
        setOrderAddress(orderDTO, userCouponOrderRO.getAddressId(), userCouponOrderRO.getBuyType(), userCouponOrderRO.getStoreId());

        orderDTO.setRemark(userCouponOrderRO.getRemark());

        orderMapper.insert(orderDTO);

        //?????????????????????????????????
        ShopCartDTO shopCartDTO = new ShopCartDTO();
        shopCartDTO.setUserId(Integer.valueOf(tokenUtil.getUserIdByToken()));
        shopCartDTO.setNum(userCouponOrderRO.getNum());
        shopCartDTO.setProductAttrId(productAttr.getId());
        shopCartDTO.setIsPay(1);
        shopCartDTO.setProductId(Long.valueOf(userCouponOrderRO.getProductId()));
        shopCartDTO.setProductInfo(JSON.toJSONString(productDTO));
        shopCartDTO.setAttrValue(ArrayUtil.join(userCouponOrderRO.getCurNav().toArray(), ",") + ",");
        shopCartDTO.setPrice(price);

        //???????????????
        shopCartDTO.setSeckillProductId(Long.valueOf(userCouponOrderRO.getSeckillProductId()));


        shopCartMapper.insert(shopCartDTO);

        //??????????????????
        //subStock(allShopCartListVOS);

        seckillProductDTO.setStock(seckillProductDTO.getStock() - userCouponOrderRO.getNum());
        seckillProductMapper.updateById(seckillProductDTO);

        //??????????????????
        long time = (seckillProductDTO.getEndTime().getTime() - new Date().getTime()) / 1000 / 60;
        redisUtil.set(SystemConstant.SECKILL_PRODUCT + seckillProductDTO.getId(), cacheProductCount - userCouponOrderRO.getNum(), time);

        //??????????????????id????????????
        List<Map<String, Object>> orderCartsRelation = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderDTO.getOrderId());
        map.put("cartId", shopCartDTO.getId());

        orderCartsRelation.add(map);

        orderMapper.insertOrderCartRelation(orderCartsRelation);

        //redis ????????????
        redisUtil.set(SystemConstant.REDIS_CANCEL_KEY + orderDTO.getOrderId(), SystemConstant.REDIS_CANCEL_KEY
                + orderDTO.getOrderId(), Integer.parseInt(sysConfigService.getSysConfigValueFromRedis(SystemConstant.CANCEL_ORDER_TIME)));

        return orderDTO;

    }

    private void setOrderAddress(OrderDTO orderDTO, String addressId, String buyType, String storeId) {

        if ("1".equals(buyType)) {

            //??????????????????
            if (StrUtil.isBlank((addressId))) {
                throw new ServiceException("?????????????????????");
            }
            AddressDTO addressDTO = addressMapper.selectById(addressId);
            if (addressDTO == null) {
                throw new ServiceException("?????????????????????");
            }
            orderDTO.setBuyType(buyType);
            orderDTO.setName(addressDTO.getName());
            orderDTO.setPhone(addressDTO.getPhone());
            orderDTO.setAddress(addressDTO.getAddress());

        } else if ("2".equals(buyType)) {

            //????????????
            if (StrUtil.isBlank(storeId)) {
                throw new ServiceException("???????????????????????????");
            }
            Store store = storeMapper.selectById(storeId);
            if (store == null) {
                throw new ServiceException("???????????????");
            }

            orderDTO.setBuyType(buyType);
            orderDTO.setName(store.getStoreName());
            orderDTO.setPhone(store.getStoreTell());
            orderDTO.setAddress(store.getStoreAddress());
            orderDTO.setStoreId(storeId);
        } else {
            throw new ServiceException("????????????");
        }

    }

    @Override
    public String selectOrderAttrValue(String orderId) {
        return orderMapper.selectOrderAttrValue(orderId);
    }

    @Override
    public OrderExpressVO selectOrderExpress(String orderId) {
        return orderMapper.selectOrderExpress(orderId);
    }

    /**
     * ??????????????????
     *
     * @param refundGoodsRO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundOrderGoods(RefundGoodsRO refundGoodsRO) {
        if (CollectionUtils.isEmpty(refundGoodsRO.getChildrenROList())) {
            throw new ServiceException("??????????????????????????????");
        }

        OrderDTO orderDTO = orderMapper.selectOne(new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getOrderId, refundGoodsRO.getOrderId()).eq(OrderDTO::getUserId, tokenUtil.getUserIdByToken()));
        if (orderDTO == null) {
            throw new ServiceException("???????????????");
        }
        if ("1".equals(orderDTO.getRefundStatus()) || "3".equals(orderDTO.getRefundStatus())) {
            throw new ServiceException("???????????????????????????");
        }
        UserOrderVO userOrderVO = this.selectUserOrderDetail(refundGoodsRO.getOrderId());

        if(userOrderVO.getPayMoney().compareTo(BigDecimal.ZERO) == 0){
            throw new ServiceException("????????????????????????");
        }

        BigDecimal refundMoney = BigDecimal.ZERO;

        //??????????????????????????????????????????????????????
        for (RefundGoodsChildrenRO refundGoodsChildrenRO : refundGoodsRO.getChildrenROList()) {
            for (UserProductVO userProductVO : userOrderVO.getUserProductVOS()) {
                if (refundGoodsChildrenRO.getProductId().equals(userProductVO.getProductDTO().getId()) &&
                        refundGoodsChildrenRO.getAttrValue().equals(userProductVO.getAttrValue())
                ) {
                    refundMoney = refundMoney.add(userProductVO.getPrice().multiply(new BigDecimal(refundGoodsChildrenRO.getRefundNum())));
                    userProductVO.setRefundNum(refundGoodsChildrenRO.getRefundNum());
                    if (refundGoodsChildrenRO.getRefundNum() > userProductVO.getNum()) {
                        throw new ServiceException("?????????????????????????????????");
                    }
                }
            }
        }

        if(refundMoney.compareTo(userOrderVO.getPayMoney()) == 1){
            refundMoney = userOrderVO.getPayMoney();
        }

        //????????????????????????
        if (refundMoney.compareTo(BigDecimal.ZERO) == 0) {
            throw new ServiceException("???????????????0");
        }

        OrderRefundDTO orderRefundDTO = new OrderRefundDTO();
        orderRefundDTO.setOrderId(orderDTO.getOrderId());
        orderRefundDTO.setPayMoney(orderDTO.getPayMoney());
        orderRefundDTO.setRefundMoney(refundMoney);
        orderRefundDTO.setType(refundGoodsRO.getType());
        orderRefundDTO.setRemark(refundGoodsRO.getRemark());
        orderRefundMapper.insert(orderRefundDTO);

        //???????????????????????????
        for (UserProductVO userProductVO : userOrderVO.getUserProductVOS()) {
            if (userProductVO.getRefundNum() > 0) {
                OrderRefundProductDTO refundProductDTO = new OrderRefundProductDTO();
                refundProductDTO.setRefundOrderId(orderRefundDTO.getId());
                refundProductDTO.setPayNum(userProductVO.getNum());
                refundProductDTO.setRefundNum(userProductVO.getRefundNum());
                refundProductDTO.setPrice(userProductVO.getPrice());
                refundProductDTO.setAttrValue(userProductVO.getAttrValue());
                refundProductDTO.setProductId(userProductVO.getProductDTO().getId());
                refundProductDTO.setProductJson(userProductVO.getProductInfoJson());
                orderRefundProductMapper.insert(refundProductDTO);
            }
        }

        //????????????????????????
        OrderDTO dto = new OrderDTO();
        dto.setId(orderDTO.getId());
        dto.setRefundStatus("1"); // ?????? ?????????
        orderMapper.updateById(dto);
    }

    @Override
    public Map selectRefundOrderGoodsDetail(String refundId) {
        OrderRefundDTO orderRefundDTO = orderRefundMapper.selectById(refundId);
        OrderDTO orderDTO = orderMapper.selectOne(new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getOrderId, orderRefundDTO.getOrderId()));
        if (orderDTO == null) {
            throw new ServiceException("???????????????");
        }
        if (!tokenUtil.getUserIdByToken().equals(String.valueOf(orderDTO.getUserId()))) {
            throw new ServiceException("????????????");
        }
        List<OrderRefundProductDTO> refundProductDTOS = orderRefundProductMapper.selectList(
                new QueryWrapper<OrderRefundProductDTO>().lambda().in(OrderRefundProductDTO::getRefundOrderId, refundId));

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("refundOrder", orderRefundDTO);
        resultMap.put("productList", refundProductDTOS);
        return resultMap;
    }

    /**
     * ????????????
     *
     * @param refundId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelRefundOrder(String refundId) {
        OrderRefundDTO orderRefundDTO = orderRefundMapper.selectById(refundId);
        if (orderRefundDTO == null) {
            throw new ServiceException("??????????????????");
        }

        LambdaQueryWrapper<OrderRefundDTO> queryWrapper = new QueryWrapper<OrderRefundDTO>().lambda();
        queryWrapper.eq(OrderRefundDTO::getId, refundId);
        queryWrapper.eq(OrderRefundDTO::getStatus, "1");

        OrderRefundDTO refundDTO = new OrderRefundDTO();
        refundDTO.setStatus("4");
        orderRefundMapper.update(refundDTO, queryWrapper);

        //??????????????????
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setRefundStatus("0");
        int i = orderMapper.update(orderDTO, new QueryWrapper<OrderDTO>().lambda().
                eq(OrderDTO::getOrderId, orderRefundDTO.getOrderId()).eq(OrderDTO::getUserId, tokenUtil.getUserIdByToken()));
        if (i == 0) {
            throw new ServiceException("????????????????????????????????????");
        }
    }

    /**
     * ????????????
     *
     * @param shopCartListVOS
     */
    private void subStock(List<ShopCartListVO> shopCartListVOS) {
        if (CollectionUtils.isEmpty(shopCartListVOS)) throw new ServiceException("?????????????????????");

        List<OrderProductListVO> orderProductListVOS = new ArrayList<>();
        for (ShopCartListVO shopCartListVO : shopCartListVOS) {
            OrderProductListVO orderProductListVO = new OrderProductListVO();
            orderProductListVO.setNum(shopCartListVO.getNum());
            orderProductListVO.setProductId(shopCartListVO.getProductId());
            orderProductListVOS.add(orderProductListVO);
        }
        orderCommonService.addOrSubStock(orderProductListVOS, SystemConstant.STOCK_TYPE_SUB);
    }


    /**
     * ??????????????????id
     *
     * @return
     */
    private ProductAttrDetailDTO getProductAttr(List<String> curNav, Integer productId) {
        ProductAttrDetailDTO dto = null;
        if (curNav != null && curNav.size() > 0) {
            String productAttrValue = ArrayUtil.join(curNav.toArray(), ",") + ",";
            QueryWrapper<ProductAttrDetailDTO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("product_id", productId);
            queryWrapper.eq("attr_value", productAttrValue);
            dto = productAttrDetailMapper.selectOne(queryWrapper);
        }
        if(dto == null){
            throw new ServiceException("?????????????????????");
        }
        return dto;
    }

    private void checkOrderCart(List<Integer> cartIds, String userId) {
        QueryWrapper<ShopCartDTO> wrapper = new QueryWrapper<>();
        wrapper.in("id", cartIds);
        wrapper.eq("is_pay", "0");
        List<ShopCartDTO> shopCartDTOS = shopCartMapper.selectList(wrapper);
        for (ShopCartDTO shopCartDTO : shopCartDTOS) {
            if (!userId.equals(String.valueOf(shopCartDTO.getUserId()))) {
                throw new ServiceException("????????????");
            }
        }

        QueryWrapper<OrderCartDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("cart_id", cartIds);
        int count = orderCartMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ServiceException("??????????????????????????????");
        }

    }

    /**
     * ?????????????????????
     *
     * @return
     */
    @Override
    public IPage selectUserOrderList(UserOrderRO userOrderRO) {

        userOrderRO.setUserId(tokenUtil.getUserIdByToken());
        IPage<UserOrderVO> page = new Page<>(userOrderRO.getCurrentPage(), userOrderRO.getPageSize());
        IPage<UserOrderVO> iPage = orderMapper.selectUserOrderList(page, userOrderRO);
        return iPage;
    }

    @Override
    public UserOrderVO selectUserOrderDetail(String orderId) {

        Map<String, Object> argMap = new HashMap<>();
        argMap.put("orderId", orderId);
        argMap.put("userId", tokenUtil.getUserIdByToken());
        UserOrderVO userOrderVO = orderMapper.selectUserOrderById(argMap);
        if (userOrderVO == null) {
            throw new ServiceException("???????????????");
        }
        return userOrderVO;
    }

    /**
     * ???????????????????????????
     *
     * @param productId
     * @param orderId
     */
    @Override
    public void updateOrderReplyStatus(String productId, String orderId) {
        List<OrderCartDTO> cartDTOS = orderCartMapper.selectList(new QueryWrapper<OrderCartDTO>().eq("order_id", orderId));
        List<Long> cartIds = cartDTOS.stream().map(item -> item.getCartId()).collect(Collectors.toList());
        List<ShopCartDTO> dtos = shopCartMapper.selectList(new QueryWrapper<ShopCartDTO>().in("id", cartIds));

        boolean allReply = true;
        for (ShopCartDTO dto : dtos) {
            if (productId.equals(dto.getProductId() + "")) {
                dto.setIsReply(1);
                shopCartMapper.updateById(dto);
            }
            if ("0".equals(dto.getIsReply())) {
                allReply = false; //???????????????????????????????????????????????????
            }
        }
        if (allReply) {
            //???????????????????????? ????????????????????????
            orderMapper.completeOrder(orderId);
        }
    }

    /**
     * ????????????
     *
     * @param orderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int refundOrder(String orderId) {
        OrderDTO orderDTO = orderMapper.selectOne(new QueryWrapper<OrderDTO>().eq("order_id", orderId));
        if (orderDTO == null) {
            throw new ServiceException("???????????????");
        }
        if (!"1".equals(orderDTO.getStatus())) {
            throw new ServiceException("??????????????????");
        }

        UserOrderVO userOrderVO = this.selectUserOrderDetail(orderId);

        BigDecimal refundMoney = userOrderVO.getPayMoney();

        OrderRefundDTO orderRefundDTO = new OrderRefundDTO();
        orderRefundDTO.setOrderId(orderDTO.getOrderId());
        orderRefundDTO.setPayMoney(orderDTO.getPayMoney());
        orderRefundDTO.setRefundMoney(refundMoney);
        orderRefundDTO.setType("1");
        orderRefundDTO.setRemark("????????????????????????");
        orderRefundMapper.insert(orderRefundDTO);

        //???????????????????????????
        for (UserProductVO userProductVO : userOrderVO.getUserProductVOS()) {
            OrderRefundProductDTO refundProductDTO = new OrderRefundProductDTO();
            refundProductDTO.setRefundOrderId(orderRefundDTO.getId());
            refundProductDTO.setPayNum(userProductVO.getNum());
            refundProductDTO.setRefundNum(0);
            refundProductDTO.setPrice(userProductVO.getPrice());
            refundProductDTO.setAttrValue(userProductVO.getAttrValue());
            refundProductDTO.setProductId(userProductVO.getProductDTO().getId());
            refundProductDTO.setProductJson(userProductVO.getProductInfoJson());
            orderRefundProductMapper.insert(refundProductDTO);
        }

        //????????????????????????
        OrderDTO dto = new OrderDTO();
        dto.setId(orderDTO.getId());
        dto.setRefundStatus("1"); // ?????? ?????????
        return orderMapper.updateById(dto);
    }

    @Override
    public IPage<UserOrderVO> selectUserRefundOrderList(RefundOrderListRO refundOrderListRO) {
        refundOrderListRO.setUserId(tokenUtil.getUserIdByToken());
        IPage<UserOrderVO> page = new Page<>(refundOrderListRO.getCurrentPage(), refundOrderListRO.getPageSize());
        IPage<UserOrderVO> iPage = orderMapper.selectUserRefundOrderList(page, refundOrderListRO);
        return iPage;
    }

}
