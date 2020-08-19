package org.egg.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/30 18:33
 */
public enum PayStatusEnum {
    INIT("INIT",""),
    PENDING("PENDING",""),
    SUCCESS("SUCCESS",""),
    FAIL("FAIL",""),
    CANCEL("CANCEL",""),
    ;
    private static Map<String, PayStatusEnum> codeEnumMap = new HashMap<>();
    static {
        for (PayStatusEnum payStatusEnum : PayStatusEnum.values()) {
            codeEnumMap.put(payStatusEnum.getCode(), payStatusEnum);
        }
    }
    PayStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PayStatusEnum getEnumByCode(String code) {
        return codeEnumMap.get(code);
    }
}
