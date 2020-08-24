package org.egg.handler.Observer;

import lombok.extern.slf4j.Slf4j;
import org.egg.biz.MsgBiz;
import org.egg.enums.PrizeTypeEnum;
import org.egg.model.DO.Customer;
import org.egg.model.DTO.PrizeBean;
import org.egg.observer.Observer;
import org.egg.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dataochen
 * @Description 中奖消息
 * @date: 2020/8/4 18:28
 */
@Slf4j
@Component("prizeMsgObserver")
public class PrizeMsgObserver implements Observer {
    @Autowired
    private MsgBiz msgBiz;
    @Autowired
    private CustomerServiceImpl customerService;


    @Override
    public void update(Object obj) {
        PrizeBean prizeBean = (PrizeBean) obj;
        PrizeTypeEnum enumByCode = PrizeTypeEnum.getEnumByCode(prizeBean.getTypeCode());
        if (enumByCode == PrizeTypeEnum.ZREO) {
            log.debug("未中奖 不记录中奖消息");
            return;
        }
        Customer customer = customerService.queryCustomerByCustomerId(prizeBean.getCid());
//        恭喜 " + nickName + " 获得1元红包
        StringBuilder prizeName = new StringBuilder("");
        switch (enumByCode) {
            case RANDOM_GOLD:
                prizeName.append(prizeBean.getPrizeNum()).append("金豆");
                break;
            default:
                prizeName.append(prizeBean.getName());
                break;
        }
        StringBuilder stringBuilder = new StringBuilder("恭喜 ");
        stringBuilder.append(customer.getNickName()).append(" 获得")
                .append(prizeName);
        switch (prizeBean.getActiveName()) {
            case "A":
                msgBiz.addRealMsg4PaoMaDeng(stringBuilder.toString());
                break;
            default:
                break;
        }


    }

    @Override
    public void update() {

    }
}
