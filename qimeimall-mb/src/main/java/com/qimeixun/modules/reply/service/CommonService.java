package com.qimeixun.modules.reply.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CommonService {
    Map uploadImgFile(String name, MultipartFile file);
}
