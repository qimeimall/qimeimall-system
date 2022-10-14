package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qimeixun.po.UserCollectionDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserCollectionMapper extends BaseMapper<UserCollectionDTO> {
}
