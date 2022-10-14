package com.qimeixun.modules.reply.service.impl;

import com.qimeixun.common.QiNiuUtil;
import com.qimeixun.modules.reply.service.CommonService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class CommonServiceImpl implements CommonService {


    @Resource
    private QiNiuUtil qiNiuUtil;

    @Override
    public Map uploadImgFile(String name, MultipartFile file) {
        return qiNiuUtil.uploadImgFile(name, file);
    }
}
