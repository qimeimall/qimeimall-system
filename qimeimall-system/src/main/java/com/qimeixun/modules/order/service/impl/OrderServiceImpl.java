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
        //????????????
        OrderRefundDTO orderRefundDTO = orderRefundMapper.selectById(refundUpdateR.getRefundId());
        if (orderRefundDTO == null) {
            throw new ServiceException("?????????????????????");
        }
        //1 ????????? 2??????????????? 3 ??????????????? 4:?????????
        OrderRefundDTO refundDTO = new OrderRefundDTO();
        refundDTO.setStatus("3");
        if(StrUtil.isNotBlank(refundUpdateR.getRemark())){
            if(refundUpdateR.getRemark().length() >= 200){
                throw new ServiceException("??????????????????");
            }
            refundDTO.setSystemRemark(refundUpdateR.getRemark());
        }
        LambdaQueryWrapper<OrderRefundDTO> queryWrapper = new QueryWrapper<OrderRefundDTO>().lambda().eq(OrderRefundDTO::getId, orderRefundDTO.getId())
                .eq(OrderRefundDTO::getStatus, "1");
        int i = orderRefundMapper.update(refundDTO, queryWrapper);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setRefundStatus("3");  //0 :?????????  1????????????  2???????????? 3 ????????????
        orderDTO.setStatus("6"); //0:?????????  1:????????? 2????????????  3????????????  4???????????? 5????????????  6????????????  7???????????? 10????????????
        orderDTO.setRefundMoney(orderRefundDTO.getRefundMoney()); //????????????
        orderMapper.update(orderDTO, new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getOrderId, orderRefundDTO.getOrderId()));

        if(i == 0){
            throw new ServiceException("??????");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refusedRefund(RefundUpdateRO refundUpdateR) {
        //????????????
        OrderRefundDTO orderRefundDTO = orderRefundMapper.selectById(refundUpdateR.getRefundId());
        if (orderRefundDTO == null) {
            throw new ServiceException("?????????????????????");
        }
        if(StrUtil.isBlank(refundUpdateR.getRemark())){
            throw new ServiceException("???????????????");
        }
        if(refundUpdateR.getRemark().length() >= 200){
            throw new ServiceException("??????????????????");
        }
        //1 ????????? 2??????????????? 3 ??????????????? 4:?????????
        OrderRefundDTO refundDTO = new OrderRefundDTO();
        refundDTO.setStatus("2");
        refundDTO.setSystemRemark(refundUpdateR.getRemark());
        LambdaQueryWrapper<OrderRefundDTO> queryWrapper = new QueryWrapper<OrderRefundDTO>().lambda().eq(OrderRefundDTO::getId, orderRefundDTO.getId())
                .eq(OrderRefundDTO::getStatus, "1");
        int i = orderRefundMapper.update(refundDTO, queryWrapper);

        //????????????????????????
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setRefundStatus("2");  //0 :?????????  1????????????  2???????????? 3 ????????????
        orderMapper.update(orderDTO, new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getOrderId, orderRefundDTO.getOrderId()));

        if(i == 0){
            throw new ServiceException("??????");
        }

    }

    /**
     * ????????????
     * @param refundId
     */
    @Override
    public void refundMoney(String refundId) {
        OrderRefundDTO orderRefundDTO = orderRefundMapper.selectById(refundId);
        if(orderRefundDTO == null){
            throw new ServiceException("?????????????????????");
        }
        if(!"3".equals(orderRefundDTO.getStatus())){
            throw new ServiceException("???????????????????????????");
        }
        OrderDTO orderDTO = orderMapper.selectOne(new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getOrderId, orderRefundDTO.getOrderId()));
        //1:????????????  2?????????????????? 3??????????????? 4:????????????
        if("1".equals(orderDTO.getPayType())){
            //????????????
        }else if("2".equals(orderDTO.getPayType())){
            //???????????????
        }else if("3".equals(orderDTO.getPayType())){
            //????????????
            customerService.addUserMoney(orderRefundDTO.getRefundMoney(), orderDTO.getUserId(), UserBillEnum.TYPE_REFUND);
        }else if("4".equals(orderDTO.getPayType())){
            //????????????
            customerService.addUserPoints(orderRefundDTO.getRefundMoney(), orderDTO.getUserId(), PointsBillEnum.TYPE_REFUND);
        }else{
            throw new ServiceException("?????????????????????");
        }
        orderDTO.setRefundMoney(orderRefundDTO.getRefundMoney());
        orderDTO.setStatus("7"); //?????????
        orderMapper.updateById(orderDTO);

        orderRefundDTO.setStatus("5"); // ?????????
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
            throw new ServiceException("?????????????????????");
        }
        OrderDTO orderDTO = orderMapper.selectOne(new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getOrderId, expressOrderRO.getOrderId()));
        if (orderDTO == null) {
            throw new ServiceException("???????????????");
        }
        if (!"1".equals(orderDTO.getStatus())) {
            throw new ServiceException("?????????????????????");
        }

        //redis ????????????
        redisUtil.set(SystemConstant.AUTO_RECEIVE_KEY + orderDTO.getOrderId(), SystemConstant.AUTO_RECEIVE_KEY
                + orderDTO.getOrderId(), Integer.parseInt(sysConfigService.getSysConfigValueFromRedis(SystemConstant.AUTO_RECEIVE_TIME)));
        return orderMapper.updateExpress(expressOrderRO);
    }

    @Override
    public int userReceiveGoods(String orderId) {
        OrderDTO orderDTO = orderMapper.selectOne(new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getOrderId, orderId));
        if (orderDTO == null) {
            throw new ServiceException("???????????????");
        }
        OrderDTO dto = new OrderDTO();
        dto.setId(orderDTO.getId());
        dto.setStatus("3");
        return orderMapper.updateById(dto);
    }
}
