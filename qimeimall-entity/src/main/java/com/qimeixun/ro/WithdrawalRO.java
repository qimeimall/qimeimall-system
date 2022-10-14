package com.qimeixun.ro;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawalRO {

    private String account;

    private String accountName;

    private String type;

    private BigDecimal money;
}
