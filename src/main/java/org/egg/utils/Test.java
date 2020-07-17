package org.egg.utils;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.egg.integration.wx.WxConfig;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author cdt
 * @Description
 * @date: 2017/11/7 15:58
 */
public class Test {
    public static void main(String[] args) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        String path = "12.32";
        System.out.println(path.contains("."));
        System.out.println(path.contains("\\."));
    }

    private static void testa(String notifyData ) throws Exception {
        WxConfig config = new WxConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData);  // 转换成map

        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            System.out.println("true");
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
        }
    }



}
