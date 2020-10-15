package org.egg.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.egg.biz.LoadFactorBiz;
import org.egg.exception.CommonException;
import org.egg.integration.wx.WxCommonApi;
import org.egg.model.DO.PayRecord;
import org.egg.model.DTO.PrizeBean;
import org.egg.model.DTO.WxCompanyPayRequestDto;
import org.egg.response.BaseResult;
import org.egg.utils.CustomerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author dataochen
 * @Description 随机红包奖励 最低一分钱
 * @date: 2020/8/4 9:50
 */
@Component(value = "RRP")
@Slf4j
public class RrpSendPrizeHandler extends AbstractSendPrize {
    @Autowired
    private WxCommonApi wxCommonApi;
    @Autowired
    private LoadFactorBiz loadFactorBiz;

    @Override
    public void sendPrizeHandler(String customerId, PrizeBean prizeBean, PayRecord payRecord) {
        log.info("发送随机红包，customerId={},prizeBean={},payRecord={}", customerId,
                JSONObject.toJSONString(prizeBean), JSONObject.toJSONString(payRecord));
        WxCompanyPayRequestDto wxCompanyPayRequestDto = new WxCompanyPayRequestDto();
        wxCompanyPayRequestDto.setOutTradeNo(payRecord.getPayNo());
        wxCompanyPayRequestDto.setMiniOpenId(CustomerUtil.getCustomer().getWxMiniOpenId());
        wxCompanyPayRequestDto.setTotalAmount(payRecord.getPayAmount());
        BaseResult result = wxCommonApi.companyPay(wxCompanyPayRequestDto);
        if (!result.isSuccess()) {
            log.warn("发送红包失败，result={}", JSONObject.toJSONString(result));
            throw new CommonException(result);
        }
        log.info("发送随机红包成功");
//        loadFactorBiz.redPackage(getPrice(prizeBean), CustomerUtil.getCustomer().getCustomerNo());
        log.info("扣除红包因子");
    }

    @Override
    public void sendPrizeHandler(String customerId, PrizeBean prizeBean) {

    }

    @Override
    public BigDecimal getPrice(PrizeBean prizeBean) {
        BigDecimal bigDecimal = new BigDecimal(Math.random() + "").multiply(prizeBean.getFactor()).setScale(2,BigDecimal.ROUND_HALF_UP);
        bigDecimal = bigDecimal.compareTo(BigDecimal.ZERO) != 1 ? new BigDecimal("0.3") : bigDecimal;
        log.info("随机金额 bigDecimal={}", bigDecimal);
        prizeBean.setPrizeNum(bigDecimal);
        return bigDecimal;
    }
}
