package org.egg.enums;

/**
 * @author cdt
 * @Description 表类型
 * @date: 2017/12/4 13:21
 */
public enum TableTypeEnum {
    CUSTOMER("1", ""),
    FLOW_RECORD("2", ""),
    PAY_RECORD("3", ""),
    OTHER("4", ""),
    ;
    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    TableTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
