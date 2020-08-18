package org.egg.utils;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.egg.integration.wx.WxConfig;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author cdt
 * @Description
 * @date: 2017/11/7 15:58
 */
public class Test {
    public static void main(String[] args) throws IllegalAccessException, IntrospectionException, InvocationTargetException, IOException {
        double cos = Math.cos(2000 / 1000d);
        double cos2 = Math.cos(1000 / 1000d);
        System.out.println(cos);
        System.out.println(cos2);
    }

    private static void testa(String notifyData) throws Exception {
        WxConfig config = new WxConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData);  // 转换成map

        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            System.out.println("true");
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
        }
    }

    private static void pp() {
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        AtomicInteger atomicInteger2 = new AtomicInteger(0);
        AtomicInteger atomicInteger3 = new AtomicInteger(0);
        AtomicInteger atomicInteger4 = new AtomicInteger(0);
        AtomicInteger atomicInteger5 = new AtomicInteger(0);
        Integer i = 1;
        Integer i2 = 2;
        Integer i3 = 5;
        Integer i4 = 10;
        Integer total = 1000000;
        Integer tt=total;
        for (int j = 0; j < tt; j++) {
            double random = Math.random() * 100;
            if (random < 2.5) {
                atomicInteger4.incrementAndGet();
                total -= 10;

            } else if (random < 7.5) {
                atomicInteger3.incrementAndGet();
                total -= 5;

            } else if (random < 20) {
                atomicInteger2.incrementAndGet();
                total -= 2;
            } else if (random < 45) {
                atomicInteger.incrementAndGet();
                total -= 1;
            } else {
                atomicInteger5.incrementAndGet();

            }
        }
        System.out.println("total="+total);
        System.out.println(atomicInteger);
        System.out.println(atomicInteger2);
        System.out.println(atomicInteger3);
        System.out.println(atomicInteger4);
        System.out.println(atomicInteger5);
    }


}
