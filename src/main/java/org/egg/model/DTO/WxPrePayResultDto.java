package org.egg.model.DTO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author cdt
 * @Description 支付反参实体
 * @date: 2018/3/29 15:44
 */
@Data
public class WxPrePayResultDto implements Serializable {
    private static final long serialVersionUID = -5066149409423631947L;
    private String prepay_id;
    private String packageValue;
    private String nonceStr;
    private String paySign;
    private Long timeStamp;

    private String attach;
    private String outTradeNo;
    /**
     * 支付类型
     * RECHARGE", "充值"),
     */
    private String payType;
    /**
     * h5支付时，返回的url
     * mweb_url为拉起微信支付收银台的中间页面，可通过访问该url来拉起微信客户端，完成支付,mweb_url的有效期为5分钟。
     */
    private String mwebUrl;
    /**
     * trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行Native支付
     */
    private String codeUrl;

    private String orderNo;

    private BigDecimal amount;

}
