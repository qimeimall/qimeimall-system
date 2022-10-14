package com.qimeixun.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author chenshouyang
 * @date 2020/5/1220:46
 */
@Data
public class CustomerAcountRecordVO {

    private Integer id;

    private double count;

    private Date createTime;

    private String nickName;

    private String sysUserName;

    private String type;

    private String way;

    private String mark;
}
