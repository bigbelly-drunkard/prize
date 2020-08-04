package org.egg.handler.Observer;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.egg.biz.LoadFactorBiz;
import org.egg.handler.SendPrizeHandler;
import org.egg.model.DTO.PrizeBean;
import org.egg.observer.Observer;
import org.egg.utils.CustomerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dataochen
 * @Description 发送奖品
 * @date: 2020/8/4 18:28
 */
@Slf4j
@Component("prizeSendObserver")
public class PrizeSendObserver implements Observer {
    @Autowired
    private Map<String, SendPrizeHandler> sendPrizeHandlerMap;


    @Override
    public void update(Object obj) {
        PrizeBean obj1 = (PrizeBean) obj;
        SendPrizeHandler sendPrizeHandler = sendPrizeHandlerMap.get(obj1.getTypeCode());
        if (sendPrizeHandler == null) {
            log.error("无sendPrizeHandler obj1={}", JSONObject.toJSONString(obj1));
            return;
        }
        sendPrizeHandler.sendPrize(CustomerUtil.getCustomer().getCustomerNo(),obj1);

    }

    @Override
    public void update() {

    }
}
