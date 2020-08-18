package org.egg.biz;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.egg.cache.LocalCache;
import org.egg.enums.CommonErrorEnum;
import org.egg.enums.UserStatusEnum;
import org.egg.exception.CommonException;
import org.egg.model.DO.Customer;
import org.egg.model.VO.CustomerVo;
import org.egg.response.BaseResult;
import org.egg.response.CommonSingleResult;
import org.egg.service.impl.CustomerServiceImpl;
import org.egg.template.BizTemplate;
import org.egg.template.TemplateCallBack;
import org.egg.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
    public CommonSingleResult<Customer> miniLoginFast(String openId) {
        CommonSingleResult<Customer> result = new CommonSingleResult<>();
        bizTemplate.processTX(result, new TemplateCallBack() {
            @Override
            public void doCheck() {

            }

            @Override
            public void doAction() {
                Customer customer = customerService.queryCustomerByWxMiniOpenId(openId);
                if (customer == null) {
                    Customer customer1 = new Customer();
                    customer1.setWxMiniOpenId(openId);
                    customerService.createCustomer(customer1);
                    localCache.setMiniOpenId_user_cache(openId, customer1);
                    result.setData(customer1);
                } else {
                    if (!UserStatusEnum.EFFECT.getCode().equals(customer.getCustomerStatus())) {
                        log.error("用户状态异常 openId={},user={}", openId, JSONObject.toJSONString(customer));
                        throw new CommonException(CommonErrorEnum.ACCOUNT_EXCEPTION);
                    }
                    localCache.setMiniOpenId_user_cache(openId, customer);
                    result.setData(customer);
                }
            }
        });
        return result;
    }

    public CommonSingleResult<CustomerVo> queryCustomer(String cid) {
        log.info("queryCustomer cid={}", cid);
        CommonSingleResult<CustomerVo> result = new CommonSingleResult<>();
        bizTemplate.process(result, new TemplateCallBack() {
            @Override
            public void doCheck() {

            }

            @Override
            public void doAction() {
                CustomerVo customerVo = new CustomerVo();
                Customer customer = customerService.queryCustomerByCustomerId(cid);
                BeanUtil.copyProperties(customer, customerVo);
                result.setData(customerVo);
            }
        });
        log.info("queryCustomer result={}", JSONObject.toJSONString(result));
        return result;
    }

    public BaseResult updateHead(String cid, String headUrl, String nickName) {
        log.info("updateHead {},headUrl={},nickName={}",cid,headUrl,nickName);
        BaseResult result = new BaseResult();
        bizTemplate.process(result, new TemplateCallBack() {
            @Override
            public void doCheck() {

            }

            @Override
            public void doAction() {
                Customer customer = customerService.queryCustomerByCustomerId(cid);
                if (!StringUtils.isEmpty(headUrl)) {
                    customer.setHeadUrl(headUrl);
                }
                if (!StringUtils.isEmpty(nickName)) {
                    customer.setNickName(nickName);
                }
                customerService.updateCustomer(customer);

            }
        });
        log.info("updateHead result={}", JSONObject.toJSONString(result));
        return result;
    }
}
