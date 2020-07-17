package org.egg.quartz;

import org.egg.model.DO.WxAccessTokenUser;
import org.egg.service.impl.WechatServiceImpl;
import org.egg.service.impl.WxAccessTokenUserServiceImpl;
import org.egg.utils.ConstantsUtil;
import org.egg.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author cdt
 * @Description 已同步微信用户信息更新同步
 * 3天一次
 * @date: 2018/3/27 20:50
 */
@Component
public class WxOldUserInfoJob implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxOldUserInfoJob.class);
    @Autowired
    private WxAccessTokenUserServiceImpl wxAccessTokenUserService;
    @Autowired
    private WechatServiceImpl wechatService;

//    @Scheduled(cron = "0 4 0 1/3 * ?")
    @Override
    public void exe() {
        if (!ConstantsUtil.WX_JOB_SWITH) {
            return;
        }
        Thread.currentThread().setName("WxOldUserInfoJob"+ DateUtil.format(new Date(),DateUtil.YMDHMSSS));
        long start = System.currentTimeMillis();
        LOGGER.info("WxOldUserInfoJob start");
//查询未同步微信用户集合
        List<WxAccessTokenUser> wxAccessTokenUsers = wxAccessTokenUserService.queryTokenOld();
        if (!CollectionUtils.isEmpty(wxAccessTokenUsers)) {
            wxAccessTokenUsers.forEach(items -> {
                wechatService.getUserToken(items);
                wechatService.getWxUserInfo(items.getAccessToken(), items.getUserNo());
            });
        }
        long end = System.currentTimeMillis();
        LOGGER.info("WxOldUserInfoJob end,spend time={} ms",end-start);

    }
}
