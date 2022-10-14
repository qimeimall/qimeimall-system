package com.qimeixun.ro;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RefundGoodsRO {

    @NotNull(message = "请选择退货订单")
    private String orderId;

    private List<RefundGoodsChildrenRO> childrenROList;

    private String remark;

    /**
     * 1 退款  2 退货退款
     */
    private String type;
}
