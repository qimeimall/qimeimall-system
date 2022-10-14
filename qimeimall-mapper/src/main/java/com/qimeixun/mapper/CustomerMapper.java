package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.entity.Customer;
import com.qimeixun.ro.UserPageRO;
import com.qimeixun.vo.BrokerageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/5/517:15
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
    Map<String, Object> selectMyInfo(String userId);

    BigDecimal selectUserConsumptionMoney(String userId);

    IPage<BrokerageVO> selectMyBrokerageList(Page page, @Param("params") UserPageRO pageRO);
}
