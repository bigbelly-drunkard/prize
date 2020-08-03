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
import org.egg.utils.IdMarkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


    /**
     * 比较负载因子 是否大于所需因子value
     * 用于控制资损
     *
     * @param value 因子数
     * @param customerId
     * @param sync
     * @return
     */
    public boolean checkLoadFactor(BigDecimal value, String customerId, boolean sync) {
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
        return loadFactor.compareTo(value) == 1;
    }

    /**
     * 累加负载因子
     */
    public void sumLoadFactor4Cache(String customerId) {

        ConcurrentHashMap<String, List<BigDecimal>> loadFactor_change_cache = localCache.getLoadFactor_change_cache();
        if (null == customerId) {
            loadFactor_change_cache.forEach((key, value) -> {
                BigDecimal reduce = value.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                changeCustomerLoadFactor(key, reduce);
            });
        } else {
            List<BigDecimal> bigDecimals = loadFactor_change_cache.get(customerId);
            if (null == bigDecimals) {
                return;
            }
            BigDecimal reduce = bigDecimals.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            changeCustomerLoadFactor(customerId, reduce);
        }
    }

    public Customer queryCustomerByCustomerId(String cid) {
        CustomerExample customerExample = new CustomerExample();
        CustomerExample.Criteria criteria = customerExample.createCriteria();
        criteria.andCustomerNoEqualTo(cid);
        List<Customer> customers = customerMapper.selectByExample(customerExample);
        Customer customer = customers.get(0);
        return customer;
    }

    public Customer queryCustomerByWxMiniOpenId(String miniOpenId) {
        CustomerExample customerExample = new CustomerExample();
        CustomerExample.Criteria criteria = customerExample.createCriteria();
        criteria.andWxMiniOpenIdEqualTo(miniOpenId);
        List<Customer> customers = customerMapper.selectByExample(customerExample);
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

//    ========================================= private 区域 ============================================================

    /**
     * 改变用户的中奖核心负载因子
     *
     * @param customerId
     */
    private void changeCustomerLoadFactor(String customerId, BigDecimal addLoadFactor) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>(3);
        stringObjectHashMap.put("customerId", customerId);
        stringObjectHashMap.put("addLoadFactor", addLoadFactor);
        customerMapperExt.updateLoadFactor(stringObjectHashMap);
    }
}
