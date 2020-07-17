package org.egg.service.impl;

import org.egg.BaseTest;
import org.egg.enums.WxTemplateMsgTypeEnum;
import org.egg.model.DO.PubOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by chendatao on 2020/2/23.
 */
public class WechatServiceImplTest extends BaseTest {
    @Autowired
    private WechatServiceImpl wechatService;
    @Test
    public void sendWxTemplateMsg4Mini() throws Exception {
        PubOrder pubOrder = new PubOrder();
        pubOrder.setOrderNo("41550895212EEE1");
        pubOrder.setStatus("ADMIN_CLOSE");
        wechatService.sendWxTemplateMsg4Mini("1", pubOrder, null, WxTemplateMsgTypeEnum.ADMIN_CLOSE_MINI_PUB);
    }
    @Test
    public void createdMenu() {
        wechatService.createdMenu();
    }

    @Test
    public void sendWxTemplateMsg4Subscription() {
        String openId = "olNjI1Hq_pnkTdIe7r-T2SRF5Lf4";
        wechatService.sendWxTemplateMsg4Subscription(openId);
    }

}