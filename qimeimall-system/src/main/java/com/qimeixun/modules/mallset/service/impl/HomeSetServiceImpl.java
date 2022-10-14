package com.qimeixun.modules.mallset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.constant.SystemConstant;
import com.qimeixun.mapper.HomeSetMapper;
import com.qimeixun.modules.mallset.service.HomeSetService;
import com.qimeixun.po.HomeSetDTO;
import com.qimeixun.ro.HomeSetRO;
import com.qimeixun.vo.ResponseResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenshouyang
 * @date 2020/5/1614:26
 */
@Service
public class HomeSetServiceImpl implements HomeSetService {

    @Resource
    HomeSetMapper homeSetMapper;

    @Override
    @Transactional
    public void updateHomeSet(List<HomeSetRO> homeSetROS) {
        QueryWrapper<HomeSetDTO> objectQueryWrapper = new QueryWrapper<>();
        homeSetMapper.delete(objectQueryWrapper);
        for (HomeSetRO homeSetRO : homeSetROS) {
            HomeSetDTO homeSetDTO = new HomeSetDTO();
            BeanUtils.copyProperties(homeSetRO, homeSetDTO);
            homeSetMapper.insert(homeSetDTO);
        }
    }

    @Override
    public List<HomeSetDTO> selectHomeSetList() {
        List<HomeSetDTO> list = homeSetMapper.selectList(new QueryWrapper<>());
        return list;
    }
}
