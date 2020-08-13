package org.egg.model.VO;

import lombok.Data;
import org.egg.model.DTO.ClientInfo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/31 14:26
 */
@Data
public class PayReq implements Serializable {
    private BigDecimal amount;
    private ClientInfo clientInfo;
    /**
     * 用于记录支付的商品信息
     */
    private String orderMsg;

    private String orderType;
}
