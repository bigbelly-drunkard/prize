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
 * @Description 金豆奖励
 * @date: 2020/8/4 9:50
 */
@Component(value = "GD")
@Slf4j
public class GdSendPrizeHandler extends AbstractSendPrize {
    @Autowired
    private FlowRecordServiceImpl flowRecordService;

    @Override
    public void sendPrizeHandler(String customerId, PrizeBean prizeBean, PayRecord payRecord) {

    }

    @Override
    public void sendPrizeHandler(String customerId, PrizeBean prizeBean) {
        log.info("发送定额金豆，customerId={},prizeBean={}", customerId,
                JSONObject.toJSONString(prizeBean));
        flowRecordService.changeScoreOrGold(customerId, FlowRecordTypeEnum.GOLD, getPrice(prizeBean),"定额金豆奖品"+prizeBean.getName());
        log.info("发送定额金豆成功");
    }

    @Override
    public BigDecimal getPrice(PrizeBean prizeBean) {
        return prizeBean.getFactor();
    }
}
