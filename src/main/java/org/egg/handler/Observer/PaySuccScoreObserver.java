package org.egg.handler.Observer;

import lombok.extern.slf4j.Slf4j;
import org.egg.enums.FlowRecordTypeEnum;
import org.egg.enums.OrderTypeEnum;
import org.egg.enums.PayStatusEnum;
import org.egg.model.DO.PayRecord;
import org.egg.observer.Observer;
import org.egg.service.impl.FlowRecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/4 20:36
 */
@Component("paySuccScoreObserver")
@Slf4j
public class PaySuccScoreObserver implements Observer {
    @Autowired
    private FlowRecordServiceImpl flowRecordService;

    @Override
    public void update(Object obj) {
        PayRecord obj1 = (PayRecord) obj;
        if (!OrderTypeEnum.SCORE.getCode().equals(obj1.getOrderType())) {
            log.info("不是积分商品类型，跳过;obj1.getOrderType()={}",obj1.getOrderType());
            return;
        }
        PayStatusEnum enumByCode = PayStatusEnum.getEnumByCode(obj1.getPayStatus());
        switch (enumByCode) {
            case SUCCESS:
                convertScore(obj1.getCustomerNo(), obj1.getPayAmount());
                break;
            case FAIL:
                break;
            default:
                break;
        }

    }

    @Override
    public void update() {

    }

    /**
     * 转换积分
     * 1元 10积分
     * 6 60
     * 45 450+25
     * 68 680+35
     * 118 1180+65
     * 198 1980+115
     * 348 3480+215
     * 648 6480+415
     *
     * @param amount
     */
    private void convertScore(String cid, BigDecimal amount) {
        int score = 0;
        switch (amount.intValue()) {
            case 1:
                score = 10;
                break;
            case 6:
                score = 60;
                break;
            case 45:
                score = 450 + 25;
                break;
            case 68:
                score = 680 + 35;
                break;
            case 118:
                score = 1180 + 65;
                break;
            case 198:
                score = 1980 + 115;
                break;
            case 348:
                score = 3480 + 215;
                break;
            case 648:
                score = 6480 + 415;
                break;
            default:
                break;
        }
        flowRecordService.changeScoreOrGold(cid, FlowRecordTypeEnum.SCORE, new BigDecimal(score));
    }
}
