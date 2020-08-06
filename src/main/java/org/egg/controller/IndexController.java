package org.egg.controller;

import lombok.extern.slf4j.Slf4j;
import org.egg.biz.RedisBiz;
import org.egg.response.BaseResult;
import org.egg.utils.CustomerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/5 18:08
 */
@RestController
@RequestMapping("/i")
@Slf4j
public class IndexController {
    @Autowired
    private RedisBiz redisBiz;

    @PostMapping("/qd")
    public BaseResult qd() {
        return redisBiz.qd(CustomerUtil.getCustomer().getCustomerNo());
    }

    @PostMapping("/dt")
    public BaseResult dt() {
        return redisBiz.dt(CustomerUtil.getCustomer().getCustomerNo());
    }

    @PostMapping("/share")
    public BaseResult share() {
        return redisBiz.share(CustomerUtil.getCustomer().getCustomerNo());
    }
}
