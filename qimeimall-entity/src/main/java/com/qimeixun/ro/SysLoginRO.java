package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 管理员登录-请求参数模型
 * @author wangdaqiang
 * @date 2019-08-23 15:40
 */
@Data
@ApiModel
public class SysLoginRO implements Serializable {

    private static final long serialVersionUID = 724274962841959313L;

    /**
     * 登录账号
     */
    @ApiModelProperty(value = "登录账号", example = "admin")
    @NotBlank(message = "请输入登录账号")
    private String loginName;

    /**
     * 登录密码
     */
    @ApiModelProperty(value = "登录密码", example = "123456")
    @NotBlank(message = "请输入密码")
    private String password;


    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码", example = "123456")
    @NotBlank(message = "请输入验证码")
    private String captcha;

    /**
     * 验证码客户端id
     */
    @ApiModelProperty(value = "验证码", example = "a")
    @NotBlank(message = "请输刷新验证码")
    private String client;

}
