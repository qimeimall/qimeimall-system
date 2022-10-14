package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.po.CartOrderDTO;
import com.qimeixun.po.OrderDTO;
import com.qimeixun.ro.*;
import com.qimeixun.vo.DataChartVO;
import com.qimeixun.vo.OrderExpressVO;
import com.qimeixun.vo.OrderProductListVO;
import com.qimeixun.vo.UserOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/5/2520:48
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderDTO> {
    CartOrderDTO selectOrderInfoCreateBefore(List<String> list);

    void insertOrderCartRelation(List<Map<String, Object>> list);

    IPage<UserOrderVO> selectOrderList(Page page, @Param("params") OrderListRO orderListRO);

    int updateExpress(ExpressOrderRO expressOrderRO);

    int confirmReceiptGoods(String orderId);

    void completeOrder(String orderId);

    BigDecimal selectOrderMoney();

    BigDecimal selectTodayOrderMoney();

    List<DataChartVO> selectCustomerCharts();

    List<DataChartVO> selectOrderMoneyCharts();

    List<DataChartVO> selectOrderCountCharts();

    String selectOrderAttrValue(String cartId);

    List<OrderProductListVO> selectOrderProductCountList(String orderId);

    IPage<UserOrderVO> selectUserRefundOrderList(IPage<UserOrderVO> page, @Param("params") RefundOrderListRO refundOrderListRO);

    IPage<UserOrderVO> selectUserOrderList(IPage<UserOrderVO> page, @Param("params") UserOrderRO userOrderRO);

    UserOrderVO selectUserOrderById(Map<String, Object> argMap);

    OrderExpressVO selectOrderExpress(String orderId);

    IPage<UserOrderVO> selectRefundOrderList(IPage<UserOrderVO> page, @Param("params") RefundOrderSystemRO orderListRO);

//    IPage<UserOrderVO> selectRefundOrderList(Page page, @Param("params") OrderListRO orderListRO);
}
