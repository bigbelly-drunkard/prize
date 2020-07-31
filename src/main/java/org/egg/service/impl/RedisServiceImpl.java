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
    /**
     * 可以取消的次数 key前缀
     */
    private static final String COUNT_CANCEL_USER = "COUNT_CANCEL_";
    /**
     * 物流信息 key前缀
     */
    private static final String WULIU = "WULIU_";
    /**
     * 首次关注奖励记录 key前缀
     */
    private static final String SUBSCRIPTION = "SUBSCRIPTION_";
    /**
     * 首次打开小程序奖励记录 key前缀
     */
    private static final String FIRST_MINI = "FIRST_MINI_";
    /**
     * 活动key
     */
    private static final String ACTIVE = "ACTIVE_";
    /**
     * 活动 队伍key
     */
    private static final String ACTIVE_TEAM = "ACTIVE_TEAM_";
    /**
     * 活动 用户key
     */
    private static final String ACTIVE_USER = "ACTIVE_USER_";
    /**
     * 活动 用户冗余key
     */
    private static final String ACTIVE_OPEN = "ACTIVE_OPEN_";
    /**
     * 活动 中奖名单key
     */
    public static final String ACTIVE_PRICE = "ACTIVE_PRICE_";
    /**
     * 生产者key
     */
    private static final String PRODUCT_KEY = "PRODUCT_KEY_";
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 用户当天剩余的取消次数
     * 默认3次
     *
     * @param userNo
     * @return
     */
    public int queryCount4CancelByUser(String userNo) {
//        当天
        String yyyyMMDD = DateUtil.format(new Date(), "yyyyMMDD");
        String key = COUNT_CANCEL_USER + yyyyMMDD + userNo;
        Object o = redisUtil.get(key);
        if (o == null) {
//初始化3次 有效期1天
            redisUtil.set(key, 3, 24 * 60 * 60);
            return 3;
        }
        return (int) o;
    }

    /**
     * 扣减剩余次数
     *
     * @param userNo
     * @return
     */
    public boolean doCancel(String userNo) {
//        初始化下
        queryCount4CancelByUser(userNo);
        String yyyyMMDD = DateUtil.format(new Date(), "yyyyMMDD");
        String key = COUNT_CANCEL_USER + yyyyMMDD + userNo;

        long decr = redisUtil.decr(key, 1);
        if (decr < 0) {
            return false;
        }
        return true;
    }

    public void setWuliuJson(String wuliuJson, String wuliuNo) {
        String key = WULIU + wuliuNo;
        redisUtil.set(key, wuliuJson);
    }

    public String getWuliuJson(String wuliuNo) {
        String key = WULIU + wuliuNo;
        Object o = redisUtil.get(key);
        if (o == null) {
            return "";
        }
        return o.toString();
    }

    /**
     * 是否已经存在数据
     * 关注公众号
     *
     * @param openId
     * @return
     */
    public boolean queryRecord4Subscription(String openId) {
        String key = SUBSCRIPTION + openId;
        Object o = redisUtil.get(key);
        return o == null ? false : true;
    }

    /**
     * 发放奖励
     * 关注公众号
     *
     * @param openId
     * @return
     */
    public boolean sendPrice4Subscription(String openId) {
        String key = SUBSCRIPTION + openId;
        return redisUtil.setNx(openId, true);
    }

    /**
     * 是否已经存在数据
     * 首次打开小程序
     *
     * @param openId
     * @return
     */
    public boolean queryRecord4FirstOpen(String openId) {
        String key = FIRST_MINI + openId;
        Object o = redisUtil.get(key);
        return o == null ? false : true;
    }

    /**
     * 发放奖励
     * 首次打开小程序
     *
     * @param openId
     * @return
     */
    public boolean sendPrice4FirstOpen(String openId) {
        String key = FIRST_MINI + openId;
        return redisUtil.setNx(key, true);
    }

}
