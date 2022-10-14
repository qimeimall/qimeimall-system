package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.po.WithdrawalDTO;
import com.qimeixun.ro.QueryWithdrawalRO;
import com.qimeixun.ro.RetailRO;
import com.qimeixun.vo.RetailRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WithdrawalMapper extends BaseMapper<WithdrawalDTO> {
    IPage<WithdrawalDTO> selectMyWithdrawalList(Page page, @Param("params") QueryWithdrawalRO queryWithdrawalRO);

    IPage<RetailRecordVO> retailList(Page page, @Param("params") RetailRO retailRO);
}
