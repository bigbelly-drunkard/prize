package org.egg.quartz;

import com.alibaba.fastjson.JSONObject;
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
 * @Description 未同步微信用户信息同步
 * 5分钟更新
 * @date: 2018/3/27 20:50
 */
@Component
public class WxUserInfoJob implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxUserInfoJob.class);
    @Autowired
    private WxAccessTokenUserServiceImpl wxAccessTokenUserService;
    @Autowired
    private WechatServiceImpl wechatService;

//    @Scheduled(cron = "0 2/5 * * * ?")
    @Override
    public void exe() {
        if (!ConstantsUtil.WX_JOB_SWITH) {
            return;
        }
        Thread.currentThread().setName("WxUserInfoJob"+ DateUtil.format(new Date(),DateUtil.YMDHMSSS));
        long start = System.currentTimeMillis();
        LOGGER.info("WxUserInfoJob start");
//查询未同步微信用户集合
        List<WxAccessTokenUser> wxAccessTokenUsers = wxAccessTokenUserService.queryTokenNew();
        if (!CollectionUtils.isEmpty(wxAccessTokenUsers)) {
            wxAccessTokenUsers.forEach(items->{
                try {
                    wechatService.getUserToken(items);
                    wechatService.getWxUserInfo(items.getAccessToken(), items.getUserNo());
                } catch (Exception e) {
                    LOGGER.error("WxUserInfoJob同步微信用户信息错误 items={}，e={}", JSONObject.toJSONString(items),e);
                }
            });
        }
        long end = System.currentTimeMillis();
        LOGGER.info("WxUserInfoJob end,spend time={} ms",end-start);

    }
}
