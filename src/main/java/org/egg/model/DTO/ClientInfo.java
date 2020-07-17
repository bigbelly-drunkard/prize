package org.egg.model.DTO;

import org.egg.enums.ClientChromeEnum;
import org.egg.enums.ClientSourceEnum;

import java.io.Serializable;

/**
 * @author cdt
 * @Description 客户端信息
 * @date: 2018/3/29 20:13
 */
public class ClientInfo implements Serializable {
    private String ip;
    private String userAgent;
    private String host;
    /**
     * 请求来源
     */
    private ClientSourceEnum clientSourceEnum;
    /**
     * 是否微信内核
     */
    private ClientChromeEnum clientChromeEnum;
    /**
     *是否来自APP的请求
     */
    private Boolean appFlag;
    /**
     *是否来自miniProgram的请求
     */
    private boolean miniProgramFlag;
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    public ClientSourceEnum getClientSourceEnum() {
        return clientSourceEnum;
    }

    public void setClientSourceEnum(ClientSourceEnum clientSourceEnum) {
        this.clientSourceEnum = clientSourceEnum;
    }

    public ClientChromeEnum getClientChromeEnum() {
        return clientChromeEnum;
    }

    public void setClientChromeEnum(ClientChromeEnum clientChromeEnum) {
        this.clientChromeEnum = clientChromeEnum;
    }

    public Boolean getAppFlag() {
        return appFlag;
    }

    public void setAppFlag(Boolean appFlag) {
        this.appFlag = appFlag;
    }

    public boolean getMiniProgramFlag() {
        return miniProgramFlag;
    }

    public void setMiniProgramFlag(boolean miniProgramFlag) {
        this.miniProgramFlag = miniProgramFlag;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"ip\":\"")
                .append(ip).append('\"');
        sb.append(",\"userAgent\":\"")
                .append(userAgent).append('\"');
        sb.append(",\"host\":\"")
                .append(host).append('\"');
        sb.append(",\"clientSourceEnum\":")
                .append(clientSourceEnum);
        sb.append(",\"clientChromeEnum\":")
                .append(clientChromeEnum);
        sb.append(",\"appFlag\":")
                .append(appFlag);
        sb.append(",\"miniProgramFlag\":")
                .append(miniProgramFlag);
        sb.append('}');
        return sb.toString();
    }
}
