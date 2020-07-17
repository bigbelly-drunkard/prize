package org.egg.model.DTO;

import java.io.Serializable;

/**
 * @author cdt
 * @Description 微信查询订单和通知 公共属性实体
 * @date: 2018/4/6 14:18
 */
public class WxUDto implements Serializable {
    private String return_code;
    private String return_msg;
    private String appid;
    private String mch_id;
    private String device_info;
    private String sign;
    private String result_code;
    private String err_code;
    private String err_code_des;
    private String openid;
    private String is_subscribe;
    private String trade_type;

    private String bank_type;
    private String total_fee;
    private String settlement_total_fee;
    private String fee_type;
    private String cash_fee;
    private String cash_fee_type;
    private String coupon_fee;
    private String coupon_count;
    private String coupon_type_$n;
    private String coupon_id_$n	;
    private String coupon_fee_$n	;
    private String transaction_id;
    private String out_trade_no;
    private String attach;
    private String time_end;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getIs_subscribe() {
        return is_subscribe;
    }

    public void setIs_subscribe(String is_subscribe) {
        this.is_subscribe = is_subscribe;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getSettlement_total_fee() {
        return settlement_total_fee;
    }

    public void setSettlement_total_fee(String settlement_total_fee) {
        this.settlement_total_fee = settlement_total_fee;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(String cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getCash_fee_type() {
        return cash_fee_type;
    }

    public void setCash_fee_type(String cash_fee_type) {
        this.cash_fee_type = cash_fee_type;
    }

    public String getCoupon_fee() {
        return coupon_fee;
    }

    public void setCoupon_fee(String coupon_fee) {
        this.coupon_fee = coupon_fee;
    }

    public String getCoupon_count() {
        return coupon_count;
    }

    public void setCoupon_count(String coupon_count) {
        this.coupon_count = coupon_count;
    }

    public String getCoupon_type_$n() {
        return coupon_type_$n;
    }

    public void setCoupon_type_$n(String coupon_type_$n) {
        this.coupon_type_$n = coupon_type_$n;
    }

    public String getCoupon_id_$n() {
        return coupon_id_$n;
    }

    public void setCoupon_id_$n(String coupon_id_$n) {
        this.coupon_id_$n = coupon_id_$n;
    }

    public String getCoupon_fee_$n() {
        return coupon_fee_$n;
    }

    public void setCoupon_fee_$n(String coupon_fee_$n) {
        this.coupon_fee_$n = coupon_fee_$n;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"return_code\":\"")
                .append(return_code).append('\"');
        sb.append(",\"return_msg\":\"")
                .append(return_msg).append('\"');
        sb.append(",\"appid\":\"")
                .append(appid).append('\"');
        sb.append(",\"mch_id\":\"")
                .append(mch_id).append('\"');
        sb.append(",\"device_info\":\"")
                .append(device_info).append('\"');
        sb.append(",\"sign\":\"")
                .append(sign).append('\"');
        sb.append(",\"result_code\":\"")
                .append(result_code).append('\"');
        sb.append(",\"err_code\":\"")
                .append(err_code).append('\"');
        sb.append(",\"err_code_des\":\"")
                .append(err_code_des).append('\"');
        sb.append(",\"openid\":\"")
                .append(openid).append('\"');
        sb.append(",\"is_subscribe\":\"")
                .append(is_subscribe).append('\"');
        sb.append(",\"trade_type\":\"")
                .append(trade_type).append('\"');
        sb.append(",\"bank_type\":\"")
                .append(bank_type).append('\"');
        sb.append(",\"total_fee\":\"")
                .append(total_fee).append('\"');
        sb.append(",\"settlement_total_fee\":\"")
                .append(settlement_total_fee).append('\"');
        sb.append(",\"fee_type\":\"")
                .append(fee_type).append('\"');
        sb.append(",\"cash_fee\":\"")
                .append(cash_fee).append('\"');
        sb.append(",\"cash_fee_type\":\"")
                .append(cash_fee_type).append('\"');
        sb.append(",\"coupon_fee\":\"")
                .append(coupon_fee).append('\"');
        sb.append(",\"coupon_count\":\"")
                .append(coupon_count).append('\"');
        sb.append(",\"coupon_type_$n\":\"")
                .append(coupon_type_$n).append('\"');
        sb.append(",\"coupon_id_$n\":\"")
                .append(coupon_id_$n).append('\"');
        sb.append(",\"coupon_fee_$n\":\"")
                .append(coupon_fee_$n).append('\"');
        sb.append(",\"transaction_id\":\"")
                .append(transaction_id).append('\"');
        sb.append(",\"out_trade_no\":\"")
                .append(out_trade_no).append('\"');
        sb.append(",\"attach\":\"")
                .append(attach).append('\"');
        sb.append(",\"time_end\":\"")
                .append(time_end).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
