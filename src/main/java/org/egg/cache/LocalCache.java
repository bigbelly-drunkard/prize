package org.egg.cache;

import lombok.extern.slf4j.Slf4j;
import org.egg.model.DO.Customer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chendatao on 2020/2/11.
 * 本地缓存
 */
@Component
@Slf4j
public class LocalCache {


    /**
     * 所有miniopenid 登录session sessionId就是openId
     * 登录时录入
     * 暂无session超时设置
     */
    private ConcurrentHashMap<String, Customer> miniOpenId_user_cache = new ConcurrentHashMap<>();
    /**
     * 所有用户的收益金额变动
     */
    private ConcurrentHashMap<String, List<BigDecimal>> amount_change_cache = new ConcurrentHashMap<>();
    /**
     * msg集合 LRU
     * k:时间
     * value:msg 内容
     */
    private Map<String, String> msgMap = new LinkedHashMap<String, String>(100, .75F,
            false) {

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            boolean tooBig = size() > 70;

            if (tooBig) {
                log.debug("最近最少使用的key=" + eldest.getKey());
            }
            return tooBig;
        }
    };
    /**
     * 抽奖跑马灯消息
     */
    private Map<String, String> msgMapPaoMaDeng = new LinkedHashMap<String, String>(100, .75F,
            false) {

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            boolean tooBig = size() > 70;

            if (tooBig) {
                log.debug("msgMapPaoMaDeng最近最少使用的key=" + eldest.getKey());
            }
            return tooBig;
        }
    };

    public ConcurrentHashMap<String, Customer> getMiniOpenId_user_cache() {
        return miniOpenId_user_cache;
    }


    public Customer getCustomerByMiniOpenId(String openId) {
        if (null == openId) {
            return null;
        }
        return miniOpenId_user_cache.get(openId);
    }

    public void setMiniOpenId_user_cache(String openId, Customer user) {
        miniOpenId_user_cache.put(openId, user);
    }

    public ConcurrentHashMap<String, List<BigDecimal>> getAmount_change_cache() {
        return amount_change_cache;
    }

    public void addAmountChangeCache(String customerId, BigDecimal value) {
        List<BigDecimal> bigDecimals = amount_change_cache.get(customerId);
        if (null == bigDecimals) {
            amount_change_cache.put(customerId, Collections.synchronizedList(new LinkedList<>()));
            return;
        }
        bigDecimals.add(value);
    }

    public void addMsg(String msg) {
        msgMap.put(System.currentTimeMillis() + "", msg);
    }

    public void addMsgPaoMaDeng(String msg) {
        msgMapPaoMaDeng.put(System.currentTimeMillis() + "", msg);
    }

    public Map<String, String> getMsgMap() {
        return msgMap;
    }

    public Map<String, String> getMsgMapPaoMaDeng() {
        return msgMapPaoMaDeng;
    }
}
