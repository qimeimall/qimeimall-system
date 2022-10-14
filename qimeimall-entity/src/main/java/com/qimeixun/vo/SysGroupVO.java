package com.qimeixun.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @desc 返回前端分组信息模型
 * @author yueyufan
 * @date 2019年11月21日 下午2:22:19
 */
@Data
public class SysGroupVO implements Serializable {

	private static final long serialVersionUID = -7456953783114820553L;

	private String id;// 主键ID

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;// 创建时间

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;// 更新时间

	private String groupName;// 组名

	private String entrustName;// 委托方名称

	private String entrustId;// 委托方ID

	private String groupLeaderId;// 组长ID

	private String groupLeaderName;// 组长姓名

	private String groupLeaderPhone;// 组长电话

}
