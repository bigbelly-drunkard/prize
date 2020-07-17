package org.egg.service.impl;

import org.egg.BaseTest;
import org.egg.integration.redis.RedisUtil;
import org.egg.model.DTO.ActiveOpenId;
import org.egg.utils.BeanUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author dataochen
 * @Description
 * @date: 2020/3/6 16:39
 */
public class RedisServiceImplTest extends BaseTest {
    @Autowired
    private RedisServiceImpl redisService;
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void tt() throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        String key = "ACTIVE_OPEN_002";
        ActiveOpenId activeOpenId = new ActiveOpenId();
        activeOpenId.setOrderNos(new ArrayList<>());
        activeOpenId.setTeamNo("1101205015236608");
        activeOpenId.setUserNo("002");
        Map<String, Object> stringObjectMap = BeanUtil.transBean2Map(activeOpenId);
        redisUtil.hmset(key, stringObjectMap);
    }

    @Test
    public void addOrder(){
        redisService.addOrder("002","22311");
    }
}