package org.egg.model.DTO;

/**
 * 微信模板消息反参实体
 * <p>
 * 正常情况是
 * {
 * "errcode":0,
 * "errmsg":"ok",
 * "msgid":200228332
 * }
 * Created by chendatao on 2018/7/27.
 */
public class WxTemplateMsgResDto {

    private String errcode;
    private String errmsg;
    private String msgid;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }
}
