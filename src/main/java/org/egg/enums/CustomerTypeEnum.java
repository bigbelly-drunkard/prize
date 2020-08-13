package org.egg.enums;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/31 17:49
 */
public enum CustomerTypeEnum {
    COMMON("COMMON", "普通用户"),
    MEMBER_01("MEMBER_01", "黄金会员"),
//    MEMBER_02("MEMBER_02", "铂金会员"),
    ;

    CustomerTypeEnum(String code, String dec) {
        this.code = code;
        this.dec = dec;
    }

    private String code;
    private String dec;

    public String getCode() {
        return code;
    }

    public String getDec() {
        return dec;
    }
}
