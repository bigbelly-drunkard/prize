package org.egg.handler;

import org.egg.model.DTO.PrizeBean;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/4 9:49
 */
public interface SendPrizeHandler {
    /**
     * 发送奖品
     * @param customerId
     * @param prizeBean
     */
    public void sendPrize(String customerId, PrizeBean prizeBean);
}
