package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.po.CouponDTO;
import com.qimeixun.ro.CouponPageRO;
import com.qimeixun.vo.CouponListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/5/2923:00
 */
@Mapper
public interface CouponMapper extends BaseMapper<CouponDTO> {
    IPage<CouponListVO> selectCouponList(Page page, @Param("params") CouponPageRO couponPageRO);

    List<CouponListVO> selectUserNotHaveCouponList(Map<String,Object> argMap);
}
