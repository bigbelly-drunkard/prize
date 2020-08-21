package org.egg.biz;

import org.egg.BaseTest;
import org.egg.integration.redis.RedisUtil;
import org.egg.mapper.CustomerMapper;
import org.egg.mapper.CustomerMapperExt;
import org.egg.model.DO.Customer;
import org.egg.model.VO.PrizeVo;
import org.egg.response.CommonSingleResult;
import org.egg.utils.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/19 17:57
 */
public class PrizeBizTest extends BaseTest {
    @Autowired
    private PrizeBiz prizeBiz;
    @Autowired
    private CustomerMapperExt customerMapperExt;
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void p() throws Exception {
        while (true) {

            CommonSingleResult<PrizeVo> a = prizeBiz.p("A", "160963614995124224");
            if (!a.isSuccess()) {
                return;
            }
        }
    }

    /**
     * 造数据
     * 1.
     */
    @Test
    public void buyGood() {
        clearZero();
        BigDecimal amount = new BigDecimal("100");
        String customerId = "160963614995124224";
        HashMap<String, Object> stringObjectHashMap = new HashMap<>(2);
        stringObjectHashMap.put("customerId", customerId);
        stringObjectHashMap.put("addLoadFactor", amount);
        customerMapperExt.updateLoadFactor(stringObjectHashMap);
        String format = DateUtil.format(DateUtil.addDay(-1), DateUtil.DMY);
        long incr = redisUtil.incr("AMOUNT_DAY_" + format, amount.multiply(new BigDecimal("100")).intValue());
//        累计总净资产
        redisUtil.incr("AMOUNT_ALL", amount.multiply(new BigDecimal("100")).intValue());

    }

    @Test
    public void clearZero() {
        String customerId = "160963614995124224";
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setGold(BigDecimal.ZERO);
//        customer.setScore(BigDecimal.ZERO);
        customer.setLoadFactor(BigDecimal.ZERO);
        customerMapper.updateByPrimaryKeySelective(customer);
        redisUtil.set("AMOUNT_ALL", 0);
        Set keys = redisUtil.keys("AMOUNT_DAY_*");
        keys.forEach(x -> {
            redisUtil.set(x.toString(), 0);
        });
    }

}