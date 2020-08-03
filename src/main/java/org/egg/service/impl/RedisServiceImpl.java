package org.egg.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.egg.integration.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by chendatao on 2020/2/20.
 */
@Component
@Slf4j
public class RedisServiceImpl {
    /**
     * 活动key
     */
    private static final String ACTIVE = "ACTIVE_";
    /**
     * 生产者key
     */
    private static final String PRODUCT_KEY = "PRODUCT_KEY_";
    @Autowired
    private RedisUtil redisUtil;



}
