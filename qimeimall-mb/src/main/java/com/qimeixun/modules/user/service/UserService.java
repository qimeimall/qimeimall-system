package com.qimeixun.modules.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.entity.Customer;
import com.qimeixun.po.MyServiceDTO;
import com.qimeixun.po.ReplyDTO;
import com.qimeixun.po.WithdrawalDTO;
import com.qimeixun.ro.*;
import com.qimeixun.vo.BrokerageVO;
import com.qimeixun.vo.ResponseResultVO;
import com.qimeixun.vo.UserPointsVO;

import java.util.List;
import java.util.Map;

/**
 * @author chenshouyang
 * @date 2020/5/2218:03
 */
public interface UserService {
    Map getUserInfo(UserLoginRO userLoginRO);

    Map selectMyInfo();

    List<MyServiceDTO> getMyServiceList();

    void sendCode(String phone);

    Map login(PhoneLoginRO phoneLoginRO);

    Map selectUserBalance();

    Customer getCustomerById(String userId);

    ResponseResultVO selectMySpreadList(MySpreadRO mySpreadRO);

    IPage<BrokerageVO> selectMyBrokerageList(Page page, UserPageRO pageRO);

    void insertWithdrawal(WithdrawalDTO dto);

    IPage<WithdrawalDTO> selectMyWithdrawalList(Page page, UserPageRO userPageRO);

    UserPointsVO selectUserSignList();

    void addSignRecord();

    IPage selectUserPointsList(PageRO pageRO);
}
