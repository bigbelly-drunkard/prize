package org.egg.biz;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.egg.enums.*;
import org.egg.exception.CommonException;
import org.egg.integration.wx.WxCommonApi;
import org.egg.model.DO.Customer;
import org.egg.model.DO.PayRecord;
import org.egg.model.DTO.WxCompanyPayRequestDto;
import org.egg.model.DTO.WxPrePayRequestDto;
import org.egg.model.DTO.WxPrePayResultDto;
import org.egg.model.DTO.WxQueryPayOrderDto;
import org.egg.model.VO.PayReq;
import org.egg.response.BaseResult;
import org.egg.response.CommonSingleResult;
import org.egg.service.impl.CustomerServiceImpl;
import org.egg.service.impl.FlowRecordServiceImpl;
import org.egg.service.impl.PayRecordServiceImpl;
import org.egg.template.BizTemplate;
import org.egg.template.TemplateCallBack;
import org.egg.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/30 18:31
 */
@Component
@Slf4j
public class PayBiz {
    //    @Autowired
//    private SnowFlake snowFlake;
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    private PayRecordServiceImpl payRecordService;
    @Autowired
    private WxCommonApi wxCommonApi;
    @Autowired
    private BizTemplate bizTemplate;
    @Autowired
    private FlowRecordServiceImpl flowRecordService;
    private List<String> FAIL_CODES = Arrays.asList("CLOSED", "REVOKED", "PAYERROR");

    public CommonSingleResult<WxPrePayResultDto> wxMiniPay(String customerId, PayReq payReq) {
        CommonSingleResult<WxPrePayResultDto> result = new CommonSingleResult<>();
        bizTemplate.process(result, new TemplateCallBack() {
            @Override
            public void doCheck() {
                CheckUtil.isNotNull("payReq", payReq);
                BigDecimal amount = payReq.getAmount();
                if (null == amount || amount.compareTo(BigDecimal.ZERO) == -1) {
                    log.error("支付金额不对 amount={}", amount);
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);
                }
                OrderTypeEnum enumByCode = OrderTypeEnum.getEnumByCode(payReq.getOrderType());
                if (null == enumByCode) {
                    log.error("非法参数");
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);
                }
//                 校验商品价格是否合法
//                会员类 校验金额
                if (OrderTypeEnum.SCORE != enumByCode) {
                    if (enumByCode.getPrize().compareTo(amount) != 1) {
                        log.error("非法参数,下单金额不一致 amount={},prize={}", amount, enumByCode.getPrize());
                        throw new CommonException(CommonErrorEnum.PARAM_ERROR);
                    }
                }


            }

            @Override
            public void doAction() {
                Customer customer = customerService.queryCustomerByCustomerId(customerId);
                PayRecord payRecord = new PayRecord();
                payRecord.setPayNo(IdMarkUtil.getUuid(TableTypeEnum.PAY_RECORD));
                WxPrePayRequestDto wxPrePayRequestDto = new WxPrePayRequestDto();
                wxPrePayRequestDto.setOutTradeNo(payRecord.getPayNo());
                wxPrePayRequestDto.setClientInfo(payReq.getClientInfo());
                wxPrePayRequestDto.setUserNo(customerId);
                wxPrePayRequestDto.setTotalAmount(payReq.getAmount());
                wxPrePayRequestDto.setMiniOpenId(customer.getWxMiniOpenId());
                WxPrePayResultDto pay = wxCommonApi.pay(wxPrePayRequestDto);
                if (null == pay || StringUtils.isEmpty(pay.getPrepay_id())) {
                    log.error("微信小程序支付下单失败");
                    throw new CommonException(CommonErrorEnum.WX_PAY_ORDER_ERROR);
                }
                payRecord.setChannelNo(pay.getPrepay_id());
                payRecord.setPayChannel(ChannelEnum.WX_MINI.getCode());
                payRecord.setPayAmount(payReq.getAmount());
                payRecord.setCustomerNo(customerId);
                payRecord.setOrderMsg(payReq.getOrderMsg());
                payRecord.setOrderType(payReq.getOrderType());
                payRecordService.createPayNo(payRecord, PayTypeEnum.PAY);
                result.setData(pay);
            }
        });

        return result;

    }

    public BaseResult cancelPay(String outTradeNo, String cid) {
        log.info("取消支付单开始{}", outTradeNo);
        BaseResult result = new BaseResult();
        bizTemplate.process(result, new TemplateCallBack() {
            @Override
            public void doCheck() {
                CheckUtil.isNotBlank("outTradeNo", outTradeNo);
            }

            @Override
            public void doAction() {
                PayRecord payRecord = payRecordService.queryDetail(outTradeNo);
                if (null == payRecord) {
                    log.error("支付单不存在 outTradeNo={}", outTradeNo);
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);
                }
                if (!payRecord.getCustomerNo().equals(cid)) {
                    log.error("非法请求 cid={},payRecord.getCustomerNo={}", cid, payRecord.getCustomerNo());
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);

                }
                PayStatusEnum enumByCode = PayStatusEnum.getEnumByCode(payRecord.getPayStatus());
                if (PayStatusEnum.SUCCESS == enumByCode || PayStatusEnum.FAIL == enumByCode || PayStatusEnum.CANCEL == enumByCode) {
                    log.error("{}订单已是终态", outTradeNo);
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);
                }
                payRecord.setPayStatus(PayStatusEnum.CANCEL.getCode());
                payRecordService.updateStatus(payRecord, PayStatusEnum.PENDING.getCode());
            }
        });
        log.info("取消支付单结束{},结果{}", outTradeNo, JSONObject.toJSONString(result));
        return result;

    }

    /**
     * 查询支付结果页
     *
     * @param payNo
     * @return
     */
    public CommonSingleResult<PayRecord> queryPayRes(String customerId, String payNo) {
        log.info("支付结果页 payNo={},customerId={}", payNo, customerId);
        CommonSingleResult<PayRecord> recordCommonSingleResult = new CommonSingleResult<>();
        bizTemplate.process(recordCommonSingleResult, new TemplateCallBack() {
            @Override
            public void doCheck() {
                CheckUtil.isNotBlank("payNo", payNo);
            }

            @Override
            public void doAction() {
                PayRecord payRecord = payRecordService.queryDetail(payNo);
                if (null == payRecord) {
                    log.error("支付单不存在，payNo={}", payNo);
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);
                }
                if (!payRecord.getCustomerNo().equals(customerId)) {
                    log.error("支付单用户不匹配，payNo={},customerId={}", payNo, customerId);
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);

                }
                if (PayStatusEnum.SUCCESS.getCode().equals(payRecord.getPayStatus()) || PayStatusEnum.FAIL.getCode().equals(payRecord.getPayStatus())
                        || PayStatusEnum.CANCEL.getCode().equals(payRecord.getPayStatus())) {
                    log.info("支付单已经为终态，payRecord={}", JSONObject.toJSONString(payRecord));
                    recordCommonSingleResult.setData(payRecord);
                    return;
                }
                syncStatus4WxPay(payNo, payRecord);
                PayRecord payRecord2 = payRecordService.queryDetail(payNo);
                recordCommonSingleResult.setData(payRecord2);
                return;
            }
        });
        log.info("支付结果页 recordCommonSingleResult={}", JSONObject.toJSONString(recordCommonSingleResult));
        return recordCommonSingleResult;

    }


    /**
     * 同步更新支付单
     *
     * @param payNo
     */
    public void syncStatus4WxPay(String payNo, PayRecord payRecord) {
        if (null == payRecord) {
            payRecord = payRecordService.queryDetail(payNo);
        }
        if (null == payRecord) {
            log.error("支付单不存在，payNo={}", payNo);
            throw new CommonException(CommonErrorEnum.PARAM_ERROR);
        }
        if (PayStatusEnum.SUCCESS.getCode().equals(payRecord.getPayStatus()) || PayStatusEnum.FAIL.getCode().equals(payRecord.getPayStatus())) {
            log.info("支付单已经为终态，payRecord={}", JSONObject.toJSONString(payRecord));
            return;
        }
        if (PayStatusEnum.INIT.getCode().equals(payRecord.getPayStatus())) {
            return;
        }
        WxQueryPayOrderDto wxQueryPayOrderDto = wxCommonApi.queryPayOrder(payNo);
        if (StringUtils.isEmpty(wxQueryPayOrderDto.getReturn_code())) {
//            查询失败
            log.warn("WxPayResultJob 查询失败 outTradeNo={}", payNo);
            return;
        }
        if ("SUCCESS".equals(wxQueryPayOrderDto.getTrade_state())) {
            wxQueryPayOrderDto.setResult_code("SUCCESS");
        } else if (FAIL_CODES.contains(wxQueryPayOrderDto.getTrade_state())) {
            wxQueryPayOrderDto.setResult_code("FAIL");
        } else if ("NOTPAY".equals(wxQueryPayOrderDto.getTrade_state())) {
            if (DateUtil.addHour(2, payRecord.getCreatedDate()).compareTo(new Date()) == -1) {
//                    2个小时超时 置为失败
                wxQueryPayOrderDto.setResult_code("FAIL");
            } else {
                log.info("NOTPAY，等待下次拉取,outTradeNo={}", payNo);
                return;
            }
        } else {
            log.warn("数据错误outTradeNo={},wxQueryPayOrderDto={}", payNo, JSONObject.toJSONString(wxQueryPayOrderDto));
            return;
        }
        //        3.校验支付金额参数是否一致
        if ("SUCCESS".equals(wxQueryPayOrderDto.getResult_code()) && payRecord.getPayAmount().multiply(new BigDecimal("100")).abs()
                .compareTo(new BigDecimal(wxQueryPayOrderDto.getTotal_fee())) != 0) {
            log.error(" 通知接受的金额不一致,wx_flow_no={},wxFlowDetail.getAmount={},wxQueryPayOrderDto.getTotal_fee={}",
                    wxQueryPayOrderDto.getOut_trade_no(), payRecord.getPayAmount().toPlainString(), wxQueryPayOrderDto.getTotal_fee());
            throw new CommonException(CommonErrorEnum.WX_AMOUNT_CHECK_ERROR);
        }
        log.info("wxNotify status={},outTradeNo={}", wxQueryPayOrderDto.getResult_code(), wxQueryPayOrderDto.getOut_trade_no());
        if ("SUCCESS".equals(wxQueryPayOrderDto.getResult_code())) {
            payRecord.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        } else if ("FAIL".equals(wxQueryPayOrderDto.getResult_code())) {
            payRecord.setPayStatus(PayStatusEnum.FAIL.getCode());
        }
        payRecordService.updateStatus(payRecord, PayStatusEnum.PENDING.getCode());

    }

    /**
     * @param customerId
     * @param amount     金豆数量
     * @return
     */
    public BaseResult cash(String customerId, BigDecimal amount) {
        log.info("#cash customerId={},amount={}", customerId, amount);
        BaseResult result = new BaseResult();
        Customer customer = customerService.queryCustomerByCustomerId(customerId);
        bizTemplate.process(result, new TemplateCallBack() {
            @Override
            public void doCheck() {
                CheckUtil.isNotNull("amount", amount);
                if (BigDecimal.ZERO.compareTo(amount) > -1) {
                    log.error("金豆数量不合法");
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);
                }
                if (!UserStatusEnum.EFFECT.getCode().equals(customer.getCustomerStatus())) {
                    log.error("用户状态不对");
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);
                }
                BigDecimal gold = customer.getGold();
                if (gold.compareTo(amount) == -1) {
                    log.error("金豆数量不足");
                    throw new CommonException(CommonErrorEnum.GOLD_NOT_ENOUGH);
                }

            }

            @Override
            public void doAction() {
                BigDecimal money = gold2Money(amount);
                PayRecord payRecord = new PayRecord();
                payRecord.setPayNo(IdMarkUtil.getUuid(TableTypeEnum.PAY_RECORD));
                payRecord.setPayChannel(ChannelEnum.WX_MINI.getCode());
                payRecord.setPayAmount(money);
                payRecord.setCustomerNo(customerId);
                payRecord.setOrderMsg("金豆提现");
                payRecordService.createPayNo(payRecord, PayTypeEnum.CASH);
//                1.扣金豆
                flowRecordService.changeScoreOrGold(CustomerUtil.getCustomer().getCustomerNo(), FlowRecordTypeEnum.GOLD, amount.negate(), "提现扣除金豆");
//                2.转账
                WxCompanyPayRequestDto wxCompanyPayRequestDto = new WxCompanyPayRequestDto();
                wxCompanyPayRequestDto.setTotalAmount(payRecord.getPayAmount());
                wxCompanyPayRequestDto.setMiniOpenId(customer.getWxMiniOpenId());
                wxCompanyPayRequestDto.setOutTradeNo(payRecord.getPayNo());
                BaseResult result1 = wxCommonApi.companyPay(wxCompanyPayRequestDto);
                if (!result1.isSuccess()) {
//                    失败回滚金豆
                    flowRecordService.changeScoreOrGold(CustomerUtil.getCustomer().getCustomerNo(), FlowRecordTypeEnum.GOLD, amount, "提现失败回滚金豆");
                    log.error("提现失败，result1={}", JSONObject.toJSONString(result1));
                    throw new CommonException(result1);
                }
            }
        });
        log.info("提现结果 result={}", JSONObject.toJSONString(result));
        return result;
    }

    public BaseResult exchange(BigDecimal amount, String customerId) {
        log.info("exchange amount={}", amount);
        BaseResult result = new BaseResult();
        bizTemplate.process(result, new TemplateCallBack() {
            @Override
            public void doCheck() {
                if (amount == null || amount.compareTo(BigDecimal.ZERO) < 1) {
                    log.error("金豆兑换 金额不合法");
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);
                }
                Customer customer = customerService.queryCustomerByCustomerId(customerId);
                if (null == customer || !UserStatusEnum.EFFECT.getCode().equals(customer.getCustomerStatus())) {
                    log.error("用户状态不对");
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);
                }
                BigDecimal score = customer.getScore();
                if (score.compareTo(amount) == -1) {
                    log.error("积分数量不足");
                    throw new CommonException(CommonErrorEnum.SCORE_NOT_ENOUGH);
                }
            }

            @Override
            public void doAction() {
                flowRecordService.changeScoreOrGold(customerId, FlowRecordTypeEnum.SCORE, amount.negate(), "金豆兑换");
                flowRecordService.changeScoreOrGold(customerId, FlowRecordTypeEnum.GOLD, amount, "金豆兑换");
            }
        });
        log.info("exchange result={}", JSONObject.toJSONString(result));
        return result;
    }

    /**
     * 金豆转换现金
     * * 5000以下 7.5元；
     * 5000-10000 20%；
     * 10000+ 10%；
     *todo 手续费待定
     * @param goldAmount
     * @return
     */
    private BigDecimal gold2Money(BigDecimal goldAmount) {
        BigDecimal bigDecimal = goldAmount.multiply(new BigDecimal("0.08")).setScale(2, BigDecimal.ROUND_HALF_UP);
        return bigDecimal;
    }
}
