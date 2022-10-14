package com.qimeixun.modules.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.entity.Specs;
import com.qimeixun.mapper.ProductAttrDetailMapper;
import com.qimeixun.mapper.ProductAttrMapper;
import com.qimeixun.mapper.ProductMapper;
import com.qimeixun.modules.product.service.ProductService;
import com.qimeixun.po.ProductAttr;
import com.qimeixun.po.ProductAttrDetailDTO;
import com.qimeixun.po.ProductDTO;
import com.qimeixun.ro.ProductListRO;
import com.qimeixun.ro.ProductRO;
import com.qimeixun.modules.system.service.impl.CommonService;
import com.qimeixun.util.TokenUtil;
import com.qimeixun.vo.ProductListVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author chenshouyang
 * @date 2020/5/1320:15
 */
@Service
public class ProductServiceImpl extends CommonService implements ProductService {

    @Resource
    TokenUtil tokenUtil;

    @Resource
    ProductMapper productMapper;

    @Resource
    ProductAttrMapper productAttrMapper;

    @Resource
    ProductAttrDetailMapper productAttrDetailMapper;

    @Override
    public IPage<ProductListVO> selectProductList(ProductListRO productListRO) {
        Page page = new Page<ProductListVO>(productListRO.getCurrentPage(), productListRO.getPageSize());
        IPage<ProductListVO> list = productMapper.selectProductList(page, productListRO);
        return list;
    }

    @Override
    @Transactional
    public int insertProduct(ProductRO productRO) {
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(productRO, productDTO);
        productDTO.setStoreId(tokenUtil.getSysUserInfo().getStoreId());
        //插入产品基础信息
        int i = productMapper.insert(productDTO);
        //插入产品规格信息
        insertProductAttr(productDTO);
        //插入产品规格价格详细信息
        insertProductAttrDetail(productDTO);
        return i;
    }

    @Override
    public ProductDTO selectProductById(String id) {
        ProductDTO productDTO = productMapper.selectById(id);

        QueryWrapper<ProductAttr> queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", id);
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
            productDTO.setSpecs(specs);
        }

        QueryWrapper<ProductAttrDetailDTO> queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("product_id", productDTO.getId());
        List<ProductAttrDetailDTO> productAttrDetailDTOS = productAttrDetailMapper.selectList(queryWrapper1);
        List<Map<String, String>> tableData = new ArrayList<>();
        for (ProductAttrDetailDTO pad : productAttrDetailDTOS) {
            Map<String, String> map = JSON.parseObject(pad.getAttrContent(), Map.class);
            tableData.add(map);
        }
        if (tableData.size() > 0) {
            productDTO.setTableData(tableData);
        }
        return productDTO;
    }

    /**
     * 修改产品
     *
     * @param productRO
     * @return
     */
    @Override
    @Transactional
    public int updateProduct(ProductRO productRO) {

        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(productRO, productDTO);
        //更新基本信息
        int i = productMapper.updateById(productDTO);

        //更新规格信息
        QueryWrapper<ProductAttr> qw = new QueryWrapper<>();
        qw.eq("product_id", productDTO.getId());
        productAttrMapper.delete(qw);
        //插入产品规格信息
        insertProductAttr(productDTO);

        //更新规格价格信息
        QueryWrapper<ProductAttrDetailDTO> pad = new QueryWrapper<>();
        pad.eq("product_id", productDTO.getId());
        productAttrDetailMapper.delete(pad);
        //插入产品规格价格详细信息
        insertProductAttrDetail(productDTO);
        return i;
    }

    @Override
    public int deleteProduct(Long id) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        productDTO.setIsDelete("1");
        int i = productMapper.updateById(productDTO);
        return i;
    }

    /**
     * 上下架
     *
     * @param id
     * @return
     */
    @Override
    public int updateProductStatus(Long id) {
        ProductDTO productDTO = new ProductDTO();
        ProductDTO dto = productMapper.selectById(id);
        if (dto != null) {
            if ("0".equals(dto.getStatus())) {
                productDTO.setStatus("1");
            }
            if ("1".equals(dto.getStatus())) {
                productDTO.setStatus("0");
            }
        }
        productDTO.setId(id);
        int i = productMapper.updateStatusById(productDTO);
        return i;
    }

    @Override
    public List<ProductDTO> selectSelfProductList() {
        QueryWrapper<ProductDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        queryWrapper.eq("is_delete", 0);
        queryWrapper.eq("store_id", tokenUtil.getSysUserInfo().getStoreId());
        return productMapper.selectList(queryWrapper);
    }

    private void insertProductAttr(ProductDTO productDTO) {
        List<Specs> specss = productDTO.getSpecs();
        if (specss == null) {
            return;
        }
        for (Specs specs : specss) {
            for (String str : specs.getSpecsValues()) {
                ProductAttr productAttr = new ProductAttr();
                productAttr.setGroupName(specs.getGroupName());
                productAttr.setAttrValue(str);
                productAttr.setProductId(productDTO.getId());
                productAttrMapper.insert(productAttr);
            }
        }
    }

    private void insertProductAttrDetail(ProductDTO productDTO) {
        List<Map<String, String>> tableData = productDTO.getTableData();
        List<Specs> specss = productDTO.getSpecs();
        ProductAttrDetailDTO productAttrDetailDTO = new ProductAttrDetailDTO();
        if (tableData == null) {
            return;
        }
        for (Map<String, String> map : tableData) {
            String attrValue = "";
            for (Specs specs : specss) {
                String value = String.valueOf(map.get(specs.getGroupName()));
                attrValue += value + ",";
            }
            productAttrDetailDTO.setProductId(productDTO.getId());
            productAttrDetailDTO.setAttrValue(attrValue);
            productAttrDetailDTO.setPrice(BigDecimal.valueOf(Double.valueOf(map.get("价格"))));
            productAttrDetailDTO.setStockCount(Integer.parseInt(map.get("库存")));
            productAttrDetailDTO.setProductCode(String.valueOf(map.get("货号")));

            productAttrDetailDTO.setAttrContent(JSON.toJSONString(map));
            productAttrDetailMapper.insert(productAttrDetailDTO);
        }
    }
}
