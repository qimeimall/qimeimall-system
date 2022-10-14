package com.qimeixun.modules.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.entity.Customer;
import com.qimeixun.mapper.CustomerMapper;
import com.qimeixun.mapper.OrderMapper;
import com.qimeixun.mapper.ProductMapper;
import com.qimeixun.modules.customer.service.CustomerService;
import com.qimeixun.modules.data.service.DataService;
import com.qimeixun.po.OrderDTO;
import com.qimeixun.po.ProductDTO;
import com.qimeixun.util.DateUtil;
import com.qimeixun.vo.DataChartVO;
import com.qimeixun.vo.DataVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataServiceImpl implements DataService {

    @Resource
    CustomerMapper customerMapper;

    @Resource
    OrderMapper orderMapper;

    @Resource
    ProductMapper productMapper;

    @Override
    public DataVO selectData() {

        Date beginTime = DateUtil.getDayBeginTime(new Date());

        //总共会员数量
        DataVO dataVO = new DataVO();
        dataVO.setCustomerCount(customerMapper.selectCount(new QueryWrapper<>()));
        dataVO.setTodayCustomerCount(customerMapper.selectCount(new QueryWrapper<Customer>().lambda().gt(Customer::getCreateTime, beginTime)));

        dataVO.setAllOrderCount(orderMapper.selectCount(new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getStatus, "5")));
        dataVO.setTodayOrderCount(orderMapper.selectCount(new QueryWrapper<OrderDTO>().lambda().eq(OrderDTO::getStatus, "5").gt(OrderDTO::getCreateTime, beginTime)));


        dataVO.setOrderMoney(orderMapper.selectOrderMoney());
        dataVO.setTodayOrderMoney(orderMapper.selectTodayOrderMoney());

        dataVO.setAllProductCount(productMapper.selectCount(new QueryWrapper<ProductDTO>()));
        dataVO.setTodayProductCount(productMapper.selectCount(new QueryWrapper<ProductDTO>().lambda().gt(ProductDTO::getCreateTime,  beginTime)));

        return dataVO;
    }

    @Override
    public Map<String, List<DataChartVO>> selectChart() {

        Map<String, List<DataChartVO>> resultMap = new HashMap<>();

        //查询本月新增用户数量
        List<DataChartVO> customerData = orderMapper.selectCustomerCharts();

        //查询本月成交额
        List<DataChartVO> orderMoneyData = orderMapper.selectOrderMoneyCharts();

        //查询本月订单数量
        List<DataChartVO> orderCountData = orderMapper.selectOrderCountCharts();

        resultMap.put("customerData", customerData);
        resultMap.put("orderMoneyData", orderMoneyData);
        resultMap.put("orderCountData", orderCountData);

        return resultMap;
    }
}
