package com.qimeixun.modules.material.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.common.QiNiuUtil;
import com.qimeixun.entity.MaterialGroup;
import com.qimeixun.entity.MaterialImg;
import com.qimeixun.mapper.MaterialImgMapper;
import com.qimeixun.mapper.MaterialMapper;
import com.qimeixun.modules.material.service.MaterialService;
import com.qimeixun.ro.MaterialImgRO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/5/317:27
 */
@Service
public class MaterialServiceImpl implements MaterialService {

    @Resource
    private MaterialMapper materialMapper;

    @Resource
    private MaterialImgMapper materialImgMapper;

    @Resource
    private QiNiuUtil qiNiuUtil;

    @Override
    public List<MaterialGroup> selectMaterialGroupList() {
        QueryWrapper<MaterialGroup> wrapper = new QueryWrapper<MaterialGroup>();
        wrapper.eq("is_delete", 0);
        wrapper.orderByAsc("id");
        List<MaterialGroup> list = materialMapper.selectList(wrapper);
        return list;
    }

    @Override
    public int insertMaterialGroup(String name) {
        MaterialGroup materialGroup = new MaterialGroup();
        materialGroup.setName(name);
        return materialMapper.insert(materialGroup);
    }

    @Override
    public int updateMaterialGroup(MaterialGroup materialGroup) {
        return materialMapper.updateById(materialGroup);
    }

    @Override
    public int deleteMaterialGroup(Integer id) {
        MaterialGroup materialGroup = new MaterialGroup();
        materialGroup.setId(id);
        materialGroup.setIsDelete(1);
        return materialMapper.updateById(materialGroup);
    }

    @Override
    public IPage<MaterialImg> selectMaterialImgList(MaterialImgRO materialImgRO) {
        QueryWrapper<MaterialImg> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        wrapper.orderByDesc("create_time");
        if (materialImgRO.getGroupId() != null && materialImgRO.getGroupId() != -1) {
            wrapper.eq("group_id", materialImgRO.getGroupId());
        }
        IPage<MaterialImg> materialImgPage = new Page<>(materialImgRO.getCurrentPage(), materialImgRO.getPageSize());
        IPage<MaterialImg> materialImgIPage = materialImgMapper.selectPage(materialImgPage, wrapper);
        return materialImgIPage;
    }

    @Override
    public int deleteMaterialImg(Integer id) {
        MaterialImg materialImg = new MaterialImg();
        materialImg.setId(id);
        materialImg.setIsDelete(1);
        return materialImgMapper.updateById(materialImg);
    }

    @Override
    public Map uploadImgFile(String name, MultipartFile file) {
        return qiNiuUtil.uploadImgFile(name, file);
    }

    @Override
    public int insertImgFile(MaterialImg materialImg) {
        return materialImgMapper.insert(materialImg);
    }
}
