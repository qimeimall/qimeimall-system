package com.qimeixun.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc 系统用户树形结构数据模型
 * @author yueyufan
 * @date 2019年12月20日 下午6:10:45
 */
@Data
public class TreeUser implements Serializable {

	private static final long serialVersionUID = -2109149822404368929L;

	@ApiModelProperty(value = "ID", example = "新增不填")
	private String id;

	@ApiModelProperty(value = "节点名称", example = "admin")
	private String nodeName;

	@ApiModelProperty(value = "上级节点")
	private String parentId;

	@ApiModelProperty(value = "子集合")
	private List<TreeUser> children = new ArrayList<>();

}
