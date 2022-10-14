package com.qimeixun.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @desc 系统分组表
 * @author yueyufan
 * @date 2019年11月18日 下午4:43:07
 */
@Data
@ApiModel(description = "系统分组数据模型")
public class SysGroup {

	@ApiModelProperty(value = "主键ID(与业务无关)", example = "新增不填")
	private String id;// 主键ID

	@ApiModelProperty(value = "创建时间", example = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;// 创建时间

	@ApiModelProperty(value = "更新时间", example = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;// 更新时间

	@ApiModelProperty(value = "组名", example = "奋斗组")
	private String groupName;// 组名

	@ApiModelProperty(value = "组长ID", example = "")
	private String groupLeaderId;// 组长ID

	@ApiModelProperty(value = "委托方ID", example = "")
	private String entrustId;// 委托方ID

	@ApiModelProperty(value = "委托方", example = "")
	private String thirdParty;// 委托方
}
