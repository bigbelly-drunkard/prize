package org.egg.model.DTO;

import lombok.Data;

/**
 * Created by chendatao on 2019/12/8.
 * * https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_3
 */
@Data
public class WxQuery4CompanyApiReqDto {
    private String nonce_str;
    private String sign;
    private String partner_trade_no;
    private String mch_id;
    private String app_id;
}
