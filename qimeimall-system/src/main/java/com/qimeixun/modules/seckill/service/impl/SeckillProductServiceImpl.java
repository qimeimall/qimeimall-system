package com.qimeixun.modules.seckill.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.constant.SystemConstant;
import com.qimeixun.entity.SysUser;
import com.qimeixun.exceptions.ServiceException;
import com.qimeixun.mapper.SeckillProductMapper;
import com.qimeixun.modules.seckill.service.SeckillProductService;
import com.qimeixun.po.SeckillProductDTO;
import com.qimeixun.ro.SeckillProductRO;
import com.qimeixun.modules.system.service.impl.CommonService;
import com.qimeixun.util.RedisUtil;
import com.qimeixun.util.TokenUtil;
import com.qimeixun.vo.SeckillProductVO;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/6/2814:14
 */
@Service
public class SeckillProductServiceImpl extends CommonService implements SeckillProductService {

    @Resource
    TokenUtil tokenUtil;

    @Resource
    SeckillProductMapper seckillProductMapper;

    @Resource
    RedisUtil redisUtil;

    @Override
    public int insertSeckillProduct(SeckillProductRO seckillProductRO) {
        if(seckillProductRO.getStock() == 0){
            throw new ServiceException("库存不能为0");
        }
        SeckillProductDTO seckillProductDTO = new SeckillProductDTO();
        BeanUtils.copyProperties(seckillProductRO, seckillProductDTO);
        SysUser sysUserInfo = tokenUtil.getSysUserInfo();
        seckillProductDTO.setStoreId(sysUserInfo.getStoreId());

        int i = seckillProductMapper.insert(seckillProductDTO);

        openSeckillCache(seckillProductDTO, seckillProductRO.getEndTime(), seckillProductRO.getStock());

        return i;
    }

    @Override
    public IPage<SeckillProductVO> selectSeckillProductList(SeckillProductRO seckillProductRO) {
        seckillProductRO.setStoreId(tokenUtil.getSysUserInfo().getStoreId());
        Page page = new Page<SeckillProductVO>(seckillProductRO.getCurrentPage(), seckillProductRO.getPageSize());
        IPage<SeckillProductVO> iPage = seckillProductMapper.selectSeckillProductList(page, seckillProductRO);
        return iPage;
    }

    @Override
    public int deleteSeckillProductById(String id) {
        SeckillProductDTO seckillProductDTO = seckillProductMapper.selectById(id);
        seckillProductDTO.setIsDelete(1);
        return seckillProductMapper.updateById(seckillProductDTO);
    }

    @Override
    public int updateStatusSeckillProductById(String id) {
        SeckillProductDTO seckillProductDTO = seckillProductMapper.selectById(id);
        if (seckillProductDTO.getStatus() == 0) {
            //禁用
            seckillProductDTO.setStatus(1);
            //删除redis缓存
            redisUtil.del(SystemConstant.SECKILL_PRODUCT + seckillProductDTO.getId());
        } else {
            //开启
            openSeckillCache(seckillProductDTO, seckillProductDTO.getEndTime(), seckillProductDTO.getStock());
            seckillProductDTO.setStatus(0);
        }
        return seckillProductMapper.updateById(seckillProductDTO);
    }

    @Override
    public SeckillProductDTO selectSeckillProductById(String id) {
        SeckillProductDTO seckillProductDTO = seckillProductMapper.selectById(id);
        return seckillProductDTO;
    }

    @Override
    public int updateSeckillProduct(SeckillProductRO seckillProductRO) {
        SeckillProductDTO seckillProductDTO = new SeckillProductDTO();
        BeanUtils.copyProperties(seckillProductRO, seckillProductDTO);

        int i = seckillProductMapper.updateById(seckillProductDTO);
        if(i > 0){
            SeckillProductDTO dto = seckillProductMapper.selectById(seckillProductRO.getId());
            openSeckillCache(dto, dto.getEndTime(), dto.getStock());
        }
        return i;
    }

    private void openSeckillCache(SeckillProductDTO seckillProductDTO, Date endTime, int stock) {
        if (endTime != null) {
            long time = (seckillProductDTO.getEndTime().getTime() - new Date().getTime()) / 1000 / 60;
            redisUtil.set(SystemConstant.SECKILL_PRODUCT + seckillProductDTO.getId(), stock, time);
        } else {
            redisUtil.set(SystemConstant.SECKILL_PRODUCT + seckillProductDTO.getId(), stock);
        }
    }
}
