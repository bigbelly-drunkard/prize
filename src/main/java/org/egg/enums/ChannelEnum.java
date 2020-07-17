package org.egg.enums;

/**
 * Created by chendatao on 2020/2/23.
 */
public enum ChannelEnum {
    WX("WX","微信平台"),
    WX_MINI("WX_MINI","微信小程序"),
    ;
    private String code;
    private String desc;

    ChannelEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
