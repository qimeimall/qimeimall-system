package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qimeixun.po.MyServiceDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chenshouyang
 * @date 2020/7/411:08
 */
@Mapper
public interface MyServiceMapper extends BaseMapper<MyServiceDTO> {
}
