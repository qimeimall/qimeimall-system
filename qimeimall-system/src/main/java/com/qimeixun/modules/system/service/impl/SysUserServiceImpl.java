package com.qimeixun.modules.system.service.impl;

import com.qimeixun.constant.SystemConstant;
import com.qimeixun.entity.SysUser;
import com.qimeixun.mapper.SysRoleMapper;
import com.qimeixun.mapper.SysUserMapper;
import com.qimeixun.ro.SysUserRO;
import com.qimeixun.ro.SysUserRoleRO;
import com.qimeixun.modules.system.service.SysLoginService;
import com.qimeixun.modules.system.service.SysUserService;
import com.qimeixun.util.MD5Util;
import com.qimeixun.util.Tool;
import com.qimeixun.vo.ResponseResultVO;
import com.qimeixun.vo.SysRoleVO;
import com.qimeixun.vo.SysUserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员用户接口实现
 *
 * @author wangdaqiang
 * @date 2019-08-23 09:12
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Value("${admin.password.md5.key}")
    private String sysUserMd5Key;

    @Resource
    private SysUserMapper sysUserMapper;
    /*@Resource
    private SysOperateLogService sysOperateLogService;*/
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysLoginService sysLoginService;


    @Override
    @Transactional
    public ResponseResultVO insert(SysUserRO sysUserRO) {
        try {
            if (StringUtils.isBlank(sysUserRO.getLoginName())) {
                return ResponseResultVO.failResult("登录名不能为空");
            } else if (!sysUserRO.getLoginName().matches(SystemConstant.LOGIN_NAME_REGEX)) {
                return ResponseResultVO.failResult("登录名必需以字母、数字开头的5-20位字符，只能包含字母、数字、下划线'_'");
            }
            if (StringUtils.isBlank(sysUserRO.getPassword())) {
                return ResponseResultVO.failResult("密码不能为空");
            } else if (sysUserRO.getPassword().length() < 6 || sysUserRO.getPassword().length() > 16) {
                return ResponseResultVO.failResult("密码长度不能少于6个字符，或大于16个字符");
            }
            if (StringUtils.isBlank(sysUserRO.getMobile())) {
                return ResponseResultVO.failResult("手机号不能为空");
            }
            SysUser loginNameUser = this.sysUserMapper.selectByLoginName(sysUserRO.getLoginName(), null);
            if (loginNameUser != null) {
                return ResponseResultVO.failResult("登录名已被使用");
            }
            SysUser mobileUser = this.sysUserMapper.selectByMobile(sysUserRO.getMobile(), null);
            if (mobileUser != null) {
                return ResponseResultVO.failResult("手机号已被使用");
            }
            SysUser sysUser = new SysUser();
            sysUser.setId(Tool.getPrimaryKey());
            sysUser.setLoginName(sysUserRO.getLoginName());
            sysUser.setPassword(MD5Util.md5(sysUserRO.getPassword(), this.sysUserMd5Key));
            if (StringUtils.isBlank(sysUserRO.getUserName())) {
                sysUser.setUserName(sysUserRO.getLoginName());
            } else {
                sysUser.setUserName(sysUserRO.getUserName());
            }
            sysUser.setMobile(sysUserRO.getMobile());
            if (sysUserRO.getUserType() != null) {
                sysUser.setUserType(sysUserRO.getUserType());
            } else {
                sysUser.setUserType(0);
            }
            sysUser.setIsSuper(0);
            sysUser.setStatus(0);
            sysUser.setIsOnline(0);
            if (StringUtils.isNotBlank(sysUserRO.getCardNumber())) {
                sysUser.setCardNumber(sysUserRO.getCardNumber());
            }
            if (sysUserRO.getStoreId() != null) {
                sysUser.setStoreId(sysUser.getStoreId());
            }
            this.sysUserMapper.insert(sysUser);

            // 关联角色数据
            if (!CollectionUtils.isEmpty(sysUserRO.getRoleIds())) {
                this.sysUserMapper.addSysUserRoles(sysUser.getId(), sysUserRO.getRoleIds());
            }

            // this.sysOperateLogService.insertAddLog(SysModuleType.SYS_USER, sysUser.getId(), "新增-管理员用户");

            return ResponseResultVO.successResult();
        } catch (Exception e) {
            logger.error("新增-管理员用户-异常", e);
            return ResponseResultVO.errorResult();
        }
    }

    @Override
    public ResponseResultVO delete(String id) {
        try {
            //查询改最后一次登录的token
            SysUser sysUser = sysUserMapper.selectById(id);
            int num = this.sysUserMapper.delete(id);
            if (num <= 0) {
                return ResponseResultVO.failResult();
            }
            // 记录操作日志
            //this.sysOperateLogService.insertDeleteLog(SysModuleType.SYS_USER, id, "删除-管理员用户");
            if (StringUtils.isNotEmpty(sysUser.getRememberToken())) {
                // 将用户踢出登录
                this.sysLoginService.loginOutByToken(sysUser.getRememberToken());
            }
            return ResponseResultVO.successResult();
        } catch (Exception e) {
            logger.error("删除-管理员用户-异常", e);
            return ResponseResultVO.errorResult();
        }
    }

    @Override
    public ResponseResultVO update(SysUserRO sysUserRO) {
        try {
            if (StringUtils.isBlank(sysUserRO.getId())) {
                return ResponseResultVO.failResult("用户id不能为空");
            }
            SysUser dbSysUser = this.sysUserMapper.selectById(sysUserRO.getId());
            if (dbSysUser == null) {
                return ResponseResultVO.failResult("用户不存在");
            }
            SysUser sysUser = new SysUser();
            sysUser.setId(sysUserRO.getId());
            if (StringUtils.isNotBlank(sysUserRO.getPassword())) {
                if (sysUserRO.getPassword().length() < 6 || sysUserRO.getPassword().length() > 16) {
                    return ResponseResultVO.failResult("密码长度不能少于6个字符，或大于16个字符");
                }
                sysUser.setPassword(MD5Util.md5(sysUserRO.getPassword(), this.sysUserMd5Key));
                //修改密码之后 剔出该用户
                this.sysLoginService.loginOutByToken(dbSysUser.getRememberToken());
            }
            if (StringUtils.isNotBlank(sysUserRO.getUserName())) {
                sysUser.setUserName(sysUserRO.getUserName());
            }
            if (sysUserRO.getUserType() != null) {
                sysUser.setUserType(sysUserRO.getUserType());
            }
            if (StringUtils.isNotBlank(sysUserRO.getCardNumber())) {
                sysUser.setCardNumber(sysUserRO.getCardNumber());
            }
            if (StringUtils.isNotBlank(sysUserRO.getMobile())) {
                SysUser mobileUser = this.sysUserMapper.selectByMobile(sysUserRO.getMobile(), sysUserRO.getId());
                if (mobileUser != null) {
                    return ResponseResultVO.failResult("手机号已被使用");
                }
                sysUser.setMobile(sysUserRO.getMobile());
            }
            if (StringUtils.isNotBlank(sysUserRO.getPassword())) {
                sysUser.setPassword(MD5Util.md5(sysUserRO.getPassword(), this.sysUserMd5Key));
            }
            if (sysUserRO.getStoreId() != null) {
                sysUser.setStoreId(sysUserRO.getStoreId());
            }
            int num = this.sysUserMapper.update(sysUser);
            if (num > 0) {
                //this.sysOperateLogService.insertUpdateLog(SysModuleType.SYS_USER, sysUser.getId(), "修改-管理员用户", dbSysUser, sysUser);

                return ResponseResultVO.successResult();
            }
            return ResponseResultVO.failResult();
        } catch (Exception e) {
            logger.error("修改-管理员用户-异常", e);
            return ResponseResultVO.errorResult();
        }
    }

    @Override
    public ResponseResultVO selectById(String id) {
        try {
            SysUser sysUser = this.sysUserMapper.selectById(id);
            // 查询用户角色
            List<SysRoleVO> roleList = this.sysRoleMapper.selectByUserId(id);
            if (!CollectionUtils.isEmpty(roleList)) {
                sysUser.setRoleList(roleList);
            }
            return ResponseResultVO.successResult(sysUser);
        } catch (Exception e) {
            logger.error("查询-管理员用户详情-异常", e);
            return ResponseResultVO.errorResult();
        }
    }

    @Override
    public ResponseResultVO selectSysUserList(SysUserRO userRO) {
        try {
            List<SysUserVO> list = this.sysUserMapper.selectSysUserList(userRO);
            int count = this.sysUserMapper.selectSysUserListCount(userRO);

            return ResponseResultVO.resultList(list, count, userRO);
        } catch (Exception e) {
            logger.error("查询-管理员用户列表-异常", e);
            return ResponseResultVO.errorResult();
        }
    }

    @Override
    public ResponseResultVO updateSysUserRole(SysUserRoleRO ro) {
        try {
            if (StringUtils.isBlank(ro.getUserId())) {
                return ResponseResultVO.failResult("用户id不能为空");
            }
            if (StringUtils.isBlank(ro.getRoleId())) {
                return ResponseResultVO.failResult("角色id不能为空");
            }
            if (ro.getOpType() == null || ro.getOpType() != 1) {
                // 新增
                this.sysUserMapper.addSysUserRole(ro);

                // this.sysOperateLogService.insertAddLog(SysModuleType.SYS_USER, ro.getUserId(), "新增-管理员用户角色");
            } else {
                // 删除
                int num = this.sysUserMapper.deleteSysUserRole(ro);
                if (num <= 0) {
                    return ResponseResultVO.failResult();
                }
                // this.sysOperateLogService.insertDeleteLog(SysModuleType.SYS_USER, ro.getUserId(), "删除-管理员用户角色");
            }
            return ResponseResultVO.successResult();
        } catch (Exception e) {
            logger.error("修改-管理员用户角色-异常", e);
            return ResponseResultVO.errorResult();
        }
    }

    @Override
    public ResponseResultVO selectSysUserRoleList(String id) {
        try {
            if (StringUtils.isBlank(id)) {
                return ResponseResultVO.failResult("角色id不能为空");
            }
            List<SysRoleVO> list = this.sysRoleMapper.selectByUserId(id);

            return ResponseResultVO.successResult(list);
        } catch (Exception e) {
            logger.error("查询-管理员用户角色列表-异常", e);
            return ResponseResultVO.errorResult();
        }
    }


}
