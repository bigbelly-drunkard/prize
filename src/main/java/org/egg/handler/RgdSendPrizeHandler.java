package org.egg.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.egg.biz.LoadFactorBiz;
import org.egg.enums.FlowRecordTypeEnum;
import org.egg.model.DO.PayRecord;
import org.egg.model.DTO.PrizeBean;
import org.egg.service.impl.FlowRecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author dataochen
 * @Description 随机金豆奖励 最低1金豆
 * @date: 2020/8/4 9:50
 */
@Component(value = "RGD")
@Slf4j
public class RgdSendPrizeHandler extends AbstractSendPrize {
    @Autowired
    private FlowRecordServiceImpl flowRecordService;
    @Autowired
    private LoadFactorBiz loadFactorBiz;
    @Override
    public void sendPrizeHandler(String customerId, PrizeBean prizeBean, PayRecord payRecord) {

    }

    @Override
    public void sendPrizeHandler(String customerId, PrizeBean prizeBean) {
        log.info("发送随机金豆，customerId={},prizeBean={}", customerId,
                JSONObject.toJSONString(prizeBean));
        BigDecimal price = getPrice(prizeBean);
        flowRecordService.changeScoreOrGold(customerId, FlowRecordTypeEnum.GOLD, price,"随机金豆奖品"+prizeBean.getName());
        loadFactorBiz.goldSend(price,customerId);
        log.info("发送随机金豆成功");
    }

    @Override
    public BigDecimal getPrice(PrizeBean prizeBean) {
        BigDecimal bigDecimal = new BigDecimal(Math.random() + "").multiply(prizeBean.getFactor()).setScale(1,BigDecimal.ROUND_HALF_UP);
        bigDecimal = bigDecimal.compareTo(new BigDecimal("1")) != 1 ? new BigDecimal("1") : bigDecimal;
        log.info("随机金豆 bigDecimal={}", bigDecimal);
        prizeBean.setPrizeNum(bigDecimal);
        return bigDecimal;
    }
}
