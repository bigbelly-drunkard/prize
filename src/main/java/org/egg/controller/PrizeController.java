package org.egg.controller;

import lombok.extern.slf4j.Slf4j;
import org.egg.biz.PrizeBiz;
import org.egg.model.DTO.PrizeBean;
import org.egg.model.VO.GameTenRes;
import org.egg.response.BaseResult;
import org.egg.response.CommonSingleResult;
import org.egg.utils.CustomerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/3 10:14
 */
@RestController
@RequestMapping("/prize")
@Slf4j
public class PrizeController extends BaseController {
    @Autowired
    private PrizeBiz prizeBiz;

    @PostMapping("/r/{activeNo}")
    public CommonSingleResult<PrizeBean> p(@PathVariable(value = "activeNo") String activeNo) {
        return prizeBiz.p(activeNo, CustomerUtil.getCustomer().getCustomerNo());
    }

    @PostMapping("/g}")
    public CommonSingleResult<GameTenRes> getHit() {
        return prizeBiz.game4TenHit(CustomerUtil.getCustomer().getCustomerNo());
    }

    @PostMapping("/c/{pid}")
    public BaseResult confirmHit(@PathVariable(value = "pid") String pid) {
        return prizeBiz.confirmHit(CustomerUtil.getCustomer().getCustomerNo(), pid);
    }


}
