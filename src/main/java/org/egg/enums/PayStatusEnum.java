package org.egg.enums;

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
    ;

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
}
