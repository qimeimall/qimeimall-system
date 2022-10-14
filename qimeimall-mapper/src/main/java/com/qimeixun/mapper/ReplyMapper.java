package com.qimeixun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qimeixun.po.ReplyDTO;
import com.qimeixun.ro.QuerytProductReplyRO;
import com.qimeixun.ro.SysReplayRO;
import com.qimeixun.vo.SysReplayVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReplyMapper extends BaseMapper<ReplyDTO> {
    IPage<ReplyDTO> selectReplyList(Page page, @Param("params") QuerytProductReplyRO querytProductReplyRO);

    IPage<SysReplayVO> selectSysReplayList(Page page, @Param("params") SysReplayRO sysReplayRO);
}
