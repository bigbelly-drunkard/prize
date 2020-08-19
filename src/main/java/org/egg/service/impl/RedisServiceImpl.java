package org.egg.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.egg.integration.redis.RedisUtil;
import org.egg.model.DTO.PrizeBean;
import org.egg.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
     * pid
     */
    private static final String KEY_04 = "P_";
    /**
     * 总收益金额 总值
     * 奖金池=总收益金额*盈利率
     */
    private static final String AMOUNT_ALL = "AMOUNT_ALL";

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
        boolean b = redisUtil.setNx(s, customerId);
        redisUtil.expire(s, 24 * 3600L);
        return b;
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

    /**
     * pid 有效期5分钟
     *
     * @param customerId
     * @param pid
     */
    public void setPid(String customerId, String pid, PrizeBean prizeBean) {
        String s = KEY_04 + customerId + pid;
        redisUtil.setEx(s, prizeBean, 5 * 60 * 1000L);
    }

    public PrizeBean checkPid(String customerId, String pid) {
        String s = KEY_04 + customerId + pid;
        PrizeBean o = (PrizeBean) redisUtil.get(s);
        return o;
    }

    /**
     * 校验是否超过奖金池
     * 30% 盈利率
     * 奖金池=核心负载因子*盈利率
     * needAmount 单位：元
     * 单位：分
     * @return
     */
    public boolean checkAmountAll(BigDecimal needAmount) {
        int o = (int) redisUtil.get(AMOUNT_ALL)/100;
        BigDecimal bigDecimal = new BigDecimal("0.3").multiply(new BigDecimal(o)).setScale(2, BigDecimal.ROUND_HALF_UP);
        log.debug("奖金池{}",bigDecimal);
        return bigDecimal.compareTo(needAmount) > -1;

    }

    /**
     * 增加或减少 负载因子
     * 目前标准：
     *单位：元
     * redis只支持long 所以单位为分
     * @param amount
     */
    public void addAmount(BigDecimal amount) {
        redisUtil.incr(AMOUNT_ALL, amount.multiply(new BigDecimal("100")).intValue());
    }

}
