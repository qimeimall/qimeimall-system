package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qimeixun.po.NewsDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chenshouyang
 * @date 2020/5/1911:59
 */
@Mapper
public interface NewsMapper extends BaseMapper<NewsDTO> {
}
