package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qimeixun.po.UserCouponDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chenshouyang
 * @date 2020/5/3018:37
 */
@Mapper
public interface CouponUserMapper extends BaseMapper<UserCouponDTO> {
}
