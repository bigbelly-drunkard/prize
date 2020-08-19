//package org.egg.biz;
//
//import org.egg.BaseTest;
//import org.egg.model.DTO.WxUDto;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * Created by chendatao on 2018/7/21.
// */
//public class WxPayBizTest extends BaseTest {
//    @Autowired
//    private WxPayBiz wxPayBiz;
//
//    /**
//     * 充值通知
//     */
//    @Test
//    public void rechargeNotify() {
//        //String s = "{}";
//        //WxUDto wxUDto = JSONObject.parseObject(s, WxUDto.class);
//        WxUDto wxUDto = new WxUDto();
//        wxUDto.setOut_trade_no("10201808190156289179626");
//        wxUDto.setTotal_fee("89800");
//        wxUDto.setResult_code("SUCCESS");
//        wxUDto.setAttach("{\"payType\":\"RECHARGE\",\"recordNo\":\"7201808190156288631719\",\"userNo\":\"5201807281826399883911\"}");
//        wxPayBiz.wxUCommon(wxUDto);
//        try {
//            Thread.sleep(10000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//    /**
//     * 发布通知
//     */
//    @Test
//    public void publishNotify() {
//        //String s = "";
//        //WxUDto wxUDto = JSONObject.parseObject(s, WxUDto.class);
//        WxUDto wxUDto = new WxUDto();
//        wxUDto.setOut_trade_no("10201808081656175521889");
//        wxUDto.setTotal_fee("600");
//        wxUDto.setResult_code("SUCCESS");
//        wxUDto.setAttach("{\"payType\":\"PUB\",\"recordNo\":\"7201808081656175111138\",\"userNo\":\"5201807281826399883911\"}");
//        wxPayBiz.wxUCommon(wxUDto);
//    }
//}
