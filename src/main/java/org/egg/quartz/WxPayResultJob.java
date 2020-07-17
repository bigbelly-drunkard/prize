//package org.egg.quartz;
//
//import com.alibaba.fastjson.JSONObject;
//import org.egg.biz.AccountFlowBiz;
//import org.egg.biz.WxPayBiz;
//import org.egg.enums.AccountFlowStatusEnum;
//import org.egg.enums.FlowUserTypeEnum;
//import org.egg.integration.wx.WxCommonApi;
//import org.egg.model.DO.AccountFlow;
//import org.egg.model.DTO.WxQueryPayOrderDto;
//import org.egg.model.VO.AccountFlowQueryReq;
//import org.egg.service.impl.AccountFlowServiceImpl;
//import org.egg.utils.DateUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.StringUtils;
//
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
///**
// * @author cdt
// * @Description 微信支付结果查询 入金
// * 5分钟一次
// * 如果有一单异常 continue 继续同步下一条
// * jdk1.8的ForEach 中的return 等同于循环中的continue
// * @date: 2018/4/2 11:24
// */
//@Component
//public class WxPayResultJob implements Job {
//    private static final Logger LOGGER = LoggerFactory.getLogger(WxPayResultJob.class);
//    @Autowired
//    private WxCommonApi wxCommonApi;
//    @Autowired
//    private WxPayBiz wxPayBiz;
//    @Autowired
//    private AccountFlowBiz accountFlowBiz;
//    @Autowired
//    private AccountFlowServiceImpl accountFlowService;
//    @Value("${environment}")
//    private String ENVIRONMENT;
//    private List<String> FAIL_CODES = Arrays.asList("CLOSED", "REVOKED", "PAYERROR");
//
////    @Scheduled(cron = "0 3/5 * * * ?")
//    @Override
//    public void exe() {
//        if (ENVIRONMENT.equals("test")) {
//            return;
//        }
//        Thread.currentThread().setName("WxPayResultJob" + DateUtil.format(new Date(), DateUtil.YMDHMSSS));
//        long start = System.currentTimeMillis();
//        LOGGER.info("WxPayResultJob start");
////        查询处理中的支付单
//        AccountFlowQueryReq accountFlowQueryReq = new AccountFlowQueryReq();
//        accountFlowQueryReq.setRecordStatus(AccountFlowStatusEnum.PROCESS.getIndex());
//        accountFlowQueryReq.setPageNo(1);
//        accountFlowQueryReq.setPageNum(10);
//        accountFlowQueryReq.setFlowUserType(FlowUserTypeEnum.PUB_USER.getIndex());
//        List<AccountFlow> accountFlows = accountFlowService.queryList(accountFlowQueryReq);
//        if (CollectionUtils.isEmpty(accountFlows)) {
//            LOGGER.info("WxPayResultJob 没有需要处理的支付单");
//            return;
//        }
//        accountFlows.forEach(items -> {
////查询订单接口
//            WxQueryPayOrderDto wxQueryPayOrderDto = wxCommonApi.queryPayOrder(items.getRecordNo());
//
//            if (StringUtils.isEmpty(wxQueryPayOrderDto.getReturn_code())) {
////            查询失败
//                LOGGER.warn("WxPayResultJob 查询失败 outTradeNo={}", items.getRecordNo());
//                return;
//            }
//            if ("SUCCESS".equals(wxQueryPayOrderDto.getTrade_state())) {
//                wxQueryPayOrderDto.setResult_code("SUCCESS");
//            } else if (FAIL_CODES.contains(wxQueryPayOrderDto.getTrade_state())) {
//                wxQueryPayOrderDto.setResult_code("FAIL");
//            } else if ("NOTPAY".equals(wxQueryPayOrderDto.getTrade_state())) {
//                if (DateUtil.addHour(2, items.getCreatedDate()).compareTo(new Date()) == -1) {
////                    2个小时超时 置为失败
//                    wxQueryPayOrderDto.setResult_code("FAIL");
//                } else {
//                    LOGGER.info("NOTPAY，等待下次拉取,outTradeNo={}", items.getRecordNo());
//                    return;
//                }
//            } else {
//                LOGGER.warn("数据错误outTradeNo={},wxQueryPayOrderDto={}", items.getRecordNo(), JSONObject.toJSONString(wxQueryPayOrderDto));
//                return;
//            }
//            try {
//                wxPayBiz.wxUCommon(wxQueryPayOrderDto);
//            } catch (Exception e) {
//                LOGGER.warn("数据异常 outTradeNo={},e={}", items.getRecordNo(), e);
//                return;
//            }
//        });
//        long end = System.currentTimeMillis();
//        LOGGER.info("WxPayResultJob end,spend time={} ms", end - start);
//
//    }
//
//}
