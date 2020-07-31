package org.egg.enums;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 *  0:正常 1：注销 2：冻结
 * Created by chendatao on 2017/12/4.
 */
public enum UserStatusEnum {
    EFFECT(0,"EFFECT","正常"),
    CANCEL(1,"CANCEL","注销"),
    FREEZE(2,"FREEZE","冻结"),
    ;

    UserStatusEnum(int index, String code, String desc) {
        this.index = index;
        this.code = code;
        this.desc = desc;
    }
    private static final Map<String, String> indexDescMap = new HashedMap();

    static {
        for (UserStatusEnum item : UserStatusEnum.values()) {
            indexDescMap.put(item.getCode(), item.getDesc());
        }
    }

    private int index;
    private String code;
    private String desc;

    public int getIndex() {
        return index;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
    public static Map<String, String> getIndexDescMap() {
        return indexDescMap;
    }

    public static String getDescByCode(String index) {
        return indexDescMap.get(index);
    }
}
