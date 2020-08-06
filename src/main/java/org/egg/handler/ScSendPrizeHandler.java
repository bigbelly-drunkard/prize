package org.egg.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.egg.enums.FlowRecordTypeEnum;
import org.egg.model.DO.PayRecord;
import org.egg.model.DTO.PrizeBean;
import org.egg.service.impl.FlowRecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author dataochen
 * @Description 积分奖励
 * @date: 2020/8/4 9:50
 */
@Component(value = "SC")
@Slf4j
public class ScSendPrizeHandler extends AbstractSendPrize {
    @Autowired
    private FlowRecordServiceImpl flowRecordService;

    @Override
    public void sendPrizeHandler(String customerId, PrizeBean prizeBean, PayRecord payRecord) {

    }

    @Override
    public void sendPrizeHandler(String customerId, PrizeBean prizeBean) {
        log.info("发送定额积分，customerId={},prizeBean={}", customerId,
                JSONObject.toJSONString(prizeBean));
        flowRecordService.changeScoreOrGold(customerId, FlowRecordTypeEnum.SCORE, getPrice(prizeBean));
        log.info("发送定额积分成功");
    }

    @Override
    public BigDecimal getPrice(PrizeBean prizeBean) {
        return prizeBean.getFactor();
    }
}
