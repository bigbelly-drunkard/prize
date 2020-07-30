package org.egg.integration.mock;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chendatao on 2018/7/21.
 * 对接网关mock
 */
public class WxMock {

    /**
     * {"nonce_str":"hGyPP23V0jw3PEVL","device_info":"WEB","appid":"wx9398d1dc8d97a45c","sign":"CB3D38A6BAF5488E45E26895A233ED75","trade_type":"JSAPI","return_msg":"OK","result_code":"SUCCESS","mch_id":"1501091921","return_code":"SUCCESS","prepay_id":"wx18230506490975a5273a95ac0212957993"}
     * @param data
     * @return
     */
    public static Map<String, String>  prepaymock(Map<String, String> data) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("prepay_id", "testprepay_id");
        stringStringHashMap.put("return_code", "SUCCESS");
        stringStringHashMap.put("result_code", "SUCCESS");
        stringStringHashMap.put("return_msg", "OK");
        stringStringHashMap.put("device_info", "WEB");
        stringStringHashMap.put("appid", "wx7e798ee145711568");
        stringStringHashMap.put("sign", "CB3D38A6BAF5488E45E26895A233ED75");
        stringStringHashMap.put("trade_type", "JSAPI");
        stringStringHashMap.put("mch_id", "1501091921");
        return stringStringHashMap;
    }
}
