package com.qimeixun.mapper;

import com.qimeixun.ro.SysLoginLog;
import com.qimeixun.vo.SysLoginLogVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysLoginLogMapper{
	
    int deleteByPrimaryKey(String id);

    int insert(SysLoginLog record);

//    int insertSelective(SysLoginLog record);

    SysLoginLog selectByPrimaryKey(String id);
    
    List<SysLoginLogVO> queryAll(SysLoginLog sysLoginLog);
    
    int queryAllCount(SysLoginLog sysLoginLog);

//    int updateByPrimaryKeySelective(SysLoginLog record);

//    int updateByPrimaryKey(SysLoginLog record);
}