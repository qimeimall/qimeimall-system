package com.qimeixun.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.ro.PageRO;
import com.qimeixun.vo.ResponseResultVO;

public class BaseController<T> {

    public ResponseResultVO insertResult(int i) {
        if (i > 0) {
            return ResponseResultVO.successResult("新增成功");
        } else {
            return ResponseResultVO.failResult("新增失败");
        }
    }

    public ResponseResultVO updateResult(int i) {
        if (i > 0) {
            return ResponseResultVO.successResult("修改成功");
        } else {
            return ResponseResultVO.failResult("修改失败");
        }
    }

    public ResponseResultVO deleteResult(int i) {
        if (i > 0) {
            return ResponseResultVO.successResult("删除成功");
        } else {
            return ResponseResultVO.failResult("删除失败");
        }
    }

    public ResponseResultVO deleteResult(boolean i) {
        if (i) {
            return ResponseResultVO.successResult("删除成功");
        } else {
            return ResponseResultVO.failResult("删除失败");
        }
    }

    //分页返回结果类
    public ResponseResultVO getPageObject(IPage iPage, PageRO pageRO) {
        return ResponseResultVO.resultList(iPage.getRecords(), (int) iPage.getTotal(), pageRO);
    }

    public ResponseResultVO getPageObject(QueryWrapper queryWrapper, PageRO pageRO, BaseMapper baseMapper) {
        IPage<T> iPage = new Page<>(pageRO.getCurrentPage(), pageRO.getPageSize());
        IPage<T> page = baseMapper.selectPage(iPage, queryWrapper);
        return getPageObject(page, pageRO);
    }
}
