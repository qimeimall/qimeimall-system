package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 系统操作日志请求参数模型
 * @author wangdaqiang
 * @date 2019-08-22
 */
@Data
@ApiModel
public class SysOperateLogRO extends PageRO {

    private static final long serialVersionUID = 5358383615964733998L;


    @ApiModelProperty(value = "操作类型(0：新增、1：修改、2：删除、3：登陆日志)", example = "3")
    private Integer operateType;

    @ApiModelProperty(value = "所属模块", example = "LOGIN_LOG")
    private String moduleId;

    @ApiModelProperty(value = "操作用户", example = "admin")
    private String userName;

    @ApiModelProperty(value = "用户IP", example = "192.168.1.36")
    private String userIp;

    @ApiModelProperty(value = "起始时间", example = "2019-10-01")
    private Date startDate;

    @ApiModelProperty(value = "截止时间", example = "2019-10-31")
    private Date entDate;

}
