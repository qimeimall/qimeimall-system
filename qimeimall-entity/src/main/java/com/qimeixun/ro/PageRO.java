package com.qimeixun.ro;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @desc 分页查询参数模型
 * @author yueyufan
 * @date 2019年11月6日 上午10:43:12
 */
@ApiModel(description = "分页查询参数模型")
public class PageRO implements Serializable {

	private static final long serialVersionUID = -143337743483486697L;
	/**
	 * 当前页
	 */
	@ApiModelProperty(value = "当前页", example = "1")
	private int currentPage = 1;

	/**
	 * 每页条数
	 */
	@ApiModelProperty(value = "每页条数", example = "15")
	private int pageSize = 15;

	/**
	 * mysql数据库查询条件
	 */
	@ApiModelProperty(value = "是否分页", example = "1")
	private int ifPage = 1;

	/**
	 * mysql数据库查询条件
	 */
	@ApiModelProperty(value = "起始页数", example = "3")
	private int limitFrom;

	/**
	 * 查询起始时间
	 */
	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createdFrom;


	/**
	 * 查询结束时间
	 */
	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createdTo;

	public int getIfPage() {
		return ifPage;
	}

	public void setIfPage(int ifPage) {
		this.ifPage = ifPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage <= 0 ? 1 : currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize > 0) {
			this.pageSize = pageSize;
		}
	}

	public int getLimitFrom() {
		limitFrom = (currentPage - 1) * pageSize;
		return limitFrom;
	}

	public Date getCreatedFrom() {
		return createdFrom;
	}

	public void setCreatedFrom(Date createdFrom) {
		this.createdFrom = createdFrom;
	}

	public Date getCreatedTo() {
		return createdTo;
	}

	public void setCreatedTo(Date createdTo) {
		this.createdTo = createdTo;
	}
}
