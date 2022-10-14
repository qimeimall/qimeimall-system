package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qimeixun.po.OrderCartDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderCartMapper extends BaseMapper<OrderCartDTO> {
}
