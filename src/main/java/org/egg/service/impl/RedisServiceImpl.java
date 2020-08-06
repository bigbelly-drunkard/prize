package org.egg.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.egg.integration.redis.RedisUtil;
import org.egg.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by chendatao on 2020/2/20.
 */
@Component
@Slf4j
public class RedisServiceImpl {

    @Autowired
    private RedisUtil redisUtil;

    private static final String KEY_01 = "QD";
    private static final String KEY_02 = "DT";
    private static final String KEY_03 = "FX";

    /**
     * 检查每日的key是否存在
     *
     * @param key
     * @return
     */
    private boolean checkKey4Day(String key, String customerId) {
        Date date = new Date();
        String format = DateUtil.format(date, DateUtil.DMY);
        String s = key + format + customerId;
        return redisUtil.setNx(s, customerId);
    }

    public boolean checkCustomer4Day1(String customerId) {
        return checkKey4Day(KEY_01, customerId);
    }

    public boolean checkCustomer4Day2(String customerId) {
        return checkKey4Day(KEY_02, customerId);
    }

    public boolean checkCustomer4Day3(String customerId) {
        return checkKey4Day(KEY_03, customerId);
    }

}
