package org.egg.cache;

import org.egg.model.DO.User;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chendatao on 2020/2/11.
 * 本地缓存
 */
@Component
public class LocalCache {

    private ConcurrentHashMap<String, User> miniOpenId_user_cache = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, User> getMiniOpenId_user_cache() {
        return miniOpenId_user_cache;
    }

    public User getUserByMiniOpenId(String openId) {
        return miniOpenId_user_cache.get(openId);
    }

    public void setMiniOpenId_user_cache(String openId, User user) {
        miniOpenId_user_cache.put(openId, user);
    }
}
