package com.qimeixun.mapper;

import com.qimeixun.entity.SysGroup;
import com.qimeixun.vo.CustomerServiceVO;
import com.qimeixun.vo.SysGroupVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysGroupMapper{

	int deleteByPrimaryKey(String id);

	SysGroup selectByPrimaryKey(String id);

	// 根据组长ID查询组信息
	SysGroup selectByGroupLeaderId(String leaderId);

	SysGroup selectByGroupName(String id);

	// 根据委托方名称查询下面所有组信息
	List<SysGroupVO> selectGroupByEntrustName(String entrustName);

	// 根据委托方ID查询下面所有组信息
	List<SysGroupVO> selectGroupByEntrustId(String entrustId);

	// 查询所有组信息
	List<SysGroupVO> selectAll();

	List<SysGroupVO> selectAllGroup(SysGroup sysGroup);
	List<CustomerServiceVO> selectAllUserByGroupId(String group_id);

}