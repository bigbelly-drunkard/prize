//package org.egg.quartz;
//
//import org.egg.enums.ChannelEnum;
//import org.egg.service.impl.WechatServiceImpl;
//import org.egg.utils.ConstantsUtil;
//import org.egg.utils.DateUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
///**
// * @author cdt
// * @Description 定时刷新普通access_token (2小时刷新)
// * @date: 2018/3/27 10:40
// */
//@Component
//public class WxAccessTokenForMerchantJob implements Job {
//    private static final Logger LOGGER = LoggerFactory.getLogger(WxAccessTokenForMerchantJob.class);
//    @Autowired
//    private WechatServiceImpl wechatService;
//
////    @Scheduled(cron = "0 1 0/2 * * ?")
//    @Override
//    public void exe() {
//        if (!ConstantsUtil.WX_JOB_SWITH) {
//            return;
//        }
//        Thread.currentThread().setName("WxAccessTokenForMerchantJob"+ DateUtil.format(new Date(),DateUtil.YMDHMSSS));
//        long start = System.currentTimeMillis();
//        LOGGER.info("WxAccessTokenForMerchantJob start");
////        XXX 暂时只有小程序
////        wechatService.getAccessTokenForMerchant(ChannelEnum.WX);
//        wechatService.getAccessTokenForMerchant(ChannelEnum.WX_MINI);
//        long end = System.currentTimeMillis();
//        LOGGER.info("WxAccessTokenForMerchantJob end,spend time={} ms",end-start);
//
//    }
//}
