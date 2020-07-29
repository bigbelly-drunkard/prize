package org.egg.biz;

import lombok.extern.slf4j.Slf4j;
import org.egg.cache.LocalCache;
import org.egg.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/29 18:11
 */
@Component
@Slf4j
public class LoadFactorBiz {
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    private LocalCache localCache;

    /**
     * 现金购买商品
     * 负载因子 1:1
     *
     * @param amount 商品金额 单位 元
     */
    public void buyGood(BigDecimal amount, String customerId) {
        localCache.addLoadFactorChangeCache(customerId, amount);
    }
//=============================扣减 区域=========================================================

    /**
     * 提现
     * 负载因子 1:1
     *
     * @param amount     提现金额
     * @param customerId
     */
    public void cash(BigDecimal amount, String customerId) {
        if (amount.compareTo(BigDecimal.ZERO) == 1) {
            amount = amount.negate();
        }
        localCache.addLoadFactorChangeCache(customerId, amount);

    }

    /**
     * 用户获取到现金红包
     * 负载因子 1:1
     *
     * @param amount
     * @param customerId
     */
    public void redPackage(BigDecimal amount, String customerId) {
        if (amount.compareTo(BigDecimal.ZERO) == 1) {
            amount = amount.negate();
        }
        localCache.addLoadFactorChangeCache(customerId, amount);
    }

    /**
     * 用户获得奖品（虚拟、实物）
     * 负载因子 1:1
     *
     * @param amount     商品价格
     * @param customerId
     */
    public void goodReduce(BigDecimal amount, String customerId) {
        if (amount.compareTo(BigDecimal.ZERO) == 1) {
            amount = amount.negate();
        }
        localCache.addLoadFactorChangeCache(customerId, amount);
    }

}
