package com.qimeixun.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @desc 返回前端系统操作日志模型
 * @author yueyufan
 * @date 2019年11月22日 上午9:51:38
 */
@Data
public class SysLogVO implements Serializable{
	
	private static final long serialVersionUID = -281277324705912279L;

	private String logId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;//记录时间

	private String type;//日志类型（info/error）

	private String title;//内容

	private String remoteAddr;//IP地址

	private String exception;//异常信息

	private String userName;//操作者

	private String timeout;

	private String requestUri;
}
