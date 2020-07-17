package org.egg.integration.wx;

import org.egg.BaseTest;
import org.egg.model.DTO.WxCompanyPayRequestDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by chendatao on 2020/2/20.
 */
public class WxCommonApiTest extends BaseTest {
    @Autowired
    private WxCommonApi wxCommonApi;

    @Test
    public void companyPay() throws Exception {
//        {"nonce_str":"1.7339196142619736E12","amount":"9","mchid":"1545385531","openid":"oD9tp5OW_wYQz72cfxWVU3l7NVGs","partner_trade_no":"716652805350",
// "mch_appid":"wx7d53782fc19bb0f4","sign":"8EAC3B020B4FF435443C28FF8B96F360","check_name":"NO_CHECK","spbill_create_ip":"47.98.196.102"}
        WxCompanyPayRequestDto wxCompanyPayRequestDto = new WxCompanyPayRequestDto();
        wxCompanyPayRequestDto.setMiniOpenId("oD9tp5OW_wYQz72cfxWVU3l7NVGs");
        wxCompanyPayRequestDto.setIp("47.98.196.102");
        wxCompanyPayRequestDto.setOutTradeNo("716652805350");
        wxCompanyPayRequestDto.setTotalAmount(new BigDecimal("0.9"));
        wxCommonApi.companyPay(wxCompanyPayRequestDto);
    }

    @Test
    public void sendMiniRedPackage() {
        String openId = "oD9tp5OW_wYQz72cfxWVU3l7NVGs";
        try {
            wxCommonApi.sendMiniRedPackage(openId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendRedPackage() throws Exception {
        String openId = "oD9tp5OW_wYQz72cfxWVU3l7NVGs";
        wxCommonApi.sendRedPackage(openId, false,new BigDecimal("1"));
    }

    @Test
    public void sendRedPackageGroup() throws Exception {
        String openId = "oD9tp5OW_wYQz72cfxWVU3l7NVGs";
        wxCommonApi.sendRedPackageGroup(openId, false);
    }

    @Test
    public void sendCoupon() throws Exception {
        String openId = "olNjI1Hq_pnkTdIe7r-T2SRF5Lf4";
        wxCommonApi.sendCoupon(openId,false);
    }
}