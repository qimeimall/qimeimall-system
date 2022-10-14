package com.qimeixun.vo;


import com.qimeixun.enums.SysModuleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统操作日志
 * @author wangdaqiang
 * @date 2019-08-22
 */
@Data
@ApiModel
public class SysOperateLogVO implements Serializable {

    private static final long serialVersionUID = -6143447529207091685L;


    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "操作类型(0：新增、1：修改、2：删除、3：登陆日志)")
    private Integer operateType;

    @ApiModelProperty(value = "模块id")
    private SysModuleType moduleId;

    @ApiModelProperty(value = "模块名称")
    private String moduleName;

    @ApiModelProperty(value = "targetId")
    private String targetId;

    @ApiModelProperty(value = "操作内容描述")
    private String description;

    @ApiModelProperty(value = "操作用户id")
    private String userId;

    @ApiModelProperty(value = "操作用户IP")
    private String userIp;

    @ApiModelProperty(value = "操作时间")
    private Date createdAt;

    @ApiModelProperty(value = "操作用户账号")
    private String loginName;

    @ApiModelProperty(value = "操作用户名称")
    private String userName;



    public String getModuleName() {
        return moduleId.getName();
    }
}
