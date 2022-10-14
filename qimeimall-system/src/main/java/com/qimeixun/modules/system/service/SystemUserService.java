package com.qimeixun.modules.system.service;

import com.qimeixun.ro.SystemUserLoginRO;
import com.qimeixun.vo.ResponseResultVO;

/**
 * @author chenshouyang
 * @date 2020/4/2613:55
 */
public interface SystemUserService {
    ResponseResultVO userLogin(SystemUserLoginRO systemUserLoginRO);
}
