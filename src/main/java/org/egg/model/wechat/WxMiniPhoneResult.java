package org.egg.model.wechat;

import lombok.Data;

/**
 * @author dataochen
 * @Description {
 * "phoneNumber": "13580006666",
 * "purePhoneNumber": "13580006666",
 * "countryCode": "86",
 * "watermark":
 * {
 * "appid":"APPID",
 * "timestamp": TIMESTAMP
 * }
 * }
 * @date: 2020/1/17 15:21
 */
@Data
public class WxMiniPhoneResult {
    private String phoneNumber;
    private String purePhoneNumber;
    private String countryCode;
    private Watermark watermark;
}
