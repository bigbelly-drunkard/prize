package org.egg.handler;

import lombok.extern.slf4j.Slf4j;
import org.egg.enums.ChannelEnum;
import org.egg.enums.PayStatusEnum;
import org.egg.enums.PayTypeEnum;
import org.egg.enums.TableTypeEnum;
import org.egg.model.DO.PayRecord;
import org.egg.model.DTO.PrizeBean;
import org.egg.service.impl.PayRecordServiceImpl;
import org.egg.utils.ApplicationContextUtil;
import org.egg.utils.IdMarkUtil;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/4 10:01
 */
@Slf4j
public abstract class AbstractSendPrize implements SendPrizeHandler {
    private PayRecordServiceImpl payRecordService;
    private List<String> needPay = Arrays.asList("RP", "RRP");

    public AbstractSendPrize() {
        payRecordService = (PayRecordServiceImpl) ApplicationContextUtil.getObject("payRecordService");
    }

    @Override
    public void sendPrize(String customerId, PrizeBean prizeBean) {
        if (needPay.contains(prizeBean.getTypeCode())) {
            PayRecord payRecord = insertPayRecord(customerId, prizeBean);
            try {
                sendPrizeHandler(customerId, prizeBean, payRecord);
                payRecord.setPayStatus(PayStatusEnum.SUCCESS.getCode());
            } catch (Exception e) {
                log.warn("发奖失败 降级默认为未中奖 e={}", e);
                //  2020/8/4 降级默认为未中奖
                prizeBean.setId(null);
                payRecord.setPayStatus(PayStatusEnum.FAIL.getCode());
            }
//          支付单状态变更
            payRecordService.updateStatus(payRecord, PayStatusEnum.PENDING.getCode());
        } else {
            try {
                sendPrizeHandler(customerId, prizeBean);
            } catch (Exception e) {
                log.warn("发奖失败 降级默认为未中奖 e={}", e);
                //  2020/8/4 降级默认为未中奖
                prizeBean.setId(null);
            }
        }

    }

    /**
     * 发放奖品
     *
     * @param customerId
     * @param prizeBean
     * @param payRecord
     */
    public abstract void sendPrizeHandler(String customerId, PrizeBean prizeBean, PayRecord payRecord);

    public abstract void sendPrizeHandler(String customerId, PrizeBean prizeBean);

    /**
     * 获取支付单金额
     *
     * @param prizeBean
     * @return
     */
    public abstract BigDecimal getPrice(PrizeBean prizeBean);

    private PayRecord insertPayRecord(String customerId, PrizeBean prizeBean) {
        PayRecord payRecord = new PayRecord();
        payRecord.setPayStatus(PayStatusEnum.PENDING.getCode());
        payRecord.setOrderMsg(prizeBean.getName());
        payRecord.setCustomerNo(customerId);
        payRecord.setPayAmount(getPrice(prizeBean));
        payRecord.setPayChannel(ChannelEnum.WX_MINI.getCode());
        payRecord.setPayNo(IdMarkUtil.getUuid(TableTypeEnum.PAY_RECORD));
        payRecordService.createPayNo(payRecord, PayTypeEnum.CASH);
        return payRecord;
    }
}
