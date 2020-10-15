package org.egg.biz;

import lombok.extern.slf4j.Slf4j;
import org.egg.cache.LocalCache;
import org.egg.service.impl.CustomerServiceImpl;
import org.egg.service.impl.RedisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author dataochen
 * @Description 出：
 * 入：购买积分礼包 购买会员
 * @date: 2020/7/29 18:11
 */
@Component
@Slf4j
public class LoadFactorBiz {
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    private LocalCache localCache;
    @Autowired
    private RedisServiceImpl redisService;

    /**
     * 现金购买商品
     * 负载因子 1:1
     *
     * @param amount 商品金额 单位 元
     */
//    public void buyGood(BigDecimal amount, String customerId) {
////        localCache.addAmountChangeCache(customerId, amount);
////        changeAllAmount(amount);
//    }
//=============================扣减 区域=========================================================

    /**
     * 提现
     * 负载因子 1:1
     *
     * @param amount     提现金额
     * @param customerId
     */
//    public void cash(BigDecimal amount, String customerId) {
//        if (amount.compareTo(BigDecimal.ZERO) == 1) {
//            amount = amount.negate();
//        }
////        localCache.addAmountChangeCache(customerId, amount);
////        changeAllAmount(amount);
//
//    }

    /**
     * 用户获取到现金红包
     * 负载因子 1:1
     *
     * @param amount
     * @param customerId
     */
//    public void redPackage(BigDecimal amount, String customerId) {
//        if (amount.compareTo(BigDecimal.ZERO) == 1) {
//            amount = amount.negate();
//        }
//        localCache.addAmountChangeCache(customerId, amount);
//        changeAllAmount(amount);
//    }

    /**
     * 用户获得奖品（虚拟、实物）
     * 负载因子 1:1
     *
     * @param amount     商品价格
     * @param customerId
     */
//    public void goodReduce(BigDecimal amount, String customerId) {
//        if (amount.compareTo(BigDecimal.ZERO) == 1) {
//            amount = amount.negate();
//        }
//        localCache.addAmountChangeCache(customerId, amount);
//        changeAllAmount(amount);
//    }

    /**
     * 金豆奖品
     * 负载因子10:1
     *
     * @param amount     负载因子
     * @param customerId
     */
//    public void goldSend(BigDecimal amount, String customerId) {
//        if (amount.compareTo(BigDecimal.ZERO) == 1) {
//            amount = amount.negate();
//        }
//        BigDecimal divide = amount.divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);
////        localCache.addAmountChangeCache(customerId, divide);
////        changeAllAmount(divide);
//    }

    /**
     * 积分抽
     *
     * @param scoreAmount 使用积分数量
     * @param prizeAmount 奖品价值 单位 元
     *                    10:1
     */
    public void play4Score(BigDecimal scoreAmount, BigDecimal prizeAmount, String customerId) {
        BigDecimal divide = scoreAmount.divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal benefit = divide.subtract(prizeAmount);
        localCache.addAmountChangeCache(customerId, benefit);
        changeAllAmount(benefit);
    }

    /**
     * 金豆抽
     *
     * @param scoreAmount 使用积分数量
     * @param prizeAmount 奖品价值 单位 元
     *                    10:1
     */
    public void play4Gold(BigDecimal scoreAmount, BigDecimal prizeAmount, String customerId) {
        BigDecimal divide = scoreAmount.divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal benefit = divide.subtract(prizeAmount);
        localCache.addAmountChangeCache(customerId, benefit);
        changeAllAmount(benefit);
    }

    /**
     * 提现手续费
     * @param fee 手续费 单位 元
     */
    public void cash4Fee(BigDecimal fee, String customerId) {
        localCache.addAmountChangeCache(customerId, fee);
        changeAllAmount(fee);
    }


    /**
     * 修改总盈利值
     *
     * @param amount
     */
    private void changeAllAmount(BigDecimal amount) {
        redisService.addAmount(amount);
    }

}
