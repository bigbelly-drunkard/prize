package org.egg.controller;

import lombok.extern.slf4j.Slf4j;
import org.egg.biz.MsgBiz;
import org.egg.biz.RedisBiz;
import org.egg.model.VO.MsgRes;
import org.egg.response.BaseResult;
import org.egg.response.CommonListResult;
import org.egg.response.CommonSingleResult;
import org.egg.utils.CustomerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
    @Autowired
    private MsgBiz msgBiz;

    /**
     * 签到
     *
     * @return
     */
    @PostMapping("/qd")
    public BaseResult qd() {
        return redisBiz.qd(CustomerUtil.getCustomer().getCustomerNo());
    }

    /**
     * 答题
     *
     * @return
     */
    @PostMapping("/dt")
    public BaseResult dt() {
        return redisBiz.dt(CustomerUtil.getCustomer().getCustomerNo());
    }

    /**
     * 分享
     *
     * @return
     */
    @PostMapping("/share")
    public BaseResult share() {
        return redisBiz.share(CustomerUtil.getCustomer().getCustomerNo());
    }

    /**
     * 查询消息
     *
     * @param type
     * @return
     */
    @PostMapping("/qml/{type}")
    public CommonListResult<MsgRes> queryMsgList(@PathVariable(value = "type") int type) {
        return msgBiz.queryMsgList(type);
    }

    @RequestMapping("/qsa")
    public CommonSingleResult<Boolean> querySwitch4Approve(Boolean flag) {
        CommonSingleResult<Boolean> aSwitch = redisBiz.getSwitch(flag);
        return aSwitch;
    }

    @GetMapping("/bossPool/{amount}")
    public BigDecimal bossPool(@PathVariable(value = "amount") BigDecimal amount) {
        return redisBiz.bossPool(amount);
    }
}
