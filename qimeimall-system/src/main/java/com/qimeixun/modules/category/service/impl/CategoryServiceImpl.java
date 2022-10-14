package com.qimeixun.modules.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.entity.Category;
import com.qimeixun.entity.SysUser;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.CategoryMapper;
import com.qimeixun.mapper.ProductMapper;
import com.qimeixun.modules.category.service.CategoryService;
import com.qimeixun.po.ProductDTO;
import com.qimeixun.util.TokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author chenshouyang
 * @date 2020/5/1312:40
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    TokenUtil tokenUtil;

    @Resource
    ProductMapper productMapper;

    @Override
    public List<Category> selectCustomerList(String storeId) {
        SysUser sysUserInfo = tokenUtil.getSysUserInfo();
        if (!"0".equals(sysUserInfo.getStoreId())) {
            storeId = String.valueOf(sysUserInfo.getStoreId());
        }
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        queryWrapper.eq("store_id", storeId);
        queryWrapper.orderByAsc("sort");
        List<Category> categoryList = categoryMapper.selectList(queryWrapper);
        List<Category> categories = listToTreeList(categoryList);
        return categories;
    }

    @Override
    public int insertCategory(Category category) {
        SysUser sysUserInfo = tokenUtil.getSysUserInfo();
        category.setStoreId(sysUserInfo.getStoreId());
        return categoryMapper.insert(category);
    }

    @Override
    public int updateCategory(Category category) {
        return categoryMapper.updateById(category);
    }

    @Override
    public Category selectCategoryById(String id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public int deleteCategoryById(String id) {
        //查询目录下是否有自己分类，否则不让删除
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        queryWrapper.eq("pid", id);
        Integer count = categoryMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ServiceException("分类下有子级分类，不能删除");
        }
        //判断分类下是否有产品
        QueryWrapper<ProductDTO> productDTOQueryWrapper = new QueryWrapper();
        productDTOQueryWrapper.eq("is_delete", 0);
        Integer count1 = productMapper.selectCount(productDTOQueryWrapper);
        if (count1 > 0) {
            throw new ServiceException("分类下有子级分类，不能删除");
        }

        return categoryMapper.deleteById(id);
    }

    @Override
    public List<Category> selectCategoryListNoPage(String storeId) {
        SysUser sysUserInfo = tokenUtil.getSysUserInfo();
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        if (!"0".equals(sysUserInfo.getStoreId())) {
            //如果不是平台管理员 只能查看自己的产品分类
            queryWrapper.eq("store_id", sysUserInfo.getStoreId());
        }
        queryWrapper.orderByAsc("id");
        return categoryMapper.selectList(queryWrapper);
    }

    @Override
    public List<Category> selectSelfCategoryList() {
        SysUser sysUserInfo = tokenUtil.getSysUserInfo();
        QueryWrapper<Category> queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("sort");
        queryWrapper.eq("store_id", sysUserInfo.getStoreId());
        return categoryMapper.selectList(queryWrapper);
    }

    /**
     * 将list转换成树形结构集合
     *
     * @param list 需要转化的数据
     * @return 转换后的树形结构集合
     */
    private List<Category> listToTreeList(List<Category> list) {
        List<Category> treeList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return treeList;
        }
        /*
         * 先对集合排序
         * 排序后，最终的树形集合、各节点下的子节点集合，都会与初始集合排序规则一致
         */
        Comparator<Category> ageComparator = (o1, o2) -> o1.getSort().compareTo(o2.getSort());
        // 按配置的顺序取值
        list.sort(ageComparator);

        Map<String, Category> map = new HashMap<>();
        // 将菜单集合数据存放到map集合中，key为菜单id，后续判断父节点用
        for (Category category : list) {
            map.put(category.getId() + "", category);
            // 将一级节点添加到树形集合中
            if (category.getPid() == null || category.getPid().intValue() == 0) {
                treeList.add(category);
            }
        }
        // 遍历结果集
        for (Category category : list) {
            if (category.getPid() == null || category.getPid().intValue() == 0) {
                // 跳过一级节点
                continue;
            }
            // 判断父节点是否存在
            Category parentMenu = map.get(String.valueOf(category.getPid()));
            if (parentMenu != null) {
                // 父节点存在，则将该节点添加到父节点的子节点集合中
                parentMenu.getChildren().add(category);
            }
        }
        return treeList;
    }
}
