package org.egg.controller;

import lombok.extern.slf4j.Slf4j;
import org.egg.biz.PayBiz;
import org.egg.model.DO.PayRecord;
import org.egg.model.DTO.WxPrePayResultDto;
import org.egg.model.VO.PayReq;
import org.egg.response.BaseResult;
import org.egg.response.CommonSingleResult;
import org.egg.service.impl.FlowRecordServiceImpl;
import org.egg.utils.ClientUtils;
import org.egg.utils.CustomerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

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
    @Autowired
    private FlowRecordServiceImpl flowRecordService;

    /**
     * 微信小程序支付
     *
     * @param payReq
     * @param request
     * @return
     */
    @PostMapping("/wxMiniPay")
    public CommonSingleResult<WxPrePayResultDto> wxMiniPay(PayReq payReq, HttpServletRequest request) {
        payReq.setClientInfo(ClientUtils.getClientInfo(request));
        CommonSingleResult<WxPrePayResultDto> result = payBiz.wxMiniPay(CustomerUtil.getCustomer().getCustomerNo(), payReq);
        return result;
    }

    /**
     *取消支付单
     * @param request
     * @return
     */
    @PostMapping("/cancelPay/{outTradeNo}")
    public BaseResult cancelPay(@PathVariable(value = "outTradeNo") String outTradeNo, HttpServletRequest request) {
        return payBiz.cancelPay(outTradeNo, CustomerUtil.getCustomer().getCustomerNo());
    }

    /**
     * 查询支付结果
     *
     * @param payNo
     * @param request
     * @return
     */
    @PostMapping("/wxMiniPay/{payNo}")
    public CommonSingleResult<PayRecord> queryPayRes(@PathVariable(value = "payNo") String payNo, HttpServletRequest request) {
        return payBiz.queryPayRes(CustomerUtil.getCustomer().getCustomerNo(), payNo);
    }

    /**
     * 金豆提现
     * 提手续费：
     *
     * @param amount
     * @return
     */
    @PostMapping("/cash/{amount}")
    public BaseResult cash(@PathVariable(value = "amount") BigDecimal amount) {
        return payBiz.cash(CustomerUtil.getCustomer().getCustomerNo(), amount);
    }

    /**
     * 金豆兑换
     */
    @PostMapping("/exchange/{bigDecimal}")
    public BaseResult exchange(@PathVariable(value = "bigDecimal") BigDecimal bigDecimal) {
        return payBiz.exchange(bigDecimal, CustomerUtil.getCustomer().getCustomerNo());
    }

}
