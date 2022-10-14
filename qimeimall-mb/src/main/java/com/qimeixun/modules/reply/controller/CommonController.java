package com.qimeixun.modules.reply.controller;

import com.qimeixun.modules.reply.service.CommonService;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Api(tags = "文件上传")
@RestController
@RequestMapping(value = "/upload", produces = "application/json;charset=UTF-8")
public class CommonController {

    @Resource
    CommonService commonService;

    @ApiOperation(value = "上传素材", notes = "上传素材")
    @RequestMapping(value = "/uploadImgFile", method = RequestMethod.POST)
    public ResponseResultVO uploadImgFile(@RequestParam(defaultValue = "") String name, @RequestParam("file") MultipartFile file) {
        return ResponseResultVO.successResult(commonService.uploadImgFile(name, file));
    }

}
