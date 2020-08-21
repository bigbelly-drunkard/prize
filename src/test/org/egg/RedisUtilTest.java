package org.egg;

import org.egg.integration.redis.RedisUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisUtilTest extends BaseTest {
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void test() throws InterruptedException {
        Object o1 = redisUtil.get("cdtTest");
        if (null == o1) {
            System.out.println("cc");
            return;
        }
        System.out.println("end");
    }
}