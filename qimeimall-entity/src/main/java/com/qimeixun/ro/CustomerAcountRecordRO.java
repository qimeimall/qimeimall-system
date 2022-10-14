package com.qimeixun.ro;

import lombok.Data;

/**
 * @author chenshouyang
 * @date 2020/5/1219:35
 */
@Data
public class CustomerAcountRecordRO extends PageRO {

    private String nickName;

    private String phone;

    private String type;
}
