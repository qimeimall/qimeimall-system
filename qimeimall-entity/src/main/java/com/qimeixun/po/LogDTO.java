package com.qimeixun.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_log")
public class LogDTO {

    private String logId;
    private Date createTime;
    private String type;
    private String title;
    private String remoteAddr;
    private String requestUri;
    private String method;
    private String params;
    private String exception;
    private Date operateDate;
    private Long timeout;
    private String userId;
    private String resultParams;

}
