package org.egg.enums;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/28 20:37
 */
public enum FlowRecordTypeEnum {
    SCORE("SCORE", "积分"),
    GOLD("GOLD", "金豆"),;
    private String code;
    private String desc;

    FlowRecordTypeEnum(String code, String desc) {
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
