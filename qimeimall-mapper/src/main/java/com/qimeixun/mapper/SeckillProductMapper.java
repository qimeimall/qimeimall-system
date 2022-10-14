package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.po.SeckillProductDTO;
import com.qimeixun.ro.SeckillProductRO;
import com.qimeixun.vo.SeckillProductListVO;
import com.qimeixun.vo.SeckillProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/6/2814:32
 */
@Mapper
public interface SeckillProductMapper extends BaseMapper<SeckillProductDTO> {
    IPage<SeckillProductVO> selectSeckillProductList(Page page, @Param("params") SeckillProductRO seckillProductRO);

    List<SeckillProductListVO> getSeckillProductList(Map<String, Object> paramMap);

    SeckillProductListVO getSeckillProduct(Map<String, Object> paramMap);
}
