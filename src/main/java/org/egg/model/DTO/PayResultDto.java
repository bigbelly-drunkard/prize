package org.egg.model.DTO;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付结果
 * Created by chendatao on 2017/12/18.
 */
public class PayResultDto implements Serializable {
    /**
     * 第三方支付单号
     */
    private String flowNo;
    /**
     * orderNo
     */
    private String orderNo;
    /**
     *支付金额
     */
    private BigDecimal amount;
    /**
     *支付状态？
     */
    private String  status;

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
