package com.qimeixun.modules.material.controller;

import com.qimeixun.base.BaseController;
import com.qimeixun.entity.MaterialGroup;
import com.qimeixun.entity.MaterialImg;
import com.qimeixun.modules.material.service.MaterialService;
import com.qimeixun.ro.MaterialImgRO;
import com.qimeixun.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 素材管理
 *
 * @author chenshouyang
 * @date 2020/5/317:25
 */
@Api(tags = "素材管理相关接口")
@RestController
@RequestMapping(value = "/material", produces = "application/json;charset=UTF-8")
public class MaterialController extends BaseController {

    @Resource
    MaterialService materialService;

    @ApiOperation(value = "查询素材组列表", notes = "查询素材组列表")
    @RequestMapping(value = "/selectMaterialGroupList", method = RequestMethod.GET)
    public ResponseResultVO selectMaterialGroupList() {
        return ResponseResultVO.successResult(materialService.selectMaterialGroupList());
    }

    @ApiOperation(value = "新增素材组", notes = "新增素材组")
    @RequestMapping(value = "/insertMaterialGroup", method = RequestMethod.GET)
    public ResponseResultVO insertMaterialGroup(@RequestParam String name) {
        if (StringUtils.isEmpty(name)) {
            return ResponseResultVO.failResult("参数不能为空");
        }
        return insertResult(materialService.insertMaterialGroup(name));
    }

    @ApiOperation(value = "修改素材组", notes = "新增素材组")
    @RequestMapping(value = "/updateMaterialGroup", method = RequestMethod.POST)
    public ResponseResultVO updateMaterialGroup(@RequestBody MaterialGroup materialGroup) {
        if (StringUtils.isEmpty(materialGroup.getName()) && materialGroup.getId() != null) {
            return ResponseResultVO.failResult("参数不能为空");
        }
        return updateResult(materialService.updateMaterialGroup(materialGroup));
    }

    @ApiOperation(value = "删除素材组（软删除）", notes = "删除素材组")
    @RequestMapping(value = "/deleteMaterialGroup", method = RequestMethod.GET)
    public ResponseResultVO deleteMaterialGroup(@RequestParam Integer id) {
        return deleteResult(materialService.deleteMaterialGroup(id));
    }

    @ApiOperation(value = "素材列表", notes = "素材列表")
    @RequestMapping(value = "/selectMaterialImgList", method = RequestMethod.POST)
    public ResponseResultVO selectMaterialImgList(@RequestBody MaterialImgRO materialImgRO) {
        return getPageObject(materialService.selectMaterialImgList(materialImgRO), materialImgRO);
    }

    @ApiOperation(value = "删除素材", notes = "素材")
    @RequestMapping(value = "/deleteMaterialImg", method = RequestMethod.GET)
    public ResponseResultVO deleteMaterialImg(@RequestParam Integer id) {
        return deleteResult(materialService.deleteMaterialImg(id));
    }

    @ApiOperation(value = "上传素材", notes = "上传素材")
    @RequestMapping(value = "/uploadImgFile", method = RequestMethod.POST)
    public ResponseResultVO uploadImgFile(@RequestParam(defaultValue = "") String name, @RequestParam("file") MultipartFile file) {
        return ResponseResultVO.successResult(materialService.uploadImgFile(name, file));
    }

    @ApiOperation(value = "保存素材", notes = "保存素材")
    @RequestMapping(value = "/insertImgFile", method = RequestMethod.POST)
    public ResponseResultVO insertImgFile(@RequestBody MaterialImg materialImg) {
        return insertResult(materialService.insertImgFile(materialImg));
    }
}
