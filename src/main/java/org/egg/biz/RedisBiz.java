package org.egg.biz;

import lombok.extern.slf4j.Slf4j;
import org.egg.enums.CommonErrorEnum;
import org.egg.enums.FlowRecordTypeEnum;
import org.egg.exception.CommonException;
import org.egg.response.BaseResult;
import org.egg.service.impl.FlowRecordServiceImpl;
import org.egg.service.impl.RedisServiceImpl;
import org.egg.template.BizTemplate;
import org.egg.template.TemplateCallBack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/5 17:50
 */
@Component
@Slf4j
public class RedisBiz {
    @Autowired
    private FlowRecordServiceImpl flowRecordService;
    @Autowired
    private BizTemplate bizTemplate;
    @Autowired
    private RedisServiceImpl redisService;

    public BaseResult qd(String customerId) {
        log.info("{} 准备签到", customerId);
        BaseResult result = new BaseResult();
        bizTemplate.process(result, new TemplateCallBack() {
            @Override
            public void doCheck() {

            }

            @Override
            public void doAction() {
                boolean b = redisService.checkCustomer4Day1(customerId);
                if (!b) {
                    log.info("用户今天已经签到过");
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);
                }
                BigDecimal value = new BigDecimal("0.5");
                flowRecordService.changeScoreOrGold(customerId, FlowRecordTypeEnum.SCORE, value,"每日签到奖励");
            }
        });
        log.info("{} 签到成功", customerId);
        return result;
    }

    public BaseResult share(String customerId) {
        log.info("{} 准备每日分享", customerId);
        BaseResult result = new BaseResult();
        bizTemplate.process(result, new TemplateCallBack() {
            @Override
            public void doCheck() {

            }

            @Override
            public void doAction() {
                boolean b = redisService.checkCustomer4Day2(customerId);
                if (!b) {
                    log.info("用户今天已经分享过");
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);
                }
                BigDecimal value = new BigDecimal("2");
                flowRecordService.changeScoreOrGold(customerId, FlowRecordTypeEnum.SCORE, value,"每日分享奖励");
            }
        });
        log.info("{} 每日分享成功", customerId);
        return result;
    }
//todo
    public BaseResult dt(String customerId) {
        log.info("{} 准备每日答题", customerId);
        BaseResult result = new BaseResult();
        bizTemplate.process(result, new TemplateCallBack() {
            @Override
            public void doCheck() {

            }

            @Override
            public void doAction() {
                boolean b = redisService.checkCustomer4Day2(customerId);
                if (!b) {
                    log.info("用户今天已经答题过");
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);
                }
                BigDecimal value = new BigDecimal("0.1");
                flowRecordService.changeScoreOrGold(customerId, FlowRecordTypeEnum.SCORE, value,"每日答题奖励");
            }
        });
        log.info("{} 每日答题成功", customerId);
        return result;
    }
}
