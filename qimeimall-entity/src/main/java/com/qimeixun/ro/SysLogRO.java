package com.qimeixun.ro;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @desc 系统操作日志
 * @author yueyufan
 * @date 2019年11月22日 上午9:19:07
 */
@Data
public class SysLogRO extends PageRO{
	
	private static final long serialVersionUID = 259944673198497224L;

	@ApiModelProperty(hidden = true)
    private String logId;

	@ApiModelProperty(hidden = true)
    private Date createTime;
	
	@ApiModelProperty(value = "查询开始时间", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeBegin;
    
	@ApiModelProperty(value = "查询结束时间", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeEnd;

    @ApiModelProperty(value = "类型", example = "info/error")
    private String type;

    @ApiModelProperty(value = "内容", example = "删除用户信息")
    private String title;

    @ApiModelProperty(hidden = true)
    private String remoteAddr;

    @ApiModelProperty(hidden = true)
    private String requestUri;

    @ApiModelProperty(hidden = true)
    private String method;

    @ApiModelProperty(hidden = true)
    private String params;

    @ApiModelProperty(hidden = true)
    private String exception;

    @ApiModelProperty(hidden = true)
    private Date operateDate;

    @ApiModelProperty(hidden = true)
    private String timeout;

    @ApiModelProperty(value = "操作者", example = "张三")
    private String userName;
    
    @ApiModelProperty(hidden = true)
    private String userId;

    @ApiModelProperty(hidden = true)
    private String resultParams;
}