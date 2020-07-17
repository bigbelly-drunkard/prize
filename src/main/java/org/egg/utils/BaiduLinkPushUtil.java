package org.egg.utils;

import com.alibaba.fastjson.JSONObject;
import org.egg.model.DTO.EmailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 百度链接提交工具
 * Created by chendatao on 2018/12/10.
 */
public class BaiduLinkPushUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaiduLinkPushUtil.class);
    private static ThreadPoolTaskExecutor poolExecutorBaiduLinkPush;

    static {
        poolExecutorBaiduLinkPush = new ThreadPoolTaskExecutor();
        poolExecutorBaiduLinkPush.setCorePoolSize(5);
        poolExecutorBaiduLinkPush.setMaxPoolSize(20);
        poolExecutorBaiduLinkPush.setKeepAliveSeconds(200);
        poolExecutorBaiduLinkPush.setQueueCapacity(100);
        poolExecutorBaiduLinkPush.initialize();
    }

    /**
     * 推送新链接
     *
     * @param urls
     */
    public static void pushNew(List<String> urls) {
        poolExecutorBaiduLinkPush.execute(() -> {
            Thread.currentThread().setName("poolExecutorBaiduLinkPush-pushNew"+ DateUtil.format(new Date(),DateUtil.YMDHMSSS));
            LOGGER.info("pushNew urls={}", JSONObject.toJSONString(urls));
            String url = "http://data.zz.baidu.com/urls?site=www.dailianshijie.com&token=MZAAkNaojbYCaBk9";
            StringBuilder conent = new StringBuilder();
            urls.forEach(item -> {
                conent.append(item);
                conent.append("\n");
            });
            String s = HttpRequestUtil.postForBaiduLinkPush(url, conent.toString());
            JSONObject jsonObject = JSONObject.parseObject(s);
            if (jsonObject.get("success") == null) {
                LOGGER.error("pushNew百度链接推送报警 error,msg={}", s);
//            EmailUtil.sendEmailFormQQ(new EmailDto("pushNew百度链接推送报警 error", s, EmailUtil.TOEMAILACC));
                return;
            }
            if (jsonObject.get("not_same_site") != null) {
                LOGGER.error("pushNew not_same_site error,not_same_site={}", jsonObject.get("not_same_site").toString());
                EmailUtil.sendEmailFormQQ(new EmailDto("pushNew百度链接推送报警 not_same_site", jsonObject.get("not_same_site").toString(), EmailUtil.TOEMAILACC));
            }
            if (jsonObject.get("not_valid") != null) {
                LOGGER.error("pushNew not_valid error,not_valid={}", jsonObject.get("not_valid").toString());
                EmailUtil.sendEmailFormQQ(new EmailDto("pushNew百度链接推送报警 not_valid", jsonObject.get("not_valid").toString(), EmailUtil.TOEMAILACC));
            }
            LOGGER.info("pushNew end");
        });
    }

    /**
     * 推送更新链接
     *
     * @param urls
     */
    public static void pushUpdate(List<String> urls) {
        poolExecutorBaiduLinkPush.execute(() -> {
            LOGGER.info("pushUpdate urls={}", JSONObject.toJSONString(urls));
            String url = "http://data.zz.baidu.com/update?site=www.dailianshijie.com&token=MZAAkNaojbYCaBk9";
            StringBuilder conent = new StringBuilder();
            urls.forEach(item -> {
                conent.append(item);
                conent.append("\n");
            });
            String s = HttpRequestUtil.postForBaiduLinkPush(url, conent.toString());
            JSONObject jsonObject = JSONObject.parseObject(s);
            if (jsonObject.get("success") == null) {
                LOGGER.error("pushUpdate百度链接推送报警 error,msg={}", s);
//            EmailUtil.sendEmailFormQQ(new EmailDto("pushUpdate百度链接推送报警 error", s, EmailUtil.TOEMAILACC));
                return;
            }
            if (jsonObject.get("not_same_site") != null) {
                LOGGER.error("pushUpdate not_same_site error,not_same_site={}", jsonObject.get("not_same_site").toString());
                EmailUtil.sendEmailFormQQ(new EmailDto("pushUpdate百度链接推送报警 not_same_site", jsonObject.get("not_same_site").toString(), EmailUtil.TOEMAILACC));
            }
            if (jsonObject.get("not_valid") != null) {
                LOGGER.error("pushUpdate not_valid error,not_valid={}", jsonObject.get("not_valid").toString());
                EmailUtil.sendEmailFormQQ(new EmailDto("pushUpdate百度链接推送报警 not_valid", jsonObject.get("not_valid").toString(), EmailUtil.TOEMAILACC));
            }
            LOGGER.info("pushUpdate end");
        });
    }


    public static void main(String[] args) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("https://www.dailianshijie.com/pc/showSpecification1");
        strings.add("https://www.dailianshijie.com/pc/showAboutUs1");

//        pushNew(strings);
        pushUpdate(strings);
    }
}
