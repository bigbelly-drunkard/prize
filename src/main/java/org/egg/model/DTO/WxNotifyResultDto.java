package org.egg.model.DTO;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 微信通知返回结果集
 * Created by chendatao on 2018/7/26.
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.NONE)
public class WxNotifyResultDto  {
    public WxNotifyResultDto(String return_code, String return_msg) {
        this.return_code = return_code;
        this.return_msg = return_msg;
    }

    @XmlElement
    private String return_code;
    @XmlElement
    private String return_msg;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"return_code\":\"")
                .append(return_code).append('\"');
        sb.append(",\"return_msg\":\"")
                .append(return_msg).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
