package org.egg.handler.Observer;

import lombok.extern.slf4j.Slf4j;
import org.egg.biz.LoadFactorBiz;
import org.egg.enums.PayStatusEnum;
import org.egg.enums.PayTypeEnum;
import org.egg.model.DO.PayRecord;
import org.egg.observer.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/4 20:36
 */
@Component("paySuccLoadFactorObserver")
@Slf4j
public class PaySuccLoadFactorObserver implements Observer {
    @Autowired
    private LoadFactorBiz loadFactorBiz;

    @Override
    public void update(Object obj) {
        PayRecord obj1 = (PayRecord) obj;
        PayStatusEnum enumByCode = PayStatusEnum.getEnumByCode(obj1.getPayStatus());
        switch (enumByCode) {
            case SUCCESS:
                loadFactorLogic(obj1);
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

    private void loadFactorLogic(PayRecord payRecord) {
        String payType = payRecord.getPayType();
        if (PayTypeEnum.PAY.getCode().equals(payType)) {
            loadFactorBiz.buyGood(payRecord.getPayAmount(), payRecord.getCustomerNo());
        } else if (PayTypeEnum.CASH.getCode().equals(payType)) {
            loadFactorBiz.cash(payRecord.getPayAmount(), payRecord.getCustomerNo());
        }
    }
}
