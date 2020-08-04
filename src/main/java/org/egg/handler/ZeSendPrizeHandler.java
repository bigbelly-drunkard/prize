package org.egg.handler;

import lombok.extern.slf4j.Slf4j;
import org.egg.model.DO.PayRecord;
import org.egg.model.DTO.PrizeBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author dataochen
 * @Description 红包奖励
 * @date: 2020/8/4 9:50
 */
@Component(value = "ZR")
@Slf4j
public class ZeSendPrizeHandler extends AbstractSendPrize {

    @Override
    public void sendPrizeHandler(String customerId, PrizeBean prizeBean, PayRecord payRecord) {
        log.info("未中奖");
    }

    @Override
    public BigDecimal getPrice(PrizeBean prizeBean) {
        return BigDecimal.ZERO;
    }
}
