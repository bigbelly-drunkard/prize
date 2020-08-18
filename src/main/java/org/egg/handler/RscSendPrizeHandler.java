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
 * @Description 随机积分奖励 最低1积分
 * @date: 2020/8/4 9:50
 */
@Component(value = "RSC")
@Slf4j
public class RscSendPrizeHandler extends AbstractSendPrize {
    @Autowired
    private FlowRecordServiceImpl flowRecordService;

    @Override
    public void sendPrizeHandler(String customerId, PrizeBean prizeBean, PayRecord payRecord) {

    }

    @Override
    public void sendPrizeHandler(String customerId, PrizeBean prizeBean) {
        log.info("发送随机积分，customerId={},prizeBean={}", customerId,
                JSONObject.toJSONString(prizeBean));
        flowRecordService.changeScoreOrGold(customerId, FlowRecordTypeEnum.SCORE, getPrice(prizeBean),"随机积分奖品"+prizeBean.getName());
        log.info("发送随机积分成功");
    }

    @Override
    public BigDecimal getPrice(PrizeBean prizeBean) {
        BigDecimal bigDecimal = new BigDecimal(Math.random() + "").multiply(prizeBean.getFactor()).setScale(1,BigDecimal.ROUND_HALF_UP);
        bigDecimal = bigDecimal.compareTo(new BigDecimal("1")) != 1 ? new BigDecimal("1") : bigDecimal;
        log.info("随机积分 bigDecimal={}", bigDecimal);
        prizeBean.setPrizeNum(bigDecimal);
        return bigDecimal;
    }
}
