package com.qimeixun.modules.user.service;

import com.qimeixun.po.UserBillDTO;
import com.qimeixun.ro.UserBillRO;

import java.util.List;

public interface UserBillService {
    List<UserBillDTO> getUserBillList(UserBillRO userBillRO);

    void insertUserBill(UserBillDTO userBillDTO);
}
