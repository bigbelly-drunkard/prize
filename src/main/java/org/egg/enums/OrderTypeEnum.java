package org.egg.enums;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/30 18:33
 */
public enum OrderTypeEnum {
    MEMBER_7("MEMBER_7", "7天会员",new BigDecimal("9.9")),
    MEMBER_30("MEMBER_30", "包月会员",new BigDecimal("29.9")),
    MEMBER_365("MEMBER_365", "包年会员",new BigDecimal("299")),
    MEMBER_1095("MEMBER_1095", "3年会员",new BigDecimal("889")),
    SCORE("SCORE", "积分礼包",null),;
    private static Map<String, OrderTypeEnum> codeEnumMap = new HashMap<>(8);
    static {
        for (OrderTypeEnum orderTypeEnum : OrderTypeEnum.values()) {
            codeEnumMap.put(orderTypeEnum.getCode(), orderTypeEnum);
        }
    }

    OrderTypeEnum(String code, String desc, BigDecimal prize) {
        this.code = code;
        this.desc = desc;
        this.prize = prize;
    }

    private String code;
    private String desc;
    private BigDecimal prize;

    public BigDecimal getPrize() {
        return prize;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static OrderTypeEnum getEnumByCode(String code) {
        return codeEnumMap.get(code);
    }
}
