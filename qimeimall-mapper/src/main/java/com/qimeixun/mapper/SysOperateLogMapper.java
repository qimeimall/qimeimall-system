package com.qimeixun.mapper;

import com.qimeixun.ro.SysLoginLog;
import com.qimeixun.ro.SysOperateLogRO;
import com.qimeixun.vo.SysOperateLogVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统操作日志Mapper
 * @author wangdaqiang
 * @date 2019-08-22
 */
@Mapper
public interface SysOperateLogMapper {

    /**
     * 插入操作日志
     * @param log 日志信息
     * @return
     */
    int insertLog(SysLoginLog log);


    /**
     * 查询操作日志(分页)
     * @param logRO 查询条件
     * @return
     */
    List<SysOperateLogVO> selectLogList(SysOperateLogRO logRO);


    /**
     * 查询操作日志(分页)
     * @param logRO 查询条件
     * @return
     */
    int selectLogListCount(SysOperateLogRO logRO);


}
