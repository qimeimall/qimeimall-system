package com.qimeixun.modules.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.constant.SystemConstant;
import com.qimeixun.enums.PointsBillEnum;
import com.qimeixun.enums.UserBillEnum;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.CustomerMapper;
import com.qimeixun.mapper.OrderMapper;
import com.qimeixun.mapper.OrderRefundMapper;
import com.qimeixun.modules.customer.service.CustomerService;
import com.qimeixun.modules.order.service.OrderService;
import com.qimeixun.po.OrderDTO;
import com.qimeixun.po.OrderRefundDTO;
import com.qimeixun.ro.ExpressOrderRO;
import com.qimeixun.ro.OrderListRO;
import com.qimeixun.ro.RefundOrderSystemRO;
import com.qimeixun.ro.RefundUpdateRO;
import com.qimeixun.service.SysConfigService;
import com.qimeixun.util.RedisUtil;
import com.qimeixun.vo.UserOrderVO;
import com.qimeixun.vo.UserProductVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/6/310:54
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    OrderMapper orderMapper;

    @Resource
    OrderRefundMapper orderRefundMapper;

    @Resource
    RedisUtil redisUtil;

    @Resource
    SysConfigService sysConfigService;

    @Resource
    CustomerService customerService;

    @Override
    public IPage<UserOrderVO> selectOrderList(OrderListRO orderListRO) {
        Page page = new Page<UserOrderVO>(orderListRO.getCurrentPage(), orderListRO.getPageSize());
        IPage<UserOrderVO> list = orderMapper.selectOrderList(page, orderListRO);
        for (UserOrderVO userOrderVO : list.getRecords()) {
            List<UserProductVO> productVOS = userOrderVO.getUserProductVOS();
            int totalNum = 0;
            for (UserProductVO userProductVO : productVOS) {
                totalNum = totalNum + userProductVO.getNum();
            }
            userOrderVO.setTotalNum(totalNum);
        }
        return list;
    }

    @Override
    public IPage selectRefundOrderList(RefundOrderSystemRO orderListRO) {
        IPage<UserOrderVO> page = new Page<>(orderListRO.getCurrentPage(), orderListRO.getPageSize());
        IPage<UserOrderVO> iPage = orderMapper.selectRefundOrderList(page, orderListRO);
        return iPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void accessRefund(RefundUpdateRO refundUpdateR) {
        //同意退款
        OrderRefundDTO orderRefundDTO = orderRefundMapper.selectById(refundUpdateR.getRefundId());
        if (orderRefundDTO == null) {
            throw new ServiceException("退款申请不存在");
        }
        //1 审核中 2：审核拒绝 3 ：审核通过 4:已撤销
        OrderRefundDTO refundDTO = new OrderRefundDTO();
        refundDTO.setStatus("3");
        if(StrUtil.isNotBlank(refundUpdateR.getRemark())){
            if(refundUpdateR.getRemark().length() >= 200){
                throw new ServiceException("备注不能太长");
            }
            refundDTO.setSystemRemark(refundUpdateR.getRemark());
        }
        LambdaQueryWrapper<OrderRefundDTO> queryWrapper = new QueryWrapper<OrderRefundDTO>().lambda().eq(OrderRefundDTO::getId, orderRefundDTO.getId())
                .eq(OrderRefundDTO::getStatus, "1");
        int i = orderRefundMapper.update(refundDTO, queryWrapper);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setRefundStatus("3");  //0 :未退款  1：售后中  2退货拒绝 3 同意退货
        orderDTO.setStatus("6"); //0:未付款  1:未发货 2：待收货  3：已收货  4：待评价 5：已完成  6：待退款  7：已退款 10：已取消
        orderDTO.setRefundMoney(orderRefundDTO.getRefundMoney()); //退款金额
        orderMapper.update(orderDTO, new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getOrderId, orderRefundDTO.getOrderId()));

        if(i == 0){
            throw new ServiceException("失败");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refusedRefund(RefundUpdateRO refundUpdateR) {
        //同意退款
        OrderRefundDTO orderRefundDTO = orderRefundMapper.selectById(refundUpdateR.getRefundId());
        if (orderRefundDTO == null) {
            throw new ServiceException("退款申请不存在");
        }
        if(StrUtil.isBlank(refundUpdateR.getRemark())){
            throw new ServiceException("请填写备注");
        }
        if(refundUpdateR.getRemark().length() >= 200){
            throw new ServiceException("备注不能太长");
        }
        //1 审核中 2：审核拒绝 3 ：审核通过 4:已撤销
        OrderRefundDTO refundDTO = new OrderRefundDTO();
        refundDTO.setStatus("2");
        refundDTO.setSystemRemark(refundUpdateR.getRemark());
        LambdaQueryWrapper<OrderRefundDTO> queryWrapper = new QueryWrapper<OrderRefundDTO>().lambda().eq(OrderRefundDTO::getId, orderRefundDTO.getId())
                .eq(OrderRefundDTO::getStatus, "1");
        int i = orderRefundMapper.update(refundDTO, queryWrapper);

        //修改售后订单状态
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setRefundStatus("2");  //0 :未退款  1：售后中  2退货拒绝 3 同意退货
        orderMapper.update(orderDTO, new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getOrderId, orderRefundDTO.getOrderId()));

        if(i == 0){
            throw new ServiceException("失败");
        }

    }

    /**
     * 立即退款
     * @param refundId
     */
    @Override
    public void refundMoney(String refundId) {
        OrderRefundDTO orderRefundDTO = orderRefundMapper.selectById(refundId);
        if(orderRefundDTO == null){
            throw new ServiceException("退款申请不存在");
        }
        if(!"3".equals(orderRefundDTO.getStatus())){
            throw new ServiceException("当前订单不支持退款");
        }
        OrderDTO orderDTO = orderMapper.selectOne(new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getOrderId, orderRefundDTO.getOrderId()));
        //1:微信支付  2：支付宝支付 3：余额支付 4:积分支付
        if("1".equals(orderDTO.getPayType())){
            //微信退款
        }else if("2".equals(orderDTO.getPayType())){
            //支付宝退款
        }else if("3".equals(orderDTO.getPayType())){
            //余额支付
            customerService.addUserMoney(orderRefundDTO.getRefundMoney(), orderDTO.getUserId(), UserBillEnum.TYPE_REFUND);
        }else if("4".equals(orderDTO.getPayType())){
            //积分支付
            customerService.addUserPoints(orderRefundDTO.getRefundMoney(), orderDTO.getUserId(), PointsBillEnum.TYPE_REFUND);
        }else{
            throw new ServiceException("付款方式不正确");
        }
        orderDTO.setRefundMoney(orderRefundDTO.getRefundMoney());
        orderDTO.setStatus("7"); //已退款
        orderMapper.updateById(orderDTO);

        orderRefundDTO.setStatus("5"); // 已退款
        orderRefundMapper.updateById(orderRefundDTO);
    }

    @Override
    public OrderDTO selectOrderById(String orderId) {
        QueryWrapper<OrderDTO> queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id", orderId);
        return orderMapper.selectOne(queryWrapper);
    }

    @Override
    public int deliverGoods(ExpressOrderRO expressOrderRO) {

        if (StrUtil.isBlank(expressOrderRO.getOrderId())) {
            throw new ServiceException("订单号不能为空");
        }
        OrderDTO orderDTO = orderMapper.selectOne(new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getOrderId, expressOrderRO.getOrderId()));
        if (orderDTO == null) {
            throw new ServiceException("订单不存在");
        }
        if (!"1".equals(orderDTO.getStatus())) {
            throw new ServiceException("订单状态不正常");
        }

        //redis 写入缓存
        redisUtil.set(SystemConstant.AUTO_RECEIVE_KEY + orderDTO.getOrderId(), SystemConstant.AUTO_RECEIVE_KEY
                + orderDTO.getOrderId(), Integer.parseInt(sysConfigService.getSysConfigValueFromRedis(SystemConstant.AUTO_RECEIVE_TIME)));
        return orderMapper.updateExpress(expressOrderRO);
    }

    @Override
    public int userReceiveGoods(String orderId) {
        OrderDTO orderDTO = orderMapper.selectOne(new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getOrderId, orderId));
        if (orderDTO == null) {
            throw new ServiceException("订单不存在");
        }
        OrderDTO dto = new OrderDTO();
        dto.setId(orderDTO.getId());
        dto.setStatus("3");
        return orderMapper.updateById(dto);
    }
}
