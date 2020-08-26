package org.egg.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.egg.cache.LocalCache;
import org.egg.enums.CustomerTypeEnum;
import org.egg.enums.TableTypeEnum;
import org.egg.enums.UserStatusEnum;
import org.egg.mapper.CustomerMapper;
import org.egg.mapper.CustomerMapperExt;
import org.egg.model.DO.Customer;
import org.egg.model.DO.CustomerExample;
import org.egg.utils.DateUtil;
import org.egg.utils.IdMarkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/29 17:12
 */
@Component
@Slf4j
public class CustomerServiceImpl {
    @Autowired
    private CustomerMapperExt customerMapperExt;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private LocalCache localCache;
    @Autowired
    private RedisServiceImpl redisService;


    /**
     * 比较负载因子
     * 用于控制资损
     * 1.比较奖金池是否满足
     * 2.计算用户负载因子 算法：使用cos函数绝对值 [0,1]，使用cos绝对值来计算此次奖品是否可以再次命中
     *
     * @param value      价值
     * @param customerId
     * @param sync
     * @return
     */
    public boolean checkLoadFactor(BigDecimal value, String customerId, boolean sync) {
        boolean b = redisService.checkAmountAll(value);
        if (!b) {
            log.info("奖金池不满足此奖品 value={}", value);
//            throw new CommonException("MOCK");
            return false;
        }
//        计算用户负载因子 算法：用户因子越大 中奖奖励越小 最后趋近为0
        if (sync) {
//             同步累计缓存中的因子值
            sumLoadFactor4Cache(customerId);
        }
        CustomerExample customerExample = new CustomerExample();
        CustomerExample.Criteria criteria = customerExample.createCriteria();
        criteria.andCustomerNoEqualTo(customerId);
        List<Customer> customers = customerMapper.selectByExample(customerExample);
        Customer customer = customers.get(0);
        BigDecimal loadFactor = customer.getLoadFactor() == null ? BigDecimal.ZERO : customer.getLoadFactor();
//        https://zh.numberempire.com/graphingcalculator.php 函数绘图可视化 100*cos(x/1000)
        double cos = Math.cos((loadFactor.doubleValue()%1000) / 1000);
        if (cos < -1) {
            cos = -cos;
        }
        log.debug("cos={}",cos);
//        添加奖金池因子 奖金池越大 几率越大 反正越小 [1,0.8]
        double v = (0.2 * (redisService.getWEEK_LAST_POOL().get()%10000 )/ ( 10000)) + 0.8;
        boolean b1 = Math.random() < cos * v;
        return b1;
    }

    /**
     * 累加负载因子
     */
    public void sumLoadFactor4Cache(String customerId) {

        ConcurrentHashMap<String, List<BigDecimal>> amount_change_cache = localCache.getAmount_change_cache();
        if (null == customerId) {
            amount_change_cache.forEach((key, value) -> {
                BigDecimal reduce = value.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                if (reduce.compareTo(BigDecimal.ZERO)!=1) {
                    return;
                }
                value.clear();
                changeCustomerLoadFactor(key, reduce);
            });
        } else {
            List<BigDecimal> bigDecimals = amount_change_cache.get(customerId);
            if (CollectionUtils.isEmpty(bigDecimals)) {
                return;
            }
            BigDecimal reduce = bigDecimals.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            bigDecimals.clear();
            changeCustomerLoadFactor(customerId, reduce);
        }
    }

    public Customer queryCustomerByCustomerId(String cid) {
        CustomerExample customerExample = new CustomerExample();
        CustomerExample.Criteria criteria = customerExample.createCriteria();
        criteria.andCustomerNoEqualTo(cid);
        List<Customer> customers = customerMapper.selectByExample(customerExample);
        if (CollectionUtils.isEmpty(customers)) {
            return null;
        }
        Customer customer = customers.get(0);
        return customer;
    }

    public Customer queryCustomerByWxMiniOpenId(String miniOpenId) {
        CustomerExample customerExample = new CustomerExample();
        CustomerExample.Criteria criteria = customerExample.createCriteria();
        criteria.andWxMiniOpenIdEqualTo(miniOpenId);
        List<Customer> customers = customerMapper.selectByExample(customerExample);
        if (CollectionUtils.isEmpty(customers)) {
            return null;
        }
        Customer customer = customers.get(0);
        return customer;
    }

    public void createCustomer(Customer customer) {
        customer.setCustomerStatus(UserStatusEnum.EFFECT.getCode());
        customer.setCreatedDate(new Date());
        customer.setCustomerType(CustomerTypeEnum.COMMON.getCode());
        customer.setCustomerNo(IdMarkUtil.getUuid(TableTypeEnum.CUSTOMER));
        customer.setGold(BigDecimal.ZERO);
        customer.setScore(BigDecimal.ZERO);
        customer.setLoadFactor(BigDecimal.ZERO);
        customerMapper.insertSelective(customer);
    }

    /**
     * 根据id来更新
     *
     * @param customer
     */
    public void updateCustomer(Customer customer) {
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 暂时只有普通会员
     * 1.查询是否是会员  如果是 追加时长 否则初始化
     *
     * @param cid
     * @param day
     */
    public void updateMember(String cid, int day) {
        Customer customer = queryCustomerByCustomerId(cid);
        if (CustomerTypeEnum.MEMBER_01.getCode().equals(customer.getCustomerType())) {
            Date date = DateUtil.addDay(day, customer.getMemberExpire());
            customer.setMemberExpire(date);
        } else {
            customer.setCustomerType(CustomerTypeEnum.MEMBER_01.getCode());
            customer.setMemberExpire(DateUtil.addDay(day));
        }
        customerMapper.updateByPrimaryKey(customer);
    }
//    ========================================= private 区域 ============================================================

    /**
     * 改变用户的中奖核心负载因子
     *
     * @param customerId
     */
    private void changeCustomerLoadFactor(String customerId, BigDecimal addLoadFactor) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>(2);
        stringObjectHashMap.put("customerId", customerId);
        stringObjectHashMap.put("addLoadFactor", addLoadFactor);
        customerMapperExt.updateLoadFactor(stringObjectHashMap);
    }
}
