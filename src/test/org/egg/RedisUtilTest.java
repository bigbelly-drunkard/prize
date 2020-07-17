package org.egg;

import org.egg.integration.redis.RedisUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class RedisUtilTest extends BaseTest {
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void test() throws InterruptedException {
        String lock = "test";
        String value1 = UUID.randomUUID().toString();
        String value2 = UUID.randomUUID().toString();
        Thread thread = new Thread(() -> {
            boolean b = redisUtil.tryLock(lock, value1, 1000, 3000);
            if (!b) {
                System.out.println("没有获取到锁 超时了 结束");
                return;
            }
            try {
                System.out.println("thread1 get lock");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean b1 = redisUtil.releaseLock(lock, value1);
            System.out.println("thread1 b1="+b1);
            System.out.println("thread1 releaseLock thread");


        });
        Thread thread2 = new Thread(() -> {
            boolean b = redisUtil.tryLock(lock, value2, 1000, 3000);
            if (!b) {
                System.out.println("没有获取到锁 超时了 结束");
                return;
            }
            try {
                System.out.println("thread2 get lock");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean b1 = redisUtil.releaseLock(lock, value2);
            System.out.println("thread2 b1="+b1);
            System.out.println("thread2 releaseLock thread");
        });
        thread.start();
        thread2.start();
        thread.join();
        thread2.join();

    }
}