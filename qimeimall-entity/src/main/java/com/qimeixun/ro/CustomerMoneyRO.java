package com.qimeixun.ro;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenshouyang
 * @date 2020/5/1018:02
 */
@Data
public class CustomerMoneyRO {

    private Long id;

    private BigDecimal count;

    private String way;

    private String type;

}
