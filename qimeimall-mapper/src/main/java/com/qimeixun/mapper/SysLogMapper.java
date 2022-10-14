package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qimeixun.po.LogDTO;
import com.qimeixun.ro.SysLogRO;
import com.qimeixun.vo.SysLogVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysLogMapper extends BaseMapper<LogDTO> {
	
	//删除
    int deleteByPrimaryKey(String logId);
    
    //批量删除
    int deleteBatch(String[] ids);

    //查询所有
    List<SysLogVO> selectAll(SysLogRO sysLogRO);
    
    //查询所有大小
    int selectAllCount(SysLogRO sysLogRO);

}