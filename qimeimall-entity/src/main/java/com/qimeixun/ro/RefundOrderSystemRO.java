package com.qimeixun.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class RefundOrderSystemRO extends PageRO {

    @ApiModelProperty(value = "1:待审核 2：审核拒绝 3：审核通过 4：已撤销")
    private String type;


    /**
     * 收件人名称
     */
    private String name;

    /**
     * 收件人电话
     */
    private String phone;

    /**
     * 用户名
     */
    private String nickName;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 订单号
     */
    private String orderId;
}
