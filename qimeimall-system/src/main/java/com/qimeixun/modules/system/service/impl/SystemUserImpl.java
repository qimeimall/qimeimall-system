package com.qimeixun.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qimeixun.entity.SystemUser;
import com.qimeixun.mapper.SystemUserMapper;
import com.qimeixun.ro.SystemUserLoginRO;
import com.qimeixun.modules.system.service.SystemUserService;
import com.qimeixun.vo.ResponseResultVO;
import org.springframework.beans.BeanUtils;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author chenshouyang
 * @date 2020/4/2613:55
 */
@Service
public class SystemUserImpl implements SystemUserService {

    @Resource
    private SystemUserMapper systemUserMapper;

    @Override
    public ResponseResultVO userLogin(SystemUserLoginRO systemUserLoginRO) {
        SystemUser systemUser = new SystemUser();
        BeanUtils.copyProperties(systemUserLoginRO,systemUser);
        QueryWrapper<SystemUser> queryWrapper = new QueryWrapper<>(systemUser);
        SystemUser user = systemUserMapper.selectOne(queryWrapper);
        if(user == null){
//            return ResponseResultVO.makeFailRsp("请重新登录");
        }else{
//            return ResponseResultVO.makeOKRsp(user);
        }
        return ResponseResultVO.successResult();
    }
}
