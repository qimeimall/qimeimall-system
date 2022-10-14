package com.qimeixun.modules.market.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.WithdrawalMapper;
import com.qimeixun.modules.market.service.FinancialService;
import com.qimeixun.po.WithdrawalDTO;
import com.qimeixun.ro.FailWithdrawalRO;
import com.qimeixun.ro.QueryWithdrawalRO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FinancialServiceImpl implements FinancialService {

    @Resource
    WithdrawalMapper withdrawalMapper;

    @Override
    public IPage<WithdrawalDTO> selectMyWithdrawalList(Page page, QueryWithdrawalRO queryWithdrawalRO) {

       return withdrawalMapper.selectMyWithdrawalList(page, queryWithdrawalRO);
    }

    @Override
    public int updateWithdrawalSuccess(String id) {
        WithdrawalDTO withdrawalDTO = withdrawalMapper.selectById(id);
        if (withdrawalDTO == null) {
            throw new ServiceException("该申请记录不存在");
        }
        if (!"0".equals(withdrawalDTO.getStatus())) {
            throw new ServiceException("该申请记录状态不正确");
        }
        withdrawalDTO.setStatus("1");
        return withdrawalMapper.updateById(withdrawalDTO);
    }

    @Override
    public int updateWithdrawalFail(FailWithdrawalRO failWithdrawalRO) {
        if (failWithdrawalRO.getId() == null) {
            throw new ServiceException("该申请记录不存在");
        }
        WithdrawalDTO withdrawalDTO = withdrawalMapper.selectById(failWithdrawalRO.getId());
        if (withdrawalDTO == null) {
            throw new ServiceException("该申请记录不存在");
        }
        if (!"0".equals(withdrawalDTO.getStatus())) {
            throw new ServiceException("该申请记录状态不正确");
        }
        if (StrUtil.isBlank(failWithdrawalRO.getRemark())) {
            throw new ServiceException("请填写备注");
        }
        withdrawalDTO.setStatus("2");
        return withdrawalMapper.updateById(withdrawalDTO);
    }
}
