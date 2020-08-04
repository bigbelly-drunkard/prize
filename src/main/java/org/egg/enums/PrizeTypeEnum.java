package org.egg.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/4 9:37
 */
public enum PrizeTypeEnum {
    RED_PACKAGE("RP", "红包"),
    RANDOM_RED_PACKAGE("RRP", "随机红包"),
    ZREO("ZR", "未中奖"),
    COUPON("CP", "代金券"),
    ;
    private static Map<String, PrizeTypeEnum> codeEnum = new HashMap<>(4);
    static {
        for (PrizeTypeEnum prizeTypeEnum : PrizeTypeEnum.values()) {
            codeEnum.put(prizeTypeEnum.getCode(), prizeTypeEnum);
        }
    }
    PrizeTypeEnum(String code, String desc) {
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

    public PrizeTypeEnum getEnumByCode(String code) {
        return codeEnum.get(code);
    }
}
