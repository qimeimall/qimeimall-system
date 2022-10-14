package com.qimeixun.modules.cart.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.ProductAttrDetailMapper;
import com.qimeixun.mapper.ProductMapper;
import com.qimeixun.mapper.SeckillProductMapper;
import com.qimeixun.mapper.ShopCartMapper;
import com.qimeixun.modules.cart.service.ShopCartService;
import com.qimeixun.po.ProductAttrDetailDTO;
import com.qimeixun.po.ProductDTO;
import com.qimeixun.po.SeckillProductDTO;
import com.qimeixun.po.ShopCartDTO;
import com.qimeixun.ro.ProductAttrRO;
import com.qimeixun.ro.ShopCartGoodsRO;
import com.qimeixun.util.TokenUtil;
import com.qimeixun.vo.ResponseResultVO;
import com.qimeixun.vo.ShopCartListVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/5/2413:34
 */
@Service
public class ShopCartServiceImpl implements ShopCartService {

    @Resource
    TokenUtil tokenUtil;

    @Resource
    ProductAttrDetailMapper productAttrDetailMapper;

    @Resource
    ShopCartMapper shopCartMapper;

    @Resource
    ProductMapper productMapper;

    @Resource
    SeckillProductMapper seckillProductMapper;

    /**
     * 查询用户的
     *
     * @return
     */
    @Override
    public List<ShopCartListVO> selectShopCartList() {
        List<ShopCartListVO> shopCartListVOS = shopCartMapper.selectUserCartList(tokenUtil.getUserIdByToken());
        return shopCartListVOS;
    }

    @Override
    public int insertShopCart(ShopCartGoodsRO shopCartGoodsRO) {
        ShopCartDTO shopCartDTO = new ShopCartDTO();
        shopCartDTO.setProductId(shopCartGoodsRO.getProduct().getId());
        shopCartDTO.setProductAttrId(getProductAttr(shopCartGoodsRO.getProduct().getId(), shopCartGoodsRO.getProductAttr().getCurNav()));
        shopCartDTO.setUserId(Integer.valueOf(tokenUtil.getUserIdByToken()));
        shopCartDTO.setNum(shopCartGoodsRO.getProductAttr().getSelectProductCount());
        if (StringUtils.isNotEmpty(shopCartGoodsRO.getSeckillProductId())) {
            SeckillProductDTO seckillProductDTO = seckillProductMapper.selectById(shopCartGoodsRO.getSeckillProductId());
            shopCartDTO.setSeckillProductId(Long.valueOf(shopCartGoodsRO.getSeckillProductId()));
            shopCartDTO.setProductId(seckillProductDTO.getProductId());
            if (seckillProductDTO.getStock() < shopCartGoodsRO.getProductAttr().getSelectProductCount()) {
                throw new ServiceException("库存不足");
            }
        } else {
            ProductDTO productDTO = productMapper.selectById(shopCartGoodsRO.getProduct().getId());
            if (ObjectUtil.isNull(productDTO)) {
                throw new ServiceException("添加失败");
            }
            if (productDTO.getStockCount() < shopCartGoodsRO.getProductAttr().getSelectProductCount()) {
                throw new ServiceException("库存不足");
            }
        }
        //查询购物车是否存在该产品
        ShopCartDTO dto = shopCartMapper.selectOne(new QueryWrapper<ShopCartDTO>().eq("product_id", shopCartGoodsRO.getProduct().getId())
                .eq("product_attr_id", getProductAttr(shopCartGoodsRO.getProduct().getId(), shopCartGoodsRO.getProductAttr().getCurNav())).eq("is_delete", "0")
                .eq("is_pay", 0).eq("user_id", shopCartDTO.getUserId())
                .last("limit 1"));
        if(dto == null){
            return shopCartMapper.insert(shopCartDTO);
        }else{
            dto.setNum(dto.getNum() + shopCartGoodsRO.getProductAttr().getSelectProductCount());
            return shopCartMapper.updateById(dto);
        }

    }

    @Override
    public void deleteShopCartProduct(String id) {
        ShopCartDTO shopCartDTO = shopCartMapper.selectById(id);
        shopCartDTO.setIsDelete(1);
        shopCartMapper.updateById(shopCartDTO);
    }

    @Override
    public void addShopCartProductNum(String id) {
        ShopCartDTO shopCartDTO = shopCartMapper.selectById(id);
        int cartNum = shopCartDTO.getNum() + 1;
        //查询该产品的库存
        updateCartProductNum(shopCartDTO, cartNum);
    }

    @Override
    public void subShopCartProductNum(String id) {
        ShopCartDTO shopCartDTO = shopCartMapper.selectById(id);
        int cartNum = shopCartDTO.getNum() - 1;
        if (cartNum <= 0) {
            throw new ServiceException("不能为0");
        }
        //查询该产品的库存
        updateCartProductNum(shopCartDTO, cartNum);
    }

    @Override
    public Map<String, Object> selectGoodsCartCount() {
        int count = shopCartMapper.selectGoodsCartCount(tokenUtil.getUserIdByToken());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("cartGoodsCount", count);
        return resultMap;
    }

    private void updateCartProductNum(ShopCartDTO shopCartDTO, int cartNum) {
        if (checkProductStock(shopCartDTO, cartNum)) {
            throw new ServiceException("库存不足");
        }
        shopCartDTO.setNum(cartNum);
        shopCartMapper.updateById(shopCartDTO);
    }

    private boolean checkProductStock(ShopCartDTO shopCartDTO, int cartNum) {
        if (shopCartDTO.getProductAttrId() == 0) {
            ProductDTO productDTO = productMapper.selectById(shopCartDTO.getProductId());
            if (cartNum > productDTO.getStockCount()) {
                return true;
            }
        } else {
            ProductAttrDetailDTO productAttrDetailDTO = productAttrDetailMapper.selectById(shopCartDTO.getProductAttrId());
            if (cartNum > productAttrDetailDTO.getStockCount()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查询商品属性id
     *
     * @param
     * @return
     */
    public Long getProductAttr(Long productId, List<String> curNav) {
        //ProductAttrRO productAttr = shopCartGoodsRO.getProductAttr();
        //<String> curNav = productAttr.getCurNav();
        if (curNav != null && curNav.size() > 0) {
            String productAttrValue = ArrayUtil.join(curNav.toArray(), ",") + ",";
            QueryWrapper<ProductAttrDetailDTO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("product_id", productId);
            queryWrapper.eq("attr_value", productAttrValue);
            ProductAttrDetailDTO dto = productAttrDetailMapper.selectOne(queryWrapper);
            return dto == null ? 0 : dto.getId();
        }
        return 0L;
    }
}
