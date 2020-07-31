package org.egg.biz;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.egg.cache.LocalCache;
import org.egg.enums.CommonErrorEnum;
import org.egg.enums.UserStatusEnum;
import org.egg.exception.CommonException;
import org.egg.model.DO.Customer;
import org.egg.model.wechat.AccessTokenMiniResult;
import org.egg.response.CommonSingleResult;
import org.egg.service.impl.CustomerServiceImpl;
import org.egg.template.BizTemplate;
import org.egg.template.TemplateCallBack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/31 17:33
 */
@Component
@Slf4j
public class CustomerBiz {
    @Autowired
    private BizTemplate bizTemplate;
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    private LocalCache localCache;

    /**
     * 小程序 快速登录
     * 如果不存在openid 则新增用户
     *
     * @return
     */
    public CommonSingleResult<Customer> miniLoginFast(AccessTokenMiniResult accessTokenMiniResult) {
        CommonSingleResult<Customer> result = new CommonSingleResult<>();
        bizTemplate.processTX(result, new TemplateCallBack() {
            @Override
            public void doCheck() {

            }

            @Override
            public void doAction() {
                Customer customer = customerService.queryCustomerByWxMiniOpenId(accessTokenMiniResult.getOpenid());
                if (customer == null) {
                    Customer customer1 = new Customer();
                    customer1.setWxMiniOpenId(accessTokenMiniResult.getOpenid());
                    customerService.createCustomer(customer1);
                    localCache.setMiniOpenId_user_cache(accessTokenMiniResult.getOpenid(), customer1);
                    result.setData(customer1);
                } else {
                    if (!UserStatusEnum.EFFECT.getCode().equals(customer.getCustomerStatus())) {
                        log.error("用户状态异常 openId={},user={}", accessTokenMiniResult.getOpenid(), JSONObject.toJSONString(customer));
                        throw new CommonException(CommonErrorEnum.ACCOUNT_EXCEPTION);
                    }
                    result.setData(customer);
                }
            }
        });
        return result;
    }
}
