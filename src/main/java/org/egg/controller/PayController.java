package org.egg.controller;

import lombok.extern.slf4j.Slf4j;
import org.egg.biz.PayBiz;
import org.egg.model.DO.PayRecord;
import org.egg.model.DTO.WxPrePayResultDto;
import org.egg.model.VO.PayReq;
import org.egg.response.CommonSingleResult;
import org.egg.utils.ClientUtils;
import org.egg.utils.CustomerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/30 18:27
 */
@RestController
@RequestMapping("/p")
@Slf4j
public class PayController {
    @Autowired
    private PayBiz payBiz;

    @PostMapping("/wxMiniPay")
    public CommonSingleResult<WxPrePayResultDto> wxMiniPay(PayReq payReq, HttpServletRequest request) {
        payReq.setClientInfo(ClientUtils.getClientInfo(request));
        CommonSingleResult<WxPrePayResultDto> result = payBiz.wxMiniPay(CustomerUtil.getCustomer().getCustomerNo(), payReq);
        return result;
    }

    @PostMapping("/wxMiniPay/{payNo}")
    public CommonSingleResult<PayRecord> queryPayRes(@PathVariable(value = "payNo") String payNo, HttpServletRequest request) {
        return payBiz.queryPayRes(CustomerUtil.getCustomer().getCustomerNo(), payNo);
    }


}
