package org.egg.model.VO;

import java.io.Serializable;

/**
 * @author cdt
 * @Description 微信js-sdk签名实体
 * @date: 2018/3/28 17:00
 */
public class WxSignVo implements Serializable {
    private static final long serialVersionUID = -5892243919034845036L;
    /**
     * APPID
     */
    private String appId;
    /**
     *  生成签名的时间戳
     */
    private Long timestamp;
    /**
     *  生成签名的随机串
     */
    private String nonceStr;
    /**
     * 签名
     */
    private String signature;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"appId\":\"")
                .append(appId).append('\"');
        sb.append(",\"timestamp\":")
                .append(timestamp);
        sb.append(",\"nonceStr\":\"")
                .append(nonceStr).append('\"');
        sb.append(",\"signature\":\"")
                .append(signature).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
