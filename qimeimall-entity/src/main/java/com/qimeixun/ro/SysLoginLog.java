package com.qimeixun.ro;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @desc 系统登录日志
 * @author yueyufan
 * @date 2019年11月18日 下午5:56:55
 */
@Data
public class SysLoginLog extends PageRO{
	
	@ApiModelProperty(hidden = true)
    private String id;//主键ID

	@ApiModelProperty(value = "查询开始时间", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginTimeBegin;//查询开始时间
    
	@ApiModelProperty(value = "查询结束时间", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginTimeEnd;//查询结束时间
    
	@ApiModelProperty(hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginTime;//登录时间

	@ApiModelProperty(hidden = true)
    private String loginIp;//登录IP

	@ApiModelProperty(hidden = true)
    private String browser;//浏览器

	@ApiModelProperty(hidden = true)
    private String system;//系统

	@ApiModelProperty(hidden = true)
    private String description;//描述

	@ApiModelProperty(value = "登录账号", example = "zhangsan")
    private String loginName;//登录用户名

	@ApiModelProperty(hidden = true)
    private String userId;//登录者用户ID
    
    @ApiModelProperty(value = "真实姓名", example = "张三")
    private String userName;//登录者真实姓名
    
    @ApiModelProperty(hidden = true)
    private String isMobile;//是否是移动终端访问（0：非移动端；1：是移动终端）
}