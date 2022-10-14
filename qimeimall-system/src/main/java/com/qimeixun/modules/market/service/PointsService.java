package com.qimeixun.modules.market.service;

import com.qimeixun.po.PointsRuleDTO;
import com.qimeixun.ro.PointsRuleRO;

import java.util.List;

public interface PointsService {
    List<PointsRuleDTO> selectPointsRuleList();

    int insertPointsRule(PointsRuleRO pointsRuleRO);

    int updatePointsRule(PointsRuleRO pointsRuleRO);

    int deletePointsRule(Long id);

    PointsRuleDTO selectPointsRulesById(Long id);
}
