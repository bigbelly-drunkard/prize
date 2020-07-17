package org.egg.utils;

import org.egg.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by chendatao on 2018/7/27.
 */
public class WxUtilTest extends BaseTest {
    @Autowired
    private WxUtil wxUtil;

    @org.junit.Test
    public void getWxMerchantAccessTokenTest() {
        String wxMerchantAccessToken = wxUtil.getWxMerchantAccessToken();
        System.out.println(wxMerchantAccessToken);
    }


}
