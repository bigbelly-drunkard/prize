package org.egg.model.DTO;

import lombok.Data;

/**
 * @author dataochen
 * @Description timeStamp: '', // 支付签名时间戳，
 * nonceStr: '', // 支付签名随机串，不长于 32 位
 * package: '', //扩展字段，由商户传入
 * signType: '', // 签名方式，
 * paySign: '', // 支付签名
 * @date: 2020/2/24 19:12
 */
@Data
public class WxMiniRedResultDto {
    private String timeStamp;
    private String nonceStr;
    private String packageStr;
    private String signType;
    private String paySign;
}
