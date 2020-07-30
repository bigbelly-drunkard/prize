package org.egg.biz;

import lombok.extern.slf4j.Slf4j;
import org.egg.enums.ChannelEnum;
import org.egg.enums.CommonErrorEnum;
import org.egg.enums.PayTypeEnum;
import org.egg.exception.CommonException;
import org.egg.integration.wx.WxCommonApi;
import org.egg.model.DO.Customer;
import org.egg.model.DO.PayRecord;
import org.egg.model.DTO.ClientInfo;
import org.egg.model.DTO.WxPrePayRequestDto;
import org.egg.model.DTO.WxPrePayResultDto;
import org.egg.service.impl.CustomerServiceImpl;
import org.egg.service.impl.PayRecordServiceImpl;
import org.egg.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/30 18:31
 */
@Component
@Slf4j
public class PayBiz {
    @Autowired
    private SnowFlake snowFlake;
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    private PayRecordServiceImpl payRecordService;
    @Autowired
    private WxCommonApi wxCommonApi;

    public WxPrePayResultDto wxMiniPay(String customerId, BigDecimal amount, String orderMsg, ClientInfo clientInfo) {
        Customer customer = customerService.queryCustomerByCustomerId(customerId);
        PayRecord payRecord = new PayRecord();
        payRecord.setPayNo(snowFlake.nextId() + "");
        WxPrePayRequestDto wxPrePayRequestDto = new WxPrePayRequestDto();
        wxPrePayRequestDto.setOutTradeNo(payRecord.getPayNo());
        wxPrePayRequestDto.setClientInfo(clientInfo);
        wxPrePayRequestDto.setUserNo(customerId);
        wxPrePayRequestDto.setTotalAmount(amount);
        wxPrePayRequestDto.setMiniOpenId(customer.getWxMiniOpenId());
        WxPrePayResultDto pay = wxCommonApi.pay(wxPrePayRequestDto);
        if (null == pay || StringUtils.isEmpty(pay.getPrepay_id())) {
            log.error("微信小程序支付下单失败");
            throw new CommonException(CommonErrorEnum.WX_PAY_ORDER_ERROR);
        }
        payRecord.setChannelNo(pay.getPrepay_id());
        payRecord.setPayChannel(ChannelEnum.WX_MINI.getCode());
        payRecord.setPayAmount(amount);
        payRecord.setCustomerNo(customerId);
        payRecord.setOrderMsg(orderMsg);
        payRecordService.createPayNo(payRecord, PayTypeEnum.PAY);
        return pay;

    }
}
