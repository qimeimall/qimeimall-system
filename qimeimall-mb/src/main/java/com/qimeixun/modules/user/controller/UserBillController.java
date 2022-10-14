package com.qimeixun.modules.user.controller;

import com.qimeixun.modules.user.service.UserBillService;
import com.qimeixun.po.UserBillDTO;
import com.qimeixun.ro.UserBillRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "用户账单")
@RestController
@RequestMapping(value = "/bill", produces = "application/json;charset=UTF-8")
@CrossOrigin
public class UserBillController {

    @Resource
    UserBillService userBillService;

    @ApiOperation(value = "获取用户的账单列表", notes = "获取用户的账单列表")
    @RequestMapping(value = "/getUserBillList", method = RequestMethod.POST)
    public ResponseResultVO getUserBillList(@RequestBody UserBillRO userBillRO) {
        List<UserBillDTO> userBillDTOS = userBillService.getUserBillList(userBillRO);
        return ResponseResultVO.successResult(userBillDTOS);
    }
}
