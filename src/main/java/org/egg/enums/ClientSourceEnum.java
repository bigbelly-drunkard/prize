package org.egg.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求来源
 * Created by chendatao on 2018/11/4.
 */
public enum ClientSourceEnum {
    ANDROID(0,"安卓"),
    IOS(1,"苹果"),
    PC(2, "电脑"),
    ;


    private int code;
    private String desc;
    private static Map<Integer, ClientSourceEnum> codeMap = new HashMap<>();
    private static Map<Integer, String> codeDescMap = new HashMap<>();
    static {
        for (ClientSourceEnum item : ClientSourceEnum.values()) {
            codeMap.put(item.getCode(), item);
            codeDescMap.put(item.getCode(), item.getDesc());
        }
    }
    ClientSourceEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ClientSourceEnum getEnumByCode(Integer code) {
        return codeMap.get(code);
    }
    public static Map<Integer, String> getCodeDescMap() {
        return codeDescMap;
    }
}
