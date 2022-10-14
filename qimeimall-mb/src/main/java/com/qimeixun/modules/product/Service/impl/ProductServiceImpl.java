package com.qimeixun.modules.product.Service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.entity.Specs;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.*;
import com.qimeixun.modules.product.Service.ProductService;
import com.qimeixun.po.*;
import com.qimeixun.ro.ProductDetailRO;
import com.qimeixun.util.MD5Util;
import com.qimeixun.util.TokenUtil;
import com.qimeixun.vo.ProductVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenshouyang
 * @date 2020/5/2311:33
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    ProductMapper productMapper;

    @Resource
    ProductAttrMapper productAttrMapper;

    @Resource
    ProductAttrDetailMapper productAttrDetailMapper;

    @Resource
    SeckillProductMapper seckillProductMapper;

    @Resource
    UserCollectionMapper userCollectionMapper;

    @Resource
    TokenUtil tokenUtil;

    @Override
    public ProductVO selectProductInfoById(ProductDetailRO productDetailRO) {
        if (StringUtils.isEmpty(productDetailRO.getProductId()) && StringUtils.isEmpty(productDetailRO.getSeckillProductId())) {
            throw new ServiceException("参数错误");
        }
        SeckillProductDTO seckillProductDTO = null;
        if (StringUtils.isNotEmpty(productDetailRO.getSeckillProductId())) {
            //秒杀id
            seckillProductDTO = seckillProductMapper.selectById(productDetailRO.getSeckillProductId());
            if (ObjectUtil.isNull(seckillProductDTO)) {
                throw new ServiceException("获取产品失败");
            }
            productDetailRO.setProductId(String.valueOf(seckillProductDTO.getProductId()));
        }
        ProductDTO productDTO = productMapper.selectById(productDetailRO.getProductId());
        if (StringUtils.isNotEmpty(productDetailRO.getSeckillProductId())) {
            productDTO.setPrice(seckillProductDTO.getSeckillPrice());
            productDTO.setStockCount(seckillProductDTO.getStock());
            productDTO.setSalesCount(seckillProductDTO.getSalesStock());
        }
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(productDTO, productVO);
        if (StringUtils.isNotEmpty(productDTO.getProductBannerImg())) {
            productVO.setBanners(Arrays.asList(productDTO.getProductBannerImg().split(",")));
        }

        QueryWrapper<ProductAttr> queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", productDetailRO.getProductId());
        queryWrapper.orderByAsc("id");
        List<Specs> specs = new ArrayList<>();
        List<ProductAttr> productAttrs = productAttrMapper.selectList(queryWrapper);
        Set<String> groups = new HashSet<>();
        for (ProductAttr p : productAttrs) {
            groups.add(p.getGroupName());
        }
        for (String groupName : groups) {
            Specs sp = new Specs();
            sp.setGroupName(groupName);
            List<String> values = new ArrayList<>();
            for (ProductAttr p : productAttrs) {
                if (groupName.equals(p.getGroupName())) {
                    values.add(p.getAttrValue());
                }
            }
            sp.setSpecsValues(values);
            specs.add(sp);
        }
        if (specs.size() > 0) {
            productVO.setSpecs(specs);
        }

        QueryWrapper<ProductAttrDetailDTO> productAttrDetailDTOQueryWrapper = new QueryWrapper();
        productAttrDetailDTOQueryWrapper.eq("product_id", productDetailRO.getProductId());
        List<ProductAttrDetailDTO> productAttrDetailDTOS = productAttrDetailMapper.selectList(productAttrDetailDTOQueryWrapper);
        if (productAttrDetailDTOS != null) {
            productVO.setAttrDetails(productAttrDetailDTOS);
        }

        //查询该商品是否被用户收藏
        Integer count = userCollectionMapper.selectCount(new QueryWrapper<UserCollectionDTO>().eq("product_id", productDetailRO.getProductId()).eq("user_id", tokenUtil.getUserIdByToken()));
        if(count > 0){
            productVO.setIsCollection("1");
        }
        return productVO;
    }

    @Override
    public List<ProductDTO> selectProductHot(String storeId) {
        QueryWrapper<ProductDTO> productDTOQueryWrapper = new QueryWrapper<>();
        productDTOQueryWrapper.eq("is_delete", "0");
        productDTOQueryWrapper.eq("status", "0");
        productDTOQueryWrapper.eq("is_hot", "1");
        productDTOQueryWrapper.eq("store_id", storeId);
        productDTOQueryWrapper.orderByAsc("sort");
        return productMapper.selectList(productDTOQueryWrapper);
    }

    @Override
    public List<ProductDTO> selectProductRecommend(String storeId) {
        QueryWrapper<ProductDTO> productDTOQueryWrapper = new QueryWrapper<>();
        productDTOQueryWrapper.eq("is_delete", "0");
        productDTOQueryWrapper.eq("status", "0");
        productDTOQueryWrapper.eq("is_recommend", "1");
        productDTOQueryWrapper.eq("store_id", storeId);
        productDTOQueryWrapper.orderByAsc("sort");
        return productMapper.selectList(productDTOQueryWrapper);
    }

    @Override
    public int collectionProduct(String productId) {
        checkProduct(productId);
        UserCollectionDTO userCollection = userCollectionMapper.selectOne(new QueryWrapper<UserCollectionDTO>().
                eq("product_id", productId).eq("user_id", tokenUtil.getUserIdByToken()));
        if(userCollection == null){
            //新增
            UserCollectionDTO userCollectionDTO = new UserCollectionDTO();
            userCollectionDTO.setProductId(Long.valueOf(productId));
            userCollectionDTO.setUserId(tokenUtil.getUserIdByToken());
            userCollectionDTO.setUnque(MD5Util.md5(productId + tokenUtil.getUserIdByToken(), ""));
            return userCollectionMapper.insert(userCollectionDTO);
        }else{
            //
            return userCollectionMapper.delete(new QueryWrapper<UserCollectionDTO>().eq("product_id", productId).eq("user_id", tokenUtil.getUserIdByToken()));
        }
    }

    @Override
    public List<ProductDTO> selectProductCollection() {
        List<UserCollectionDTO> collectionDTOS = userCollectionMapper.selectList(new QueryWrapper<UserCollectionDTO>().eq("user_id", tokenUtil.getUserIdByToken()));
        List<Long> productIds = collectionDTOS.stream().map(product -> product.getProductId()).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(productIds)){
            return new ArrayList<>();
        }
        List<ProductDTO> productDTOS = productMapper.selectList(new QueryWrapper<ProductDTO>().in("id", productIds).eq("is_delete", 0));
        return productDTOS;
    }

    private void checkProduct(String productId) {
        if (StrUtil.isBlank(productId)) {
            throw new ServiceException("产品不能为空");
        }
        ProductDTO productDTO = productMapper.selectById(productId);
        if (productDTO == null) {
            throw new ServiceException("产品不存在");
        }
        if ("1".equals(productDTO.getIsDelete())) {
            throw new ServiceException("产品已下架");
        }
    }
}
