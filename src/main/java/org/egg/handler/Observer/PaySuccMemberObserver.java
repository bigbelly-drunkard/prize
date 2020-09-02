package org.egg.handler.Observer;

import lombok.extern.slf4j.Slf4j;
import org.egg.enums.OrderTypeEnum;
import org.egg.enums.PayStatusEnum;
import org.egg.enums.PayTypeEnum;
import org.egg.model.DO.PayRecord;
import org.egg.observer.Observer;
import org.egg.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/4 20:36
 */
@Component("paySuccMemberObserver")
@Slf4j
public class PaySuccMemberObserver implements Observer {
    @Autowired
    private CustomerServiceImpl customerService;

    @Override
    public void update(Object obj) {
        PayRecord obj1 = (PayRecord) obj;
        if (!PayTypeEnum.PAY.getCode().equals(obj1.getPayType())) {
            log.debug("非支付单，跳过会员开通观察者");
            return;
        }
        OrderTypeEnum enumByCode1 = OrderTypeEnum.getEnumByCode(obj1.getOrderType());


        PayStatusEnum enumByCode = PayStatusEnum.getEnumByCode(obj1.getPayStatus());
        switch (enumByCode) {
            case SUCCESS:
                exe(obj1.getCustomerNo(),enumByCode1);
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

    private void exe(String cid, OrderTypeEnum enumByCode) {
        int day = 0;
        switch (enumByCode) {
            case MEMBER_7:
                day=7;
                break;
            case MEMBER_30:
                day=30;
                break;
            case MEMBER_365:
                day=365;
                break;
            case MEMBER_1095:
                day=1095;
                break;
            default:
                return;
        }
        customerService.updateMember(cid, day);
    }
}
