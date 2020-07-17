package org.egg.model.DTO;

import java.io.Serializable;

/**
 * @author cdt
 * @Description 查询微信支付单详情
 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_2
 * @date: 2018/4/2 16:01
 */
public class WxQueryPayOrderDto extends WxUDto implements Serializable {
    private static final long serialVersionUID = -3051789941466586638L;
    private String nonce_str;
    /**
     * SUCCESS—支付成功
     * <p>
     * REFUND—转入退款
     * <p>
     * NOTPAY—未支付
     * <p>
     * CLOSED—已关闭
     * <p>
     * REVOKED—已撤销（刷卡支付）
     * <p>
     * USERPAYING--用户支付中
     * <p>
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     * <p>
     * 支付状态机请见下单API页面
     */
    private String trade_state;

    private String trade_state_desc;

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getTrade_state() {
        return trade_state;
    }

    public void setTrade_state(String trade_state) {
        this.trade_state = trade_state;
    }

    public String getTrade_state_desc() {
        return trade_state_desc;
    }

    public void setTrade_state_desc(String trade_state_desc) {
        this.trade_state_desc = trade_state_desc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"nonce_str\":\"")
                .append(nonce_str).append('\"');
        sb.append(",\"trade_state\":\"")
                .append(trade_state).append('\"');
        sb.append(",\"trade_state_desc\":\"")
                .append(trade_state_desc).append('\"');
        StringBuilder sbSuper = new StringBuilder(super.toString());
        sbSuper.replace(0, 1, ",");
        sbSuper.deleteCharAt(sbSuper.length() - 1);
        sb.append(sbSuper.toString());
        sb.append('}');
        return sb.toString();
    }
}
