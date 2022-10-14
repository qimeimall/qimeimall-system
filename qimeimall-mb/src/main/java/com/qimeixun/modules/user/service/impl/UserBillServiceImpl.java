package com.qimeixun.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.mapper.UserBillMapper;
import com.qimeixun.modules.user.service.UserBillService;
import com.qimeixun.po.UserBillDTO;
import com.qimeixun.ro.UserBillRO;
import com.qimeixun.util.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class UserBillServiceImpl implements UserBillService {

    @Resource
    UserBillMapper userBillMapper;

    @Resource
    TokenUtil tokenUtil;

    @Override
    public List<UserBillDTO> getUserBillList(UserBillRO userBillRO) {
        //db type 0 用户购买  1 退款   2充值   3 系统充值
        QueryWrapper<UserBillDTO> queryWrapper = new QueryWrapper<>();
        if ("1".equals(userBillRO.getType())) {
            //消费
            queryWrapper.eq("type", "0").or().eq("type", "1");
        }
        if ("2".equals(userBillRO.getType())) {
            //充值
            queryWrapper.eq("type", "2").or().eq("type", "3");
        }
        queryWrapper.eq("user_id", tokenUtil.getUserIdByToken());
        queryWrapper.orderByDesc("create_time");
        List<UserBillDTO> userBillDTOS = userBillMapper.selectList(queryWrapper);
        for (UserBillDTO userBillDTO : userBillDTOS) {
            if ("0".equals(userBillDTO.getType())) {
                userBillDTO.setMoney(userBillDTO.getMoney().multiply(new BigDecimal(-1)));
            }
        }
        return userBillDTOS;
    }

    @Override
    public void insertUserBill(UserBillDTO userBillDTO) {
        userBillMapper.insert(userBillDTO);
    }
}
