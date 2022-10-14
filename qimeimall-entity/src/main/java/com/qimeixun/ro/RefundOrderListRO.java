package com.qimeixun.ro;

import lombok.Data;

@Data
public class RefundOrderListRO extends PageRO{

    private String type; //0 全部 ， 1：退款中  2：退款完成

    private String userId;
}
