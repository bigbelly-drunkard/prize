package org.egg.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求来源
 * Created by chendatao on 2018/11/4.
 */
public enum ClientChromeEnum {
    WX(0, "微信内核"),
    OTHER(1, "非微信内核"),;


    private int code;
    private String desc;
    private static Map<Integer, ClientChromeEnum> codeMap = new HashMap<>();
    private static Map<Integer, String> codeDescMap = new HashMap<>();

    static {
        for (ClientChromeEnum item : ClientChromeEnum.values()) {
            codeMap.put(item.getCode(), item);
            codeDescMap.put(item.getCode(), item.getDesc());
        }
    }

    ClientChromeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ClientChromeEnum getEnumByCode(Integer code) {
        return codeMap.get(code);
    }

    public static Map<Integer, String> getCodeDescMap() {
        return codeDescMap;
    }
}
