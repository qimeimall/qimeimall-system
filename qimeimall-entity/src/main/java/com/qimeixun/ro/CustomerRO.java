package com.qimeixun.ro;

import lombok.Data;

/**
 * @author chenshouyang
 * @date 2020/5/517:11
 */
@Data
public class CustomerRO extends PageRO {
    private String nickName;

    private String phone;
}
