package org.egg.controller;

import lombok.extern.slf4j.Slf4j;
import org.egg.biz.CustomerBiz;
import org.egg.model.VO.CustomerVo;
import org.egg.response.BaseResult;
import org.egg.response.CommonSingleResult;
import org.egg.utils.CustomerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/14 15:50
 */
@RestController
@Slf4j
@RequestMapping("/customer")
public class CustomerController extends BaseController {
    @Autowired
    private CustomerBiz customerBiz;

    @PostMapping("/qc")
    public CommonSingleResult<CustomerVo> queryCustomer() {
        return customerBiz.queryCustomer(CustomerUtil.getCustomer().getCustomerNo());
    }

    @PostMapping("/uh")
    public BaseResult updateHead(String headUrl, String nickName) {
        return customerBiz.updateHead(CustomerUtil.getCustomer().getCustomerNo(), headUrl, nickName);
    }
}
