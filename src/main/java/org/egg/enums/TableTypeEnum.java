package org.egg.enums;

/**
 * @author cdt
 * @Description 表类型
 * @date: 2017/12/4 13:21
 */
public enum TableTypeEnum {
    OPERAT_TABLE("0", "操作记录表"),
    ACCEPT_FLOW_TABLE("1", "收单流水记录表"),
    ACCEPT_ORDER_TABLE("2", "收单记录表"),
    PUBLISH_FLOW_TABLE("3", "发单流水记录表"),
    PUBLISH_ORDER_TABLE("4", "发达记录表"),
    USER_TABLE("5", "用户记录表"),
    FEED_BACK_TABLE("6", "反馈表"),
    FLOW_TOTAL_TABLE("7", "资金流水总表"),
    CREDIT_SOCRE_TABLE("9", "信誉分流水表"),
    BOSS_USER_TABLE("10", "管理员用户表"),
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
