package org.egg.model.DTO;

import lombok.Data;

/**
 * Created by chendatao on 2019/12/8.
 * 企业付款查询结果接口
 * https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_3
 */
@Data
public class WxQuery4CompanyApiResultDto {
    private String return_code;
    private String return_msg;
    private String result_code;
    private String err_code;
    private String err_code_des;
    private String partner_trade_no;
    private String appid;
    private String mch_id;
    private String detail_id;
    /**
     * SUCCESS:转账成功
     * <p>
     * FAILED:转账失败
     * <p>
     * PROCESSING:处理中
     */
    private String status;
    private String reason;
    private String openid;
    private String transfer_name;
    private int payment_amount;
    private String transfer_time;
    private String payment_time;
    private String desc;
}
