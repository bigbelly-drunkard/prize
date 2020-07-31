package org.egg.cache;

import org.egg.model.DO.Customer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chendatao on 2020/2/11.
 * 本地缓存
 */
@Component
public class LocalCache {
    /**
     * 所有miniopenid 登录session sessionId就是openId
     * 登录时录入
     * 暂无session超时设置
     */
    private ConcurrentHashMap<String, Customer> miniOpenId_user_cache = new ConcurrentHashMap<>();
    /**
     * 所有用户的负载因子变动
     */
    private ConcurrentHashMap<String, List<BigDecimal>> loadFactor_change_cache = new ConcurrentHashMap<>();
    /**
     * msg集合 LRU
     * k:时间
     * value:msg 内容
     */
    private Map<String, String> msgMap = new LinkedHashMap<String, String>(100, .75F,
            false) {

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            boolean tooBig = size() > 10;

            if (tooBig) {
                System.out.println("最近最少使用的key=" + eldest.getKey());
            }
            return tooBig;
        }
    };

    public ConcurrentHashMap<String, Customer> getMiniOpenId_user_cache() {
        return miniOpenId_user_cache;
    }


    public Customer getCustomerByMiniOpenId(String openId) {
        return miniOpenId_user_cache.get(openId);
    }

    public void setMiniOpenId_user_cache(String openId, Customer user) {
        miniOpenId_user_cache.put(openId, user);
    }

    public ConcurrentHashMap<String, List<BigDecimal>> getLoadFactor_change_cache() {
        return loadFactor_change_cache;
    }

    public void addLoadFactorChangeCache(String customerId, BigDecimal value) {
        List<BigDecimal> bigDecimals = loadFactor_change_cache.get(customerId);
        if (null == bigDecimals) {
            loadFactor_change_cache.put(customerId, new LinkedList<>());
            return;
        }
        bigDecimals.add(value);
    }

    public void addMsg(String msg) {
        msgMap.put(System.currentTimeMillis() + "", msg);
    }

    public Map<String, String> getMsgMap() {
        return msgMap;
    }
}
