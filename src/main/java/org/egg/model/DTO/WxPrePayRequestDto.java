package org.egg.model.DTO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author cdt
 * @Description 支付入参实体
 * @date: 2018/3/29 15:44
 */
@Data
public class WxPrePayRequestDto implements Serializable {

    /**
     * 商户订单号(唯一标示) 必须
     */
    private String outTradeNo;

    /**
     * 支付金额（单位：元） 必须
     */
    private BigDecimal totalAmount;
    /**
     * 客户端信息 必须 ip 必须
     */
    private ClientInfo clientInfo;
    /**
     * 用户唯一标示 必须
     */
    private String userNo;

    /**
     * 附加信息json
     */
    private String attachJson;
    /**
     * openId
     */
    private String openId;
    /**
     * miniOpenId
     */
    private String miniOpenId;

    private String orderMsg;

}
