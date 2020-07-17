package org.egg.model.wechat;

import java.io.Serializable;

/**
 * @author cdt
 * @Description
 * 正确样例: { "errcode":0,"errmsg":"ok"}；
 * 错误样例：{ "errcode":40003,"errmsg":"invalid openid"}
 * @date: 2018/1/19 16:12
 */
public class WxBaseResult implements Serializable{
    /**
     *
     */
    private String errorCode;
    /**
     *
     */
    private String errmsg;

    /**
     * 是否成功/有效
     * @return
     */
    public Boolean isSuccess() {
        return "0".equals(errorCode);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
