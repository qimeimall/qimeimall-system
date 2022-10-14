package com.qimeixun.modules.market.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.mapper.PointsRuleMapper;
import com.qimeixun.modules.market.service.PointsService;
import com.qimeixun.po.PointsRuleDTO;
import com.qimeixun.ro.PointsRuleRO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PointsServiceImpl implements PointsService {

    @Resource
    PointsRuleMapper pointsRuleMapper;


    @Override
    public List<PointsRuleDTO> selectPointsRuleList() {
        QueryWrapper<PointsRuleDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PointsRuleDTO::getIsShow, "0").orderByAsc(PointsRuleDTO::getSortNo);
        return pointsRuleMapper.selectList(queryWrapper);
    }

    @Override
    public int insertPointsRule(PointsRuleRO pointsRuleRO) {
        PointsRuleDTO pointsRuleDTO = new PointsRuleDTO();
        BeanUtils.copyProperties(pointsRuleRO, pointsRuleDTO);
        return pointsRuleMapper.insert(pointsRuleDTO);
    }

    @Override
    public int updatePointsRule(PointsRuleRO pointsRuleRO) {
        PointsRuleDTO pointsRuleDTO = new PointsRuleDTO();
        BeanUtils.copyProperties(pointsRuleRO, pointsRuleDTO);
        return pointsRuleMapper.updateById(pointsRuleDTO);
    }

    @Override
    public int deletePointsRule(Long id) {
        return pointsRuleMapper.deleteById(id);
    }

    @Override
    public PointsRuleDTO selectPointsRulesById(Long id) {
        return pointsRuleMapper.selectById(id);
    }
}
