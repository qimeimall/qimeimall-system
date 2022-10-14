package com.qimeixun.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.constant.SystemConfigConstants;
import com.qimeixun.constant.SystemConstant;
import com.qimeixun.entity.Customer;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.*;
import com.qimeixun.po.*;
import com.qimeixun.service.OrderCommonService;
import com.qimeixun.service.SysConfigService;
import com.qimeixun.util.RedisUtil;
import com.qimeixun.util.TokenUtil;
import com.qimeixun.vo.OrderProductListVO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class OrderCommonServiceImpl implements OrderCommonService {

    @Resource
    OrderMapper orderMapper;

    @Resource
    ProductMapper productMapper;

    @Resource
    SeckillProductMapper seckillProductMapper;

    @Resource
    RedisUtil redisUtil;

    @Resource
    CustomerMapper customerMapper;

    @Resource
    SysConfigService sysConfigService;

    @Resource
    BrokerageMapper brokerageMapper;

    @Resource
    CouponUserMapper couponUserMapper;


    @Override
    public int cancelOrder(String orderId, String userId) {
        OrderDTO orderDTO = orderMapper.selectOne(new QueryWrapper<OrderDTO>().eq("order_id", orderId));
        if (orderDTO == null) {
            throw new ServiceException("订单不存在");
        }
        if (!"0".equals(orderDTO.getStatus())) {
            throw new ServiceException("订单状态错误");
        }
        QueryWrapper<OrderDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        if(StrUtil.isNotBlank(userId)){
            queryWrapper.eq("user_id", userId);
        }
        orderDTO.setStatus("10");
        int i = orderMapper.update(orderDTO, queryWrapper);

        //退回库存
        List<OrderProductListVO> list = orderMapper.selectOrderProductCountList(orderDTO.getOrderId());
        addOrSubStock(list, SystemConstant.STOCK_TYPE_ADD);

        if (i == 0) {
            throw new ServiceException("取消订单失败");
        }
        return i;
    }

    /**
     * 扣减库存
     *
     * @param orderProductListVOS
     */
    @Override
    public synchronized void addOrSubStock(List<OrderProductListVO> orderProductListVOS, String type) {
        if (CollectionUtils.isEmpty(orderProductListVOS)) throw new ServiceException("数据异常，请联系客服");

        switch (type){
            case SystemConstant.STOCK_TYPE_ADD:
                addStock(orderProductListVOS);
                break;
            case SystemConstant.STOCK_TYPE_SUB:
                subStock(orderProductListVOS);
                break;
            default:
                throw new ServiceException("参数异常，请联系客服");
        }

    }

    private void addStock(List<OrderProductListVO> orderProductListVOS){
        for (OrderProductListVO orderProductListVO : orderProductListVOS) {
            if(StrUtil.isBlank(orderProductListVO.getSeckillProductId())){ //退普通购买库存
                ProductDTO productDTO = productMapper.selectById(orderProductListVO.getProductId());
                if (productDTO == null) {
                    throw new ServiceException("商品不存在");
                }

                ProductDTO dto = new ProductDTO();
                dto.setId(productDTO.getId());
                dto.setSalesCount(productDTO.getSalesCount() - orderProductListVO.getNum());
                dto.setStockCount(productDTO.getStockCount() + orderProductListVO.getNum());
                productMapper.updateById(dto);
            }else{ // 退秒杀订单库存
                //扣减秒杀库存
                SeckillProductDTO seckillProductDTO = seckillProductMapper.selectById(orderProductListVO.getSeckillProductId());
                seckillProductDTO.setStock(seckillProductDTO.getStock() + orderProductListVO.getNum());
                seckillProductDTO.setSalesStock(seckillProductDTO.getSalesStock() - orderProductListVO.getNum());
                seckillProductMapper.updateById(seckillProductDTO);

                int cacheProductCount = 0;
                if(redisUtil.hasKey(SystemConstant.SECKILL_PRODUCT + orderProductListVO.getSeckillProductId())) {
                    Object o = redisUtil.get(SystemConstant.SECKILL_PRODUCT + orderProductListVO.getSeckillProductId());
                    cacheProductCount = Integer.valueOf(String.valueOf(o));

                    //扣减缓存库存
                    long time = (seckillProductDTO.getEndTime().getTime() - new Date().getTime()) / 1000 / 60;
                    redisUtil.set(SystemConstant.SECKILL_PRODUCT + seckillProductDTO.getId(), cacheProductCount - orderProductListVO.getNum(), time);
                }
            }
        }
    }

    private void subStock(List<OrderProductListVO> orderProductListVOS){
        for (OrderProductListVO orderProductListVO : orderProductListVOS) {
            if(StrUtil.isBlank(orderProductListVO.getSeckillProductId())){ //退普通购买库存
                ProductDTO productDTO = productMapper.selectById(orderProductListVO.getProductId());
                if (productDTO == null) {
                    throw new ServiceException("商品不存在");
                }

                ProductDTO dto = new ProductDTO();
                dto.setId(productDTO.getId());
                dto.setSalesCount(productDTO.getSalesCount() + orderProductListVO.getNum());
                dto.setStockCount(productDTO.getStockCount() - orderProductListVO.getNum());
                productMapper.updateById(dto);
            }else{ // 退秒杀订单库存
                //扣减秒杀库存
                SeckillProductDTO seckillProductDTO = seckillProductMapper.selectById(orderProductListVO.getSeckillProductId());
                seckillProductDTO.setStock(seckillProductDTO.getStock() - orderProductListVO.getNum());
                seckillProductDTO.setSalesStock(seckillProductDTO.getSalesStock() + orderProductListVO.getNum());
                seckillProductMapper.updateById(seckillProductDTO);

                int cacheProductCount = 0;
                if(redisUtil.hasKey(SystemConstant.SECKILL_PRODUCT + orderProductListVO.getSeckillProductId())) {
                    Object o = redisUtil.get(SystemConstant.SECKILL_PRODUCT + orderProductListVO.getSeckillProductId());
                    cacheProductCount = Integer.valueOf(String.valueOf(o));

                    //扣减缓存库存
                    long time = (seckillProductDTO.getEndTime().getTime() - new Date().getTime()) / 1000 / 60;
                    redisUtil.set(SystemConstant.SECKILL_PRODUCT + seckillProductDTO.getId(), cacheProductCount + orderProductListVO.getNum(), time);
                }
            }
        }
    }

    /**
     * 确认收货
     *
     * @param orderId
     * @return
     */
    @Override
    public int confirmReceiptGoods(String orderId, String userId) {
        OrderDTO orderDTO = orderMapper.selectOne(new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getOrderId, orderId));
        if(orderDTO == null){
            throw new ServiceException("订单不存在");
        }
        if(StrUtil.isNotBlank(userId) && Long.valueOf(userId) != Long.valueOf(orderDTO.getUserId())){
            throw new ServiceException("非法操作");
        }
        if(!"2".equals(orderDTO.getStatus())){
            throw new ServiceException("订单状态异常");
        }
        int i = orderMapper.confirmReceiptGoods(orderId);

        //返佣 普通订单返佣
        if("0".equals(orderDTO.getIsPoints())){
            userBrokerage(orderDTO, userId);
        }
        return i;
    }

    /**
     * 用戶返佣
     */
    private void userBrokerage(OrderDTO orderDTO, String userId){

        Customer customer = customerMapper.selectById(userId);
        if(customer == null || customer.getReferrerId() == null || customer.getReferrerId().longValue() == 0){
            return;
        }

        //查詢上一級代理
        Customer pCustomer = customerMapper.selectById(customer.getReferrerId());
        if(pCustomer != null){
            // 一級代理返佣 level_one_rebate
            String valueFromRedis = sysConfigService.getSysConfigValueFromRedis(SystemConfigConstants.LEVEL_ONE_REBATE);
            if(StrUtil.isNotBlank(valueFromRedis)){
                BigDecimal money = NumberUtil.div(NumberUtil.mul(orderDTO.getPayMoney(), Double.valueOf(valueFromRedis)), 100);
                BrokerageDTO brokerageDTO = new BrokerageDTO();
                brokerageDTO.setBuyUserId(Long.valueOf(userId));
                brokerageDTO.setOrderId(orderDTO.getOrderId());
                brokerageDTO.setBuyUserId(pCustomer.getId());
                brokerageDTO.setConsumerMoney(orderDTO.getPayMoney());
                brokerageDTO.setCommissionMoney(money);
                brokerageDTO.setStatus("0");
                brokerageMapper.insert(brokerageDTO);
            }
        }


        //查詢上二級代理
        if(pCustomer == null || pCustomer.getReferrerId() == null || pCustomer.getReferrerId().longValue() == 0){
            return;
        }
        Customer tCustomer = customerMapper.selectById(pCustomer.getReferrerId());
        if(tCustomer != null){
            // 一級代理返佣 level_one_rebate
            String valueFromRedis = sysConfigService.getSysConfigValueFromRedis(SystemConfigConstants.LEVEL_TWO_REBATE);
            if(StrUtil.isNotBlank(valueFromRedis)){
                BigDecimal money =  NumberUtil.div(NumberUtil.mul(orderDTO.getPayMoney(), Double.valueOf(valueFromRedis)), 100);

                BrokerageDTO brokerageDTO = new BrokerageDTO();
                brokerageDTO.setBuyUserId(Long.valueOf(userId));
                brokerageDTO.setOrderId(orderDTO.getOrderId());
                brokerageDTO.setBuyUserId(pCustomer.getId());
                brokerageDTO.setConsumerMoney(orderDTO.getPayMoney());
                brokerageDTO.setCommissionMoney(money);
                brokerageDTO.setStatus("0");
                brokerageMapper.insert(brokerageDTO);
            }
        }
    }

    /**
     * 设置优惠券已使用
     * @param couponId
     */
    @Override
    public void setCouponIsUsed(Long couponId) {
        if(couponId != null){
            //优惠券不未空 表示使用过优惠券 将优惠券设置为使用
            UserCouponDTO userCouponDTO = new UserCouponDTO();
            userCouponDTO.setIsUsed(1);
            QueryWrapper<UserCouponDTO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(UserCouponDTO::getId, couponId).eq(UserCouponDTO::getIsUsed, "0");
            int i = couponUserMapper.update(userCouponDTO, queryWrapper);
            if(i <= 0){
                throw new ServiceException("该优惠券不能使用");
            }
        }
    }
}
