package org.egg.handler.Observer;

import lombok.extern.slf4j.Slf4j;
import org.egg.enums.FlowRecordTypeEnum;
import org.egg.model.DTO.PrizeBean;
import org.egg.observer.Observer;
import org.egg.service.impl.CustomerServiceImpl;
import org.egg.service.impl.FlowRecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author dataochen
 * @Description 扣除积分等数据库操作
 * @date: 2020/8/4 18:28
 */
@Slf4j
@Component("prizeDbObserver")
public class PrizeDbObserver implements Observer {
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    private FlowRecordServiceImpl flowRecordService;

    @Override
    public void update(Object obj) {
        PrizeBean obj1 = (PrizeBean) obj;
        if (null != obj1.getNeedScore()&& BigDecimal.ZERO.compareTo(obj1.getNeedScore())==-1) {
            flowRecordService.changeScoreOrGold(obj1.getCid(), FlowRecordTypeEnum.SCORE, obj1.getNeedScore().negate(),"抽奖扣除积分");
        }
        if (null != obj1.getNeedGold()&& BigDecimal.ZERO.compareTo(obj1.getNeedGold())==-1) {
            flowRecordService.changeScoreOrGold(obj1.getCid(), FlowRecordTypeEnum.GOLD, obj1.getNeedGold().negate(),"抽奖扣除金豆");
        }
    }

    @Override
    public void update() {

    }
}
