package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qimeixun.po.ShopCartDTO;
import com.qimeixun.vo.ShopCartListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/2419:04
 */
@Mapper
public interface ShopCartMapper extends BaseMapper<ShopCartDTO> {
    List<ShopCartListVO> selectUserCartList(String userId);

    void updateCartPayStatus(List<ShopCartListVO> list);

    int selectGoodsCartCount(String userId);
}
