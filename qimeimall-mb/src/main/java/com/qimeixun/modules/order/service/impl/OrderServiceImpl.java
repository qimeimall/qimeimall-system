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

        //店铺优惠
        BigDecimal storeCoupnMoney = new BigDecimal(0);
        //        //商品总额
        BigDecimal totalGoodsMoney = new BigDecimal(0);
        //总的运费金额
        BigDecimal totalPostMoney = new BigDecimal(0);
        //平台优惠金额
        BigDecimal platformCouponMoney = new BigDecimal(0);
        //积分抵扣金额
        BigDecimal pointsMoney = new BigDecimal(0);
        //优惠后金额
        BigDecimal couponAfterMoney = new BigDecimal(0);

        List<ShopCartListVO> allShopCartListVOS = new ArrayList<>();
        //获取购物车中的产品id
        CartOrderDTO cartOrderDTO = getCartOrderDTOS(userCouponOrderRO.getIds());

        BigDecimal[] maxPostMoney = {new BigDecimal(0)};
        allShopCartListVOS.addAll(cartOrderDTO.getShopCartListVOS());
        for (ShopCartListVO shopCartListVO : cartOrderDTO.getShopCartListVOS()) {
            totalGoodsMoney = totalGoodsMoney.add(NumberUtil.mul(shopCartListVO.getNum(), shopCartListVO.getPrice()));
            //所有商品中最贵的邮费为当前订单的邮费
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
                //获取当前门店是否选择优惠券
                storeCoupnMoney = storeCoupnMoney.add(getMaxCouponMoney(c.getCouponId(), cartOrderDTO.getShopCartListVOS()));
            }
        }

        couponAfterMoney = totalGoodsMoney.subtract(storeCoupnMoney);
        //获取平台优惠金额
        platformCouponMoney = platformCouponMoney.add(getMaxCouponMoney(userCouponOrderRO.getCouponId(), allShopCartListVOS));
        if (couponAfterMoney.compareTo(platformCouponMoney) == -1) {
            platformCouponMoney = couponAfterMoney;
        }
        couponAfterMoney = couponAfterMoney.subtract(platformCouponMoney); //减去平台优惠后的价格
        Customer customer = customerMapper.selectById(tokenUtil.getUserIdByToken());
        if (userCouponOrderRO.isPoints()) {
            //开启积分抵扣
            //100积分等于1元
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
        //通过传进来的优惠券计算 商品总额 积分抵扣金额 运费  店铺优惠金额 平台优惠金额 总金额
        return resultMap;

    }

    /**
     * 获取该优惠券最多能优惠的金额
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
            throw new ServiceException("非法优惠券，禁止使用");
        }
        //可以使用的产品id集合
        List<ShopCartListVO> lists = new ArrayList<>();
        List<Integer> integers = JSON.parseArray(userCouponDTO.getEffectProductId(), Integer.class);
        if ("2".equals(userCouponDTO.getType()) || CollectionUtils.isEmpty(integers)) {
            //表示该店全品类券
            lists = shopCartListVOS;
        }else{
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
        }

        //计算所有符合优惠的产品集合 计算价格是否满足
        BigDecimal totalMoney = new BigDecimal(0);
        for (ShopCartListVO shopCartListVO : lists) {
            totalMoney = totalMoney.add(NumberUtil.mul(shopCartListVO.getPrice(), shopCartListVO.getNum()));
        }

        if (!CollectionUtils.isEmpty(lists)) {
            if (totalMoney.compareTo(userCouponDTO.getMinConsume()) >= 0) {
                //商品总价格大于等于优惠券的最低限额
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
            throw new ServiceException("数据异常");
        }
        return orderMapper.selectOrderInfoCreateBefore(ids);
    }

    @Override
    @Transactional
    public OrderDTO createOrder(UserCouponOrderRO userCouponOrderRO) {
        //通过ids查询购物车中的产品信息
        CartOrderDTO cartOrderDTO = getCartOrderDTOS(userCouponOrderRO.getIds());


        //List<OrderDTO> orderDTOS = new ArrayList<>();
        List<ShopCartListVO> allShopCartListVOS = new ArrayList<>();


        allShopCartListVOS.addAll(cartOrderDTO.getShopCartListVOS());
        //创建订单 生成订单id
        String orderId = IdUtil.getSnowflake(1, 1).nextIdStr();
        BigDecimal totalMoney = new BigDecimal(0);
        int totalNum = 0;
        //订单和购物车id关联集合
        List<Map<String, Object>> orderCartsRelation = new ArrayList<>();
        //购物车订单id
        List<Integer> cartIds = new ArrayList<>();
        //计算所有商品的总价
        for (ShopCartListVO shopCartListVOS : cartOrderDTO.getShopCartListVOS()) {
            totalMoney = totalMoney.add(NumberUtil.mul(shopCartListVOS.getNum(), shopCartListVOS.getPrice()));
            totalNum = totalNum + shopCartListVOS.getNum();

            Map<String, Object> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("cartId", shopCartListVOS.getId());
            cartIds.add(shopCartListVOS.getId());

            orderCartsRelation.add(map);

            //查询商品信息
            ProductDTO productDTO = productMapper.selectById(shopCartListVOS.getProductId());
            if (productDTO != null) {
                shopCartListVOS.setProductInfo(JSON.toJSONString(productDTO));
                if (StringUtils.isEmpty(shopCartListVOS.getAttrValue())) {
                    shopCartListVOS.setPrice(productDTO.getPrice());
                }
            }
        }
        //构造订单 插入订单
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
            //平台优惠券不为空
            platformCouponMoney = getMaxCouponMoney(userCouponOrderRO.getCouponId(), cartOrderDTO.getShopCartListVOS());
            if (platformCouponMoney.compareTo(BigDecimal.ZERO) == 1) { //platformCouponMoney 大于0
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
        //设置门店优惠金额
        orderDTO.setStoreCouponMoney(storeCouponMoney);
        //收货地址信息

        setOrderAddress(orderDTO, userCouponOrderRO.getAddressId(), userCouponOrderRO.getBuyType(), userCouponOrderRO.getStoreId());
        orderDTO.setRemark(userCouponOrderRO.getRemark());

        orderMapper.insert(orderDTO);

        //插入订单与购物车的关系
        checkOrderCart(cartIds, tokenUtil.getUserIdByToken());
        orderMapper.insertOrderCartRelation(orderCartsRelation);

        //扣减库存
        subStock(cartOrderDTO.getShopCartListVOS());

        //修改购物车中商品状态为待支付
        shopCartMapper.updateCartPayStatus(cartOrderDTO.getShopCartListVOS());

        orderCommonService.setCouponIsUsed(userCouponOrderRO.getCouponId()); //设置优惠券为已使用

        //redis 写入缓存
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
            throw new ServiceException("商品不存在");
        }

        if(CollectionUtils.isEmpty(userCouponOrderRO.getCurNav()) || userCouponOrderRO.getCurNav().size() != 1){
            throw new ServiceException("商品规格错误");
        }

        if ("1".equals(userCouponOrderRO.getProductType())) {
            //积分兑换订单
            if (!"1".equals(productDTO.getIsPoints())) {
                throw new ServiceException("非积分兑换商品");
            }
        } else {
            userCouponOrderRO.setProductType("0");
        }

        ProductAttrDetailDTO productAttrDetailDTO = getProductAttr(userCouponOrderRO.getCurNav(), userCouponOrderRO.getProductId());
        if (productAttrDetailDTO == null) {
            throw new ServiceException("该商品不存在");
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

        //构造订单 插入订单
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
        //设置平台优惠金额
        orderDTO.setPlatformCouponMoney(couponMoney);
        orderDTO.setCouponId(userCouponOrderRO.getCouponId());

        if ("1".equals(userCouponOrderRO.getProductType())) {
            orderDTO.setOrderType(OrderTypeEnum.ORDER_TYPE_POINTS.getType());
        }
        //收货地址信息
        setOrderAddress(orderDTO, userCouponOrderRO.getAddressId(), userCouponOrderRO.getBuyType(), userCouponOrderRO.getStoreId());

        orderDTO.setRemark(userCouponOrderRO.getRemark());
        orderDTO.setIsPoints(userCouponOrderRO.getProductType());

        orderMapper.insert(orderDTO);

        //插入订单与购物车的关系
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

        //扣减库存
        subStock(allShopCartListVOS);

        //订单和购物车id关联集合
        List<Map<String, Object>> orderCartsRelation = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderDTO.getOrderId());
        map.put("cartId", shopCartDTO.getId());

        orderCartsRelation.add(map);

        orderMapper.insertOrderCartRelation(orderCartsRelation);

        orderCommonService.setCouponIsUsed(userCouponOrderRO.getCouponId()); //设置优惠券为已使用

        //redis 写入缓存
        redisUtil.set(SystemConstant.REDIS_CANCEL_KEY + orderDTO.getOrderId(), SystemConstant.REDIS_CANCEL_KEY
                + orderDTO.getOrderId(), Integer.parseInt(sysConfigService.getSysConfigValueFromRedis(SystemConstant.CANCEL_ORDER_TIME)));

        return orderDTO;
    }

    /**
     * 秒杀订单
     *
     * @param userCouponOrderRO
     * @return
     */
    private OrderDTO createKillOrder(UserSingleOrderRO userCouponOrderRO) {

        SeckillProductDTO seckillProductDTO = seckillProductMapper.selectById(userCouponOrderRO.getSeckillProductId());
        if (seckillProductDTO == null) {
            throw new ServiceException("产品不存在");
        }

        int cacheProductCount = 0;
        if (redisUtil.hasKey(SystemConstant.SECKILL_PRODUCT + userCouponOrderRO.getSeckillProductId())) {
            Object o = redisUtil.get(SystemConstant.SECKILL_PRODUCT + userCouponOrderRO.getSeckillProductId());
            cacheProductCount = Integer.valueOf(String.valueOf(o));
            if (cacheProductCount < userCouponOrderRO.getNum()) {
                throw new ServiceException("库存不足");
            }
        } else {
            throw new ServiceException("该秒杀商品不存在");
        }

        BigDecimal price = seckillProductDTO.getSeckillPrice(); //获取秒杀产品价格

        ProductDTO productDTO = productMapper.selectById(userCouponOrderRO.getProductId());

        ProductAttrDetailDTO productAttr = getProductAttr(userCouponOrderRO.getCurNav(), userCouponOrderRO.getProductId());


        List<ShopCartListVO> allShopCartListVOS = new ArrayList<>();
        ShopCartListVO cartListVO = new ShopCartListVO();
        cartListVO.setProductId(productDTO.getId());
        cartListVO.setCategoryId(productDTO.getCategoryId());
        cartListVO.setNum(userCouponOrderRO.getNum());
        allShopCartListVOS.add(cartListVO);

        //构造订单 插入订单
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
        //设置门店优惠金额
        orderDTO.setStoreCouponMoney(new BigDecimal(0)); //优惠金额为0
        //收货地址信息
        setOrderAddress(orderDTO, userCouponOrderRO.getAddressId(), userCouponOrderRO.getBuyType(), userCouponOrderRO.getStoreId());

        orderDTO.setRemark(userCouponOrderRO.getRemark());

        orderMapper.insert(orderDTO);

        //插入订单与购物车的关系
        ShopCartDTO shopCartDTO = new ShopCartDTO();
        shopCartDTO.setUserId(Integer.valueOf(tokenUtil.getUserIdByToken()));
        shopCartDTO.setNum(userCouponOrderRO.getNum());
        shopCartDTO.setProductAttrId(productAttr.getId());
        shopCartDTO.setIsPay(1);
        shopCartDTO.setProductId(Long.valueOf(userCouponOrderRO.getProductId()));
        shopCartDTO.setProductInfo(JSON.toJSONString(productDTO));
        shopCartDTO.setAttrValue(ArrayUtil.join(userCouponOrderRO.getCurNav().toArray(), ",") + ",");
        shopCartDTO.setPrice(price);

        //是秒杀订单
        shopCartDTO.setSeckillProductId(Long.valueOf(userCouponOrderRO.getSeckillProductId()));


        shopCartMapper.insert(shopCartDTO);

        //扣减秒杀库存
        //subStock(allShopCartListVOS);

        seckillProductDTO.setStock(seckillProductDTO.getStock() - userCouponOrderRO.getNum());
        seckillProductMapper.updateById(seckillProductDTO);

        //扣减缓存库存
        long time = (seckillProductDTO.getEndTime().getTime() - new Date().getTime()) / 1000 / 60;
        redisUtil.set(SystemConstant.SECKILL_PRODUCT + seckillProductDTO.getId(), cacheProductCount - userCouponOrderRO.getNum(), time);

        //订单和购物车id关联集合
        List<Map<String, Object>> orderCartsRelation = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderDTO.getOrderId());
        map.put("cartId", shopCartDTO.getId());

        orderCartsRelation.add(map);

        orderMapper.insertOrderCartRelation(orderCartsRelation);

        //redis 写入缓存
        redisUtil.set(SystemConstant.REDIS_CANCEL_KEY + orderDTO.getOrderId(), SystemConstant.REDIS_CANCEL_KEY
                + orderDTO.getOrderId(), Integer.parseInt(sysConfigService.getSysConfigValueFromRedis(SystemConstant.CANCEL_ORDER_TIME)));

        return orderDTO;

    }

    private void setOrderAddress(OrderDTO orderDTO, String addressId, String buyType, String storeId) {

        if ("1".equals(buyType)) {

            //查询收货地址
            if (StrUtil.isBlank((addressId))) {
                throw new ServiceException("请选择收获地址");
            }
            AddressDTO addressDTO = addressMapper.selectById(addressId);
            if (addressDTO == null) {
                throw new ServiceException("收货地址不存在");
            }
            orderDTO.setBuyType(buyType);
            orderDTO.setName(addressDTO.getName());
            orderDTO.setPhone(addressDTO.getPhone());
            orderDTO.setAddress(addressDTO.getAddress());

        } else if ("2".equals(buyType)) {

            //查询门店
            if (StrUtil.isBlank(storeId)) {
                throw new ServiceException("请选择收获自提门店");
            }
            Store store = storeMapper.selectById(storeId);
            if (store == null) {
                throw new ServiceException("门店不存在");
            }

            orderDTO.setBuyType(buyType);
            orderDTO.setName(store.getStoreName());
            orderDTO.setPhone(store.getStoreTell());
            orderDTO.setAddress(store.getStoreAddress());
            orderDTO.setStoreId(storeId);
        } else {
            throw new ServiceException("参数异常");
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
     * 发起退货申请
     *
     * @param refundGoodsRO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundOrderGoods(RefundGoodsRO refundGoodsRO) {
        if (CollectionUtils.isEmpty(refundGoodsRO.getChildrenROList())) {
            throw new ServiceException("请选择要售后商品数量");
        }

        OrderDTO orderDTO = orderMapper.selectOne(new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getOrderId, refundGoodsRO.getOrderId()).eq(OrderDTO::getUserId, tokenUtil.getUserIdByToken()));
        if (orderDTO == null) {
            throw new ServiceException("订单不存在");
        }
        if ("1".equals(orderDTO.getRefundStatus()) || "3".equals(orderDTO.getRefundStatus())) {
            throw new ServiceException("该订单已经申请售后");
        }
        UserOrderVO userOrderVO = this.selectUserOrderDetail(refundGoodsRO.getOrderId());

        if(userOrderVO.getPayMoney().compareTo(BigDecimal.ZERO) == 0){
            throw new ServiceException("请联系客服退换货");
        }

        BigDecimal refundMoney = BigDecimal.ZERO;

        //校验商品退款退货数量是否超出原下单量
        for (RefundGoodsChildrenRO refundGoodsChildrenRO : refundGoodsRO.getChildrenROList()) {
            for (UserProductVO userProductVO : userOrderVO.getUserProductVOS()) {
                if (refundGoodsChildrenRO.getProductId().equals(userProductVO.getProductDTO().getId()) &&
                        refundGoodsChildrenRO.getAttrValue().equals(userProductVO.getAttrValue())
                ) {
                    refundMoney = refundMoney.add(userProductVO.getPrice().multiply(new BigDecimal(refundGoodsChildrenRO.getRefundNum())));
                    userProductVO.setRefundNum(refundGoodsChildrenRO.getRefundNum());
                    if (refundGoodsChildrenRO.getRefundNum() > userProductVO.getNum()) {
                        throw new ServiceException("退款商品数量超出下单量");
                    }
                }
            }
        }

        if(refundMoney.compareTo(userOrderVO.getPayMoney()) == 1){
            refundMoney = userOrderVO.getPayMoney();
        }

        //插入售后申请订单
        if (refundMoney.compareTo(BigDecimal.ZERO) == 0) {
            throw new ServiceException("退款金额为0");
        }

        OrderRefundDTO orderRefundDTO = new OrderRefundDTO();
        orderRefundDTO.setOrderId(orderDTO.getOrderId());
        orderRefundDTO.setPayMoney(orderDTO.getPayMoney());
        orderRefundDTO.setRefundMoney(refundMoney);
        orderRefundDTO.setType(refundGoodsRO.getType());
        orderRefundDTO.setRemark(refundGoodsRO.getRemark());
        orderRefundMapper.insert(orderRefundDTO);

        //插入退货商品数量表
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

        //修改订单退货状态
        OrderDTO dto = new OrderDTO();
        dto.setId(orderDTO.getId());
        dto.setRefundStatus("1"); // 退款 退货中
        orderMapper.updateById(dto);
    }

    @Override
    public Map selectRefundOrderGoodsDetail(String refundId) {
        OrderRefundDTO orderRefundDTO = orderRefundMapper.selectById(refundId);
        OrderDTO orderDTO = orderMapper.selectOne(new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getOrderId, orderRefundDTO.getOrderId()));
        if (orderDTO == null) {
            throw new ServiceException("订单不存在");
        }
        if (!tokenUtil.getUserIdByToken().equals(String.valueOf(orderDTO.getUserId()))) {
            throw new ServiceException("非法数据");
        }
        List<OrderRefundProductDTO> refundProductDTOS = orderRefundProductMapper.selectList(
                new QueryWrapper<OrderRefundProductDTO>().lambda().in(OrderRefundProductDTO::getRefundOrderId, refundId));

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("refundOrder", orderRefundDTO);
        resultMap.put("productList", refundProductDTOS);
        return resultMap;
    }

    /**
     * 撤销申请
     *
     * @param refundId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelRefundOrder(String refundId) {
        OrderRefundDTO orderRefundDTO = orderRefundMapper.selectById(refundId);
        if (orderRefundDTO == null) {
            throw new ServiceException("该申请不存在");
        }

        LambdaQueryWrapper<OrderRefundDTO> queryWrapper = new QueryWrapper<OrderRefundDTO>().lambda();
        queryWrapper.eq(OrderRefundDTO::getId, refundId);
        queryWrapper.eq(OrderRefundDTO::getStatus, "1");

        OrderRefundDTO refundDTO = new OrderRefundDTO();
        refundDTO.setStatus("4");
        orderRefundMapper.update(refundDTO, queryWrapper);

        //修改订单状态
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setRefundStatus("0");
        int i = orderMapper.update(orderDTO, new QueryWrapper<OrderDTO>().lambda().
                eq(OrderDTO::getOrderId, orderRefundDTO.getOrderId()).eq(OrderDTO::getUserId, tokenUtil.getUserIdByToken()));
        if (i == 0) {
            throw new ServiceException("撤销申请异常、请联系客服");
        }
    }

    /**
     * 扣减库存
     *
     * @param shopCartListVOS
     */
    private void subStock(List<ShopCartListVO> shopCartListVOS) {
        if (CollectionUtils.isEmpty(shopCartListVOS)) throw new ServiceException("购买商品不存在");

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
     * 查询商品属性id
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
            throw new ServiceException("商品规格不存在");
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
                throw new ServiceException("下单失败");
            }
        }

        QueryWrapper<OrderCartDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("cart_id", cartIds);
        int count = orderCartMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ServiceException("购物车商品已重复提交");
        }

    }

    /**
     * 查询用户的订单
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
            throw new ServiceException("订单不存在");
        }
        return userOrderVO;
    }

    /**
     * 修改订单是否已评价
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
                allReply = false; //有一个没有评价表示该订单仍需要评价
            }
        }
        if (allReply) {
            //所有订单评价完毕 改成订单为已完成
            orderMapper.completeOrder(orderId);
        }
    }

    /**
     * 申请退款
     *
     * @param orderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int refundOrder(String orderId) {
        OrderDTO orderDTO = orderMapper.selectOne(new QueryWrapper<OrderDTO>().eq("order_id", orderId));
        if (orderDTO == null) {
            throw new ServiceException("订单不存在");
        }
        if (!"1".equals(orderDTO.getStatus())) {
            throw new ServiceException("订单状态错误");
        }

        UserOrderVO userOrderVO = this.selectUserOrderDetail(orderId);

        BigDecimal refundMoney = userOrderVO.getPayMoney();

        OrderRefundDTO orderRefundDTO = new OrderRefundDTO();
        orderRefundDTO.setOrderId(orderDTO.getOrderId());
        orderRefundDTO.setPayMoney(orderDTO.getPayMoney());
        orderRefundDTO.setRefundMoney(refundMoney);
        orderRefundDTO.setType("1");
        orderRefundDTO.setRemark("用户发起退款申请");
        orderRefundMapper.insert(orderRefundDTO);

        //插入退货商品数量表
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

        //修改订单退货状态
        OrderDTO dto = new OrderDTO();
        dto.setId(orderDTO.getId());
        dto.setRefundStatus("1"); // 退款 退货中
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
