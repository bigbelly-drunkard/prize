package org.egg.utils;

import org.apache.commons.lang3.StringUtils;
import org.egg.enums.TableTypeEnum;

import java.util.Random;

/**
 * @author cdt
 * @Description id生成器
 * @date: 2017/11/14 17:53
 */
public class IdMarkUtil {
    private static final long start = 1583225611000L;
    private static SnowFlake snowFlake;
    static{
         snowFlake = new SnowFlake(1, 1);

    }
    /**
     * 唯一标识
     * @return
     */
    @Deprecated
    public static String getUniqId(TableTypeEnum tableTypeEnum) {

        Random random = new Random();
        long l = System.currentTimeMillis() - start;
        return new StringBuilder(tableTypeEnum.getCode()).append(l).append(String.format("%04d", Integer
                .valueOf(StringUtils.left(String.valueOf(Math.abs(random.nextInt())), 4)))).toString();
    }

    public static String getUuid(TableTypeEnum tableTypeEnum) {
        return tableTypeEnum.getCode()+snowFlake.nextId()+"";
    }

    public static void main(String[] args) {
    }

    /**
     * 生产7位的代理推荐码
     * @return
     */
    public static String getAgentCode() {
        Double s = Math.random() * 1000000 + 1000000;
        return ""+s.intValue();
    }


}
