package com.qimeixun.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @desc 返回前端客服数据模型
 * @author yueyufan
 * @date 2019年11月21日 下午3:04:54
 */
@Data
@ApiModel(description = "返回前端客服数据模型")
public class CustomerServiceVO implements Serializable {

	private static final long serialVersionUID = 8391145152981366117L;

	@ApiModelProperty(value = "用户主键ID(与业务无关)", example = "1911183938551090686238720")
	private String userId;

	@ApiModelProperty(value = "客服姓名", example = "张三")
	private String truename;

	@ApiModelProperty(value = "性别（0：男；1：女；2：保密）", example = "0")
	private String sex;

	@ApiModelProperty(value = "客服手机号", example = "1808629xxxx")
	private String mobile;

	@ApiModelProperty(value = "邮箱", example = "xxxxxx@qq.com")
	private String email;

	@ApiModelProperty(value = "是否分组（0：未分组；1：有分组）", example = "0")
	private String isGroup;

	@ApiModelProperty(value = "模板状态（0：禁用；1：启用；2：未配置模板）", example = "1")
	private String callModel;

	@ApiModelProperty(value = "组名称", example = "奋斗组")
	private String groupName;

	@ApiModelProperty(value = "职位（1：管理员；2：数据管理员；3：项目经理；4：组长；5：平台客服；6：质检员）", example = "5")
	private Integer position;

	@ApiModelProperty(value = "历史总回款额", example = "500000.00")
	private BigDecimal historyTotalMoneyBack;

	@ApiModelProperty(value = "历史客户数量", example = "241")
	private Integer historyTotalCustomer;

	private Integer hasTemplate;

	@ApiModelProperty(value = "客服当日发函量", example = "")
	private int sendEmsCount;

	@ApiModelProperty(value = "客服当日主动跟进数量", example = "")
	private int followCount;

	@ApiModelProperty(value = "客服当日来电数量", example = "")
	private int phoneCount;
}
