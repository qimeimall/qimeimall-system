package com.qimeixun.vo;

import com.qimeixun.po.PointsRuleDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UserPointsVO {

    private BigDecimal points;

    private int index;

    private int todayIsSign;

    private int userSignCount; //用户签到次数

    private int signCount; //连续签到多少天

    private int totalUserSign; //多少个用户参与

    private List<PointsRuleDTO> ruleDTOS;
}
