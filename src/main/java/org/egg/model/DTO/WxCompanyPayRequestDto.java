package org.egg.model.DTO;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author cdt
 * @Description 企业付款入参实体
 * @date: 2018/3/29 15:44
 */
public class WxCompanyPayRequestDto implements Serializable {

    /**
     * 商户订单号(唯一标示) 必须
     */
    private String outTradeNo;

    /**
     * 支付金额（单位：元） 必须
     */
    private BigDecimal totalAmount;
    /**
     * 服务端 ip 必须
     */
    private String ip;

    /**
     * openId
     */
    private String openId;

    /**
     * miniOpenId
     */
    private String miniOpenId;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getMiniOpenId() {
        return miniOpenId;
    }

    public void setMiniOpenId(String miniOpenId) {
        this.miniOpenId = miniOpenId;
    }
}
