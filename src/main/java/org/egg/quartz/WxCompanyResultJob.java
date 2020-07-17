//package org.egg.quartz;
//
//import com.alibaba.fastjson.JSONObject;
//import org.egg.biz.AccountFlowBiz;
//import org.egg.biz.WxPayBiz;
//import org.egg.enums.AccountFlowStatusEnum;
//import org.egg.enums.FlowUserTypeEnum;
//import org.egg.integration.wx.WxCommonApi;
//import org.egg.model.DO.AccountFlow;
//import org.egg.model.VO.AccountFlowQueryReq;
//import org.egg.response.BaseResult;
//import org.egg.service.impl.AccountFlowServiceImpl;
//import org.egg.utils.DateUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
///**
// * @author cdt
// * @Description 企业付款结果查询 出金
// * 5分钟一次
// * 如果有一单异常 continue 继续同步下一条
// * jdk1.8的ForEach 中的return 等同于循环中的continue
// * @date: 2018/4/2 11:24
// */
//@Component
//public class WxCompanyResultJob implements Job {
//    private static final Logger LOGGER = LoggerFactory.getLogger(WxCompanyResultJob.class);
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
////    @Scheduled(cron = "0 4/5 * * * ?")
//    @Override
//    public void exe() {
//        Thread.currentThread().setName("WxCompanyResultJob" + DateUtil.format(new Date(), DateUtil.YMDHMSSS));
//        long start = System.currentTimeMillis();
//        LOGGER.info("WxCompanyResultJob start");
////        查询处理中的支付单
//        AccountFlowQueryReq accountFlowQueryReq = new AccountFlowQueryReq();
//        accountFlowQueryReq.setRecordStatus(AccountFlowStatusEnum.PROCESS.getIndex());
//        accountFlowQueryReq.setPageNo(1);
//        accountFlowQueryReq.setPageNum(10);
//        accountFlowQueryReq.setFlowUserType(FlowUserTypeEnum.ACCEPT_USER.getIndex());
//        List<AccountFlow> accountFlows = accountFlowService.queryList(accountFlowQueryReq);
//        if (CollectionUtils.isEmpty(accountFlows)) {
//            LOGGER.info("WxCompanyResultJob 没有需要处理的支付单");
//            return;
//        }
//        accountFlows.forEach(items -> {
//            try {
//
////查询订单接口
//                BaseResult result = wxCommonApi.queryResult4CompanyApi(items.getRecordNo());
//
//                if (null == result) {
////            查询失败
//                    LOGGER.warn("WxCompanyResultJob 查询失败 outTradeNo={}", items.getRecordNo());
//                    return;
//                }
//                if (result.isSuccess()) {
//                    accountFlowBiz.updateStatus(AccountFlowStatusEnum.SUCC, items);
//                } else {
//                    accountFlowBiz.updateStatus(AccountFlowStatusEnum.FAIL, items);
//                }
//            } catch (Exception e) {
//                LOGGER.warn("#WxCompanyResultJob items={},e={}", JSONObject.toJSONString(items), e);
//            }
//        });
//        long end = System.currentTimeMillis();
//        LOGGER.info("WxCompanyResultJob end,spend time={} ms", end - start);
//
//    }
//
//}
