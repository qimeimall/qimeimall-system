package com.qimeixun.modules.material.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qimeixun.entity.MaterialGroup;
import com.qimeixun.entity.MaterialImg;
import com.qimeixun.ro.MaterialImgRO;
import com.qimeixun.vo.ResponseResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/5/317:27
 */
public interface MaterialService {
    List<MaterialGroup> selectMaterialGroupList();

    int insertMaterialGroup(String name);

    int updateMaterialGroup(MaterialGroup materialGroup);

    int deleteMaterialGroup(Integer id);

    IPage<MaterialImg> selectMaterialImgList(MaterialImgRO materialImgRO);

    int deleteMaterialImg(Integer id);

    Map uploadImgFile(String name, MultipartFile file);

    int insertImgFile(MaterialImg materialImg);
}
