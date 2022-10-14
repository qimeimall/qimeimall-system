package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.po.ProductDTO;
import com.qimeixun.ro.ProductListRO;
import com.qimeixun.vo.ProductListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author chenshouyang
 * @date 2020/5/1321:26
 */
@Mapper
public interface ProductMapper extends BaseMapper<ProductDTO> {
    /**
     * 查询产品列表
     * @param page
     * @param productListRO
     * @return
     */
    IPage<ProductListVO> selectProductList(Page page, @Param("params") ProductListRO productListRO);

    int updateStatusById(ProductDTO productDTO);
}
