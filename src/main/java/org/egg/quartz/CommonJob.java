package org.egg.quartz;

import lombok.extern.slf4j.Slf4j;
import org.egg.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/29 17:38
 */
@Component
@Slf4j
public class CommonJob {
    @Autowired
    private CustomerServiceImpl customerService;

    /**
     * 同步累计缓存中的负载因子值
     * 1小时一次 每个小时的第二分钟开始执行
     */
    @Scheduled(cron = "0 2 0/1 * * ?")
    public void syncLoadFactor4Cache() {
        log.info("syncLoadFactor4Cache start");
        customerService.sumLoadFactor4Cache(null);
        log.info("syncLoadFactor4Cache end");
    }
}
