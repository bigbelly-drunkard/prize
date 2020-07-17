package org.egg;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import org.egg.integration.wx.WxMiniConfig;
import org.egg.utils.HttpRequestUtil;
import org.egg.utils.WxMiniUtil;
import org.egg.utils.WxPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dataochen
 * @Description
 * @date: 2019/1/2 14:08
 */
public class WxApiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxApiTest.class);
    public static void main(String[] args) {
        try {
//            unifiedOrder();
            sendTemMs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getmerchatToken() {
            String url = new StringBuilder("https://api.weixin.qq.com/cgi-bin/token").append("?grant_type=client_credential&").
                    append("appid=").append("wx7e798ee145711568")
                    .append("&secret=").append("f6683ae2a0af7d028d5043b93908a325")
                    .toString();
            String result = HttpRequestUtil.get(url);
            LOGGER.debug("getWxMerchantAccessToken result={}", result);
            JSONObject jsonObject = JSONObject.parseObject(result);
        }

    public static void sendTemMs() {
        String ACCESS_TOKEN = "17_KxnpJsOauVd8nPedxo9U7JpVfni_SxgGgUIQtFgiw9fpFQ_1hywokakanCg97EigMWxgwaUGYsxhsMRFZ3wet7zeC06xA7uAzoH-tPCp9FvXUsK1TEh8b47KCPwd9nkD6JtyxjTCho_0WAPCCGPiAFAQUD";
        String touser = "oE9L54kY9j7Usxvq-wXYKrAjl6rc";
        String template_id = "42hBVTtFzAUfHVojdKAEI1Urx_NhVKIl4s0IoAmojFI";
        String form_id = "wx03143105133105bf8327a5a81118015417";
        Map<String, String> data = new HashMap<String, String>(16);
        data.put("touser", touser);
        data.put("template_id", template_id);
        data.put("form_id", form_id);
//        WxTemplateMsgData wxTemplateMsgData = new WxTemplateMsgData();
//        data.put("data", JSONObject.toJSONString(wxTemplateMsgData));

        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+ACCESS_TOKEN;
        try {
            System.out.println("compay data="+JSONObject.toJSONString(data));
            String post = HttpRequestUtil.post(url, JSONObject.toJSONString(data));
            System.out.println("compay result="+post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void companyPay(){
        String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
        WxMiniConfig config = null;
        try {
            config = new WxMiniConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }

        WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, false);
        Map<String, String> data = new HashMap<String, String>(16);
        data.put("mch_appid", WxMiniUtil.APP_ID);
        data.put("mchid", WxMiniUtil.MCH_ID);
        Date now = new Date();
        String nonce_str = String.valueOf(Math.random() * now.getTime() + now.getTime());
        data.put("nonce_str", nonce_str);
        // TODO: 2019/1/2
        data.put("partner_trade_no", "testpartnertradeno");
        data.put("openid", "oE9L54n9gqyHGf82EYmE5Wk-uYcY");
        data.put("check_name", "NO_CHECK");
        data.put("amount", "100");
        data.put("desc", "提现");
        data.put("spbill_create_ip", "192.168.0.1");
        data.put("sign", WxPayUtil.paySign(data));
        try {
            System.out.println("compay data="+JSONObject.toJSONString(data));
            String s = wxpay.requestWithCert(url, data, config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs());
            System.out.println("compay result="+s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unifiedOrder() {
        WxMiniConfig config = null;
        try {
            config = new WxMiniConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, false);
        Map<String, String> data = new HashMap<String, String>(16);
//        data.put("appid", "wx7e798ee145711568");
//        data.put("mch_id", "1501091921");

        Date now = new Date();
        String nonce_str = String.valueOf(Math.random() * now.getTime() + now.getTime());
        data.put("nonce_str", nonce_str);
//        商品描述 商品简单描述，该字段请按照规范传递，具体请见参数规定 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2
        data.put("body", "testbody");
//        商户订单号 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。详见商户订单号 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2
        data.put("out_trade_no", "testout_trade_no");
//        标价币种 符合ISO 4217标准的三位字母代码，默认人民币：CNY，详细列表请参见货币类型 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2
        data.put("fee_type", "CNY");
//        标价金额 订单总金额，单位为分，详见支付金额 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2
        data.put("total_fee", (BigDecimal.TEN).toPlainString());
//    终端IP APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
        data.put("spbill_create_ip", "121.69.11.141");
//        通知地址 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
        data.put("notify_url", new StringBuilder("http://localhost/").append("wx/wxNotify").toString());
        data.put("trade_type", "JSAPI");
        data.put("openid", "oE9L54kY9j7Usxvq-wXYKrAjl6rc");  //
        data.put("attach", "testattach");
        data.put("sign", WxPayUtil.paySign(data));

        try {
            System.out.println("data="+ JSONObject.toJSONString(data, SerializerFeature.WriteMapNullValue));
            Map<String, String> stringStringMap = wxpay.unifiedOrder(data);
            System.out.println("stringStringMap="+ JSONObject.toJSONString(stringStringMap, SerializerFeature.WriteMapNullValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
