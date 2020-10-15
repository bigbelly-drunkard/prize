package org.egg.service.impl;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.extern.slf4j.Slf4j;
import org.egg.exception.CommonException;
import org.egg.integration.redis.RedisUtil;
import org.egg.model.DTO.PrizeBean;
import org.egg.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    private static final String KEY_05 = "WX_OPENID_";
    private static final String KEY_06 = "APPROVE_SWITCH";
    /**
     * 总收益金额 总值
     */
    private static final String AMOUNT_ALL = "AMOUNT_ALL";
    private static final String AMOUNT_DAY = "AMOUNT_DAY_";
    private AtomicDouble WEEK_LAST_POOL = new AtomicDouble(0);


    @PostConstruct
    public void init() {
//        系统重新启动时 需要把今天的负资产加入到上一周内 否则会有资损
        getWeekLast();
    }

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
     * 30% 奖金池占比率
     * 奖金池=上一周的盈利总值*奖金池占比率
     * needAmount 单位：元
     * 单位：分
     *
     * @return
     */
    public boolean checkAmountAll(BigDecimal needAmount) {

        log.debug("奖金池{},need {}", WEEK_LAST_POOL, needAmount);
        return new BigDecimal(WEEK_LAST_POOL.get()).compareTo(needAmount) > -1;

    }

    /**
     * 后台 奖金池额度
     * @param change
     * @return
     */
    public BigDecimal bossPool(BigDecimal change) {
        AtomicDouble week_last_pool = getWEEK_LAST_POOL();
        double v = week_last_pool.get();
        BigDecimal bigDecimal = new BigDecimal(v + "");
        if (null == change) {
            return bigDecimal;
        }
        BigDecimal add = bigDecimal.add(change);
        WEEK_LAST_POOL.set(add.doubleValue());
        return add;
    }

    /**
     * 计算上一周的奖金池 并清空当前天的金额到上一周内
     */
    public void getWeekLast() {
        int weekAmount = 0;
        for (int i = 1; i < 8; i++) {
            Date date = DateUtil.addDay(-i);
            int day = getDay(date);
            weekAmount += day;
        }
        int day = getDay(new Date());
        weekAmount += day;
//        String format = DateUtil.format(new Date(), DateUtil.DMY);
//        redisUtil.set(AMOUNT_DAY + format, 0);
//        一周最少10元 负资产哦 后期去除
        log.debug("weekAmount={}", weekAmount);
        float o = weekAmount / 100f;
        BigDecimal bigDecimal = new BigDecimal("0.3").multiply(new BigDecimal(o)).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal bigDecimal1 = bigDecimal.compareTo(BigDecimal.ZERO) < 1 ? BigDecimal.TEN : bigDecimal;
        WEEK_LAST_POOL.set(bigDecimal1.doubleValue());
    }


    private int getDay(Date date) {
        String format = DateUtil.format(date, DateUtil.DMY);
        Object o1 = redisUtil.get(AMOUNT_DAY + format);
        if (null == o1) {
            redisUtil.set(AMOUNT_DAY + format, 0);
            return 0;
        }
        int o11 = (int) o1;
        return o11;
    }

    /**
     * 增加或减少 当天的盈利值
     * 单位：元
     * redis只支持long 所以单位为分
     *
     * @param amount
     */
    public void addAmount(BigDecimal amount) {
        String format = DateUtil.format(new Date(), DateUtil.DMY);
        double v = WEEK_LAST_POOL.addAndGet(amount.doubleValue());
        if (v < 0) {
            log.error("超了 {}", v);
            throw new CommonException("超了");
        }
        long incr = redisUtil.incr(AMOUNT_DAY + format, amount.multiply(new BigDecimal("100")).intValue());
//        累计总净资产
        redisUtil.incr(AMOUNT_ALL, amount.multiply(new BigDecimal("100")).intValue());
    }

    public AtomicDouble getWEEK_LAST_POOL() {
        return WEEK_LAST_POOL;
    }

    /**
     * 检查用户是否第一次登陆
     *
     * @param openId
     * @return
     */
    public boolean checkFirstLogin(String openId) {
        Object andSet = null;
        try {
            andSet = redisUtil.getAndSet(KEY_05 + openId, new Date());
            if (null == andSet) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 默认关闭
     * 获取开关
     *
     * @return
     */
    public boolean getSwitch() {
        Object o = redisUtil.get(KEY_06);
        if (null == o) {
            return false;
        }
        return Boolean.valueOf(o.toString());
    }

    public boolean setSwitch(boolean flag) {
        return redisUtil.set(KEY_06, flag);
    }

}
