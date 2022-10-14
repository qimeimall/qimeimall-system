package com.qimeixun.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysLoginLogVO implements Serializable{
	
	private static final long serialVersionUID = 5251906354087592497L;

	private String id;//主键ID

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginTime;//登录时间

    private String loginIp;//登录IP

    private String browser;//浏览器

    private String system;//系统

    private String description;//描述

    private String loginName;//登录用户名

    private String userName;//登录者真实姓名
    
    private String isMobile;//是否是移动终端访问（0：非移动端；1：是移动终端）
}
