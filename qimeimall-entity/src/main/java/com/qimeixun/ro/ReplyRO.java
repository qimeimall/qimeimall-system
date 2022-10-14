package com.qimeixun.ro;

import lombok.Data;

import java.util.List;

@Data
public class ReplyRO {

    private String orderId;

    private String productId;

    private String productScore;

    private String serviceScore;

    private String comment;

    private List<String> pics;

    private String cartId;
}
