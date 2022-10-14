package com.qimeixun.modules.category.service;

import com.qimeixun.entity.Category;
import com.qimeixun.vo.ResponseResultVO;

import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/1312:40
 */
public interface CategoryService {
    List<Category> selectCustomerList(String storeId);

    int insertCategory(Category category);

    int updateCategory(Category category);

    Category selectCategoryById(String id);

    int deleteCategoryById(String id);

    List<Category> selectCategoryListNoPage(String storeId);

    List<Category> selectSelfCategoryList();
}
