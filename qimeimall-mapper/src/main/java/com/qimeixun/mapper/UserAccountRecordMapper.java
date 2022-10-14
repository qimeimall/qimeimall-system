package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.entity.UserAccountRecord;
import com.qimeixun.ro.CustomerAcountRecordRO;
import com.qimeixun.vo.CustomerAcountRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author chenshouyang
 * @date 2020/5/1218:04
 */
@Mapper
public interface UserAccountRecordMapper extends BaseMapper<UserAccountRecord> {

    IPage<CustomerAcountRecordVO> selectCustomerAcountRecord(Page page, @Param("params") CustomerAcountRecordRO customerLevelRO);
}
