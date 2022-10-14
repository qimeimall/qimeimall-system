package com.qimeixun.modules.system.service.impl;

import com.qimeixun.entity.SysUser;
import com.qimeixun.enums.SysModuleType;
import com.qimeixun.enums.SysOperateType;
import com.qimeixun.mapper.SysOperateLogMapper;
import com.qimeixun.ro.SysLoginLog;
import com.qimeixun.ro.SysOperateLogRO;
import com.qimeixun.modules.system.service.SysOperateLogService;
import com.qimeixun.util.TokenUtil;
import com.qimeixun.util.Tool;
import com.qimeixun.vo.ResponseResultVO;
import com.qimeixun.vo.SysOperateLogVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 系统操作日志接口实现
 * @author wangdaqiang
 * @date 2019-08-22
 */
@Service
public class SysOperateLogServiceImpl implements SysOperateLogService {

    private static final Logger logger = LoggerFactory.getLogger(SysOperateLogServiceImpl.class);

    @Resource
    private SysOperateLogMapper sysOperateLogMapper;
    @Resource
    private TokenUtil tokenUtil;


    @Override
    public void insertLog(SysOperateType operateType, SysModuleType moduleType, String targetId, String description, Object beforeData, Object afterData) {
        String userId = null;
        try {
            userId = this.tokenUtil.getUserIdByToken();
            saveLog(userId,"",description);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("operateType=").append(operateType).append(",");
            sb.append("moduleType=").append(moduleType).append(",");
            sb.append("targetId=").append(targetId).append(",");
            sb.append("description=[").append(description).append("],");
            sb.append("userId=").append(userId);
            sb.append("IP=").append(this.tokenUtil.getIpAddr());
            sb.append("}");
            logger.error("新增-操作日志-异常:" + sb.toString(), e);
        }
    }

    @Override
    public void insertAddLog(SysModuleType moduleType, String targetId, String description) {
        this.insertLog(SysOperateType.ADD, moduleType, targetId, description, null, null);
    }

    @Override
    public void insertAddLog(String targetId, String description) {
        this.insertAddLog(SysModuleType.BACKGROUND, targetId, description);
    }

    @Override
    public void insertUpdateLog(SysModuleType moduleType, String targetId, String description, Object beforeData, Object afterData) {
        this.insertLog(SysOperateType.UPDATE, moduleType, targetId, description, beforeData, afterData);
    }

    @Override
    public void insertUpdateLog(String targetId, String description, Object beforeData, Object afterData) {
        this.insertUpdateLog(SysModuleType.BACKGROUND, targetId, description, beforeData, afterData);
    }

    @Override
    public void insertDeleteLog(SysModuleType moduleType, String targetId, String description) {
        this.insertLog(SysOperateType.DELETE, moduleType, targetId, description, null, null);
    }

    @Override
    public void insertDeleteLog(String targetId, String description) {
        this.insertDeleteLog(SysModuleType.BACKGROUND, targetId, description);
    }


    @Override
    public void insertLoginLog(SysUser sysUser, String description) {
        try {
            saveLog(sysUser.getId(),sysUser.getLoginName(),description);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("operateType=").append(SysOperateType.LOGIN_LOG).append(",");
            sb.append("moduleType=").append(SysModuleType.LOGIN_LOG).append(",");
            sb.append("targetId=").append(sysUser.getLoginName()).append(",");
            sb.append("description=[").append(description).append("],");
            sb.append("userId=").append(sysUser.getId());
            sb.append("IP=").append(this.tokenUtil.getIpAddr());
            sb.append("}");
            logger.error("登录日志记录-异常:" + sb.toString(), e);
        }
    }

    // 添加系统登录日志
    private void saveLog(String user_id, String user_name, String description) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        SysLoginLog log = new SysLoginLog();
        log.setId(Tool.getPrimaryKey());
        log.setLoginTime(new Date());
        log.setLoginIp(tokenUtil.getIpAddr());
        log.setBrowser(Tool.getBrowserInfo(request));
        log.setSystem(Tool.getSystemInfo(request));
        log.setDescription(description);
        log.setLoginName(user_name);
        log.setUserId(user_id);
        log.setIsMobile(Tool.getIsMobile(request));
        sysOperateLogMapper.insertLog(log);
    }

    @Override
    public ResponseResultVO selectLogList(SysOperateLogRO logRO) {
        try {
            List<SysOperateLogVO> list = this.sysOperateLogMapper.selectLogList(logRO);
            int count = this.sysOperateLogMapper.selectLogListCount(logRO);

            return ResponseResultVO.resultList(list, count, logRO);
        } catch (Exception e) {
            logger.error("查询-操作日志-异常", e);
            return ResponseResultVO.errorResult();
        }
    }

}
