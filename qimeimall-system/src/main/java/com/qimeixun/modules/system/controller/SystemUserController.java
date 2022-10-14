package com.qimeixun.modules.system.controller;

import com.qimeixun.ro.SystemUserLoginRO;
import com.qimeixun.modules.system.service.SystemUserService;
import com.qimeixun.vo.ResponseResultVO;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenshouyang
 * @date 2020/4/2613:54
 */
@RestController
@RequestMapping("/systemUser")
public class SystemUserController {


    @Resource
    private SystemUserService systemUserService;

    @PostMapping("/login")
    public ResponseResultVO userLogin(@RequestBody SystemUserLoginRO systemUserLoginRO) {
        return systemUserService.userLogin(systemUserLoginRO);
    }
}
