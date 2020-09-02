package org.egg.integration.wx;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.jdcloud.sdk.utils.StringUtils;
import org.egg.enums.CommonErrorEnum;
import org.egg.exception.CommonException;
import org.egg.integration.mock.WxMock;
import org.egg.model.DTO.*;
import org.egg.response.BaseResult;
import org.egg.utils.BeanUtil;
import org.egg.utils.WxMiniUtil;
import org.egg.utils.WxPayUtil;
import org.egg.utils.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cdt
 * @Description 微信支付文档：https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
 * @date: 2018/3/29 16:39
 */
@Component
public class WxCommonApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxCommonApi.class);


    @Value("${wx.sandbox.flag}")
    private String SANDBOX_FLAG;
    @Value("${wx.mock.flag}")
    private String MOCK_FLAG;
    @Value("${company.pay.url}")
    private String COMPANY_PAY_URL;
    @Value("${company.query.url}")
    private String COMPANY_QUERY_URL;
    /**
     * 发送现金红包url
     */
    @Value("${send.red.package.url}")
    private String SEND_RED_PACKAGE_URL;
    /**
     * 发送裂变红包url
     */
    @Value("${send.group.red.package.url}")
    private String SEND_GROUP_RED_PACKAGE_URL;
    /**
     * 发送小程序红包url
     */
    @Value("${send.mini.red.package.url}")
    private String SEND_MINI_RED_PACKAGE_URL;
    /**
     * 发送代金券url
     */
    @Value("${send.coupon.url}")
    private String SEND_COUPON_URL;

    /**
     * 公众号支付
     * <p>
     * com.github.wxpay.sdk.WXPay#fillRequestData(java.util.Map)覆盖填充了好多参数 注意此坑
     *
     * @param wxPrePayRequestDto
     * @return 预支付交易会话标识 prepay_id 微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
     */
    public WxPrePayResultDto pay(WxPrePayRequestDto wxPrePayRequestDto) {
        WxPrePayResultDto result = new WxPrePayResultDto();
        WXPayConfig config = null;
        try {
            if (wxPrePayRequestDto.getClientInfo().getMiniProgramFlag()) {
                config = new WxMiniConfig();
            } else {
                config = new WxConfig();
            }
        } catch (Exception e) {
            LOGGER.error("微信支付 加载证书失败 e={}", e);
            return null;
        }
        Date now = new Date();
        Map<String, String> data = new HashMap<String, String>(16);

        if (wxPrePayRequestDto.getClientInfo().getMiniProgramFlag()) {
//        设备号 自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
            data.put("device_info", "MINI");
        } else {
//        设备号 自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
            data.put("device_info", "WEB");

        }
//        商品描述 商品简单描述，该字段请按照规范传递，具体请见参数规定 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2
        data.put("body", "积分礼包");
//        商户订单号 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。详见商户订单号 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2
        data.put("out_trade_no", wxPrePayRequestDto.getOutTradeNo());
//        标价币种 符合ISO 4217标准的三位字母代码，默认人民币：CNY，详细列表请参见货币类型 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2
        data.put("fee_type", "CNY");
//        标价金额 订单总金额，单位为分，详见支付金额 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2
        data.put("total_fee", (wxPrePayRequestDto.getTotalAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP)).toPlainString());
//    终端IP APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
        data.put("spbill_create_ip", wxPrePayRequestDto.getClientInfo().getIp());
//        data.put("spbill_create_ip", "121.69.11.141");
//        通知地址 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
        data.put("notify_url", new StringBuilder(wxPrePayRequestDto.getClientInfo().getHost()).append("wx/wxNotify").toString());
//        交易类型 JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付，统一下单接口trade_type的传参可参考这里 MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口
        boolean miniProgramFlag = wxPrePayRequestDto.getClientInfo().getMiniProgramFlag();
        if (miniProgramFlag) {
            data.put("openid", wxPrePayRequestDto.getMiniOpenId());  // 此处指定为扫码支付
        } else {
            data.put("openid", wxPrePayRequestDto.getOpenId());  // 此处指定为扫码支付
        }
        if (null != wxPrePayRequestDto.getClientInfo().getClientSourceEnum()) {
            switch (wxPrePayRequestDto.getClientInfo().getClientSourceEnum()) {
                case ANDROID:
                case IOS:
                    switch (wxPrePayRequestDto.getClientInfo().getClientChromeEnum()) {
                        case WX:
                            data.put("trade_type", "JSAPI");
                            break;
                        case OTHER:
                            data.put("trade_type", "NATIVE");
                            break;
                    }
                    break;
                case PC://  2018/11/4 二维码？
                    data.put("trade_type", "NATIVE");
                    break;

            }
        } else {
            data.put("trade_type", "JSAPI");

        }

//        商户id trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
//        data.put("product_id", "12");
//附加数据 	附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。
        String attachJson = wxPrePayRequestDto.getAttachJson();
        if (!StringUtils.isBlank(attachJson)) {
            data.put("attach", wxPrePayRequestDto.getAttachJson());
        }
//     用户标识 trade_type=JSAPI时（即公众号支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识。
//        data.put("userNo", wxPrePayRequestDto.getUserNo());
//        data.put("sign", WxPayUtil.paySign(data));

        try {
            LOGGER.info("微信支付统一下单 MOCK_FLAG={},SANDBOX_FLAG={}, param={}", MOCK_FLAG, SANDBOX_FLAG, JSONObject.toJSONString(data, SerializerFeature.WriteMapNullValue));
            Map<String, String> resp = null;
            WXPay wxpay = null;
            //是否mock
            if (Boolean.valueOf(MOCK_FLAG)) {
                resp = WxMock.prepaymock(data);
            } else {
                //是否走微信平台的沙盒环境
                if (Boolean.valueOf(SANDBOX_FLAG)) {
                    wxpay = new WXPay(config, WXPayConstants.SignType.MD5, true);
                } else {
                    wxpay = new WXPay(config, WXPayConstants.SignType.MD5, false);
                }
                resp = wxpay.unifiedOrder(data);
            }
            LOGGER.info("微信支付统一下单 MOCK_FLAG={},SANDBOX_FLAG={},result={}", MOCK_FLAG, SANDBOX_FLAG, JSONObject.toJSONString(resp, SerializerFeature.WriteMapNullValue));
            String return_code = resp.get("return_code");
            String result_code = resp.get("result_code");
            if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {
                result.setPrepay_id(resp.get("prepay_id"));
                result.setMwebUrl(resp.get("mweb_url"));
                result.setCodeUrl(resp.get("code_url"));
                result.setPackageValue("prepay_id=" + result.getPrepay_id());
                String nonce_str2 = String.valueOf(Math.random() * now.getTime() + now.getTime());
                result.setNonceStr(nonce_str2);
                result.setTimeStamp(System.currentTimeMillis());
                Map<String, String> stringStringMap = new HashMap<>(16);
                if (wxPrePayRequestDto.getClientInfo().getMiniProgramFlag()) {
                    stringStringMap.put("appId", WxMiniUtil.APP_ID);
                } else {
                    stringStringMap.put("appId", WxUtil.APP_ID);
                }
                stringStringMap.put("timeStamp", result.getTimeStamp().toString());
                stringStringMap.put("nonceStr", result.getNonceStr());
                stringStringMap.put("package", result.getPackageValue());
                stringStringMap.put("signType", "MD5");
                result.setPaySign(WxPayUtil.paySign(stringStringMap));
                result.setAttach(wxPrePayRequestDto.getAttachJson());
                result.setOutTradeNo(wxPrePayRequestDto.getOutTradeNo());
                result.setPayType("RECHARGE");
            } else {
                LOGGER.error("微信支付统一下单失败");
                throw new CommonException(CommonErrorEnum.WX_PAY_ORDER_ERROR);
            }
        } catch (Exception e) {
            LOGGER.error("微信支付统一下单异常 e={}", e);
            throw new CommonException(CommonErrorEnum.WX_PAY_ORDER_ERROR);
        }
        return result;

    }

    /**
     * 校验通知签名
     *
     * @param notifyData 支付结果通知的xml格式数据
     * @return
     */
    public Boolean checkNotifySign(String notifyData, WxNotifyDto wxNotifyDto) {

        try {
            WxConfig config = new WxConfig();
            WXPay wxpay = new WXPay(config);

            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData);  // 转换成map

            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                // 签名正确
                BeanUtil.transMap2Bean2(notifyMap, wxNotifyDto);
                return true;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
            }
        } catch (Exception e) {
            LOGGER.error(" checkNotifySign e={}", e);
        }
        return false;

    }

    /**
     * 查询微信支付订单
     *
     * @param outTradeNo
     * @return
     */
    public WxQueryPayOrderDto queryPayOrder(String outTradeNo) {
        WxQueryPayOrderDto wxQueryPayOrderDto = new WxQueryPayOrderDto();
        WxMiniConfig config = null;
        try {
//            config = new WxConfig();
//             只支持微信小程序支付 后期流水表添加渠道字段来区分微信支付和小程序支付
            config = new WxMiniConfig();
        } catch (Exception e) {
            LOGGER.error("微信支付 加载证书失败 e={}", e);
            return wxQueryPayOrderDto;
        }
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", outTradeNo);

        try {
            Map<String, String> resp = wxpay.orderQuery(data);
            LOGGER.info("queryPayOrder resp={}", JSONObject.toJSONString(resp));
            String return_code = resp.get("return_code");
            String result_code = resp.get("result_code");
            if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {
                BeanUtil.transMap2Bean2(resp, wxQueryPayOrderDto);
            } else {
                LOGGER.error("微信支付查询订单单失败 resp={}", JSONObject.toJSONString(resp));
            }
        } catch (Exception e) {
            LOGGER.error(" queryPayOrder e={}", e);
        }
        return wxQueryPayOrderDto;

    }

    /**
     * 企业付款接口
     * 幂等
     *
     * @param wxCompanyPayRequestDto
     * @return
     *
     * err_code=NOTENOUGH, return_msg=支付失败,
     */
    public BaseResult companyPay(WxCompanyPayRequestDto wxCompanyPayRequestDto) {
        wxCompanyPayRequestDto.setIp("212.64.11.168");
        BaseResult result = new BaseResult();
        if (Boolean.valueOf(MOCK_FLAG)) {
            LOGGER.info("mock open");
            result.setError(CommonErrorEnum.SUCCESS);
            result.setSuccess(true);
            return result;
        }
        try {
            Map<String, String> data = new HashMap<String, String>(16);
            WXPayConfig config;
            if (StringUtils.isEmpty(wxCompanyPayRequestDto.getMiniOpenId())) {
                data.put("mch_appid", WxUtil.APP_ID);
                data.put("mchid", WxUtil.MCH_ID);
                data.put("openid", wxCompanyPayRequestDto.getOpenId());
                config = new WxConfig();
            } else {
                data.put("mch_appid", WxMiniUtil.APP_ID);
                data.put("mchid", WxMiniUtil.MCH_ID);
                data.put("openid", wxCompanyPayRequestDto.getMiniOpenId());
                config = new WxMiniConfig();
            }
            WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, false);
            Date now = new Date();
            String nonce_str = String.valueOf(Math.random() * now.getTime() + now.getTime());
            data.put("nonce_str", nonce_str);
            data.put("partner_trade_no", wxCompanyPayRequestDto.getOutTradeNo());
            data.put("check_name", "NO_CHECK");
            data.put("amount", (wxCompanyPayRequestDto.getTotalAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP)).toPlainString());
            data.put("desc", "奖励");
            data.put("spbill_create_ip", wxCompanyPayRequestDto.getIp());
            data.put("sign", WxPayUtil.paySign(data));
            LOGGER.info("companyPay data={}", JSONObject.toJSONString(data));
            String rst = wxpay.requestWithCert(COMPANY_PAY_URL, data, config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs());
            LOGGER.info("companyPay rst={}", rst);
            Map<String, String> stringStringMap = WXPayUtil.xmlToMap(rst);
            LOGGER.info("companyPay stringStringMap={}", stringStringMap);
            String return_code = stringStringMap.get("return_code");
            String result_code = stringStringMap.get("result_code");
            String err_code = stringStringMap.get("err_code");
            String err_code_des = stringStringMap.get("err_code_des");
            result.setRespCode(err_code);
            result.setRespDesc(err_code_des);
            if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {
                result.setSuccess(true);
            } else if ("SYSTEMERROR".equals(err_code)) {
                result.setSuccess(false);
                LOGGER.warn("微信企业付款不成功，等待定时拉取 return_code={},result_code={}", return_code, result_code);
            } else {
                result.setSuccess(false);
                LOGGER.error("微信企业付款失败 return_code={},result_code={}", return_code, result_code);
            }
        } catch (Exception e) {
            LOGGER.error("companyPay e={}", e);
            result.setRespCode(CommonErrorEnum.SYSTEM_EXCEPTION.getCode());
            result.setRespDesc(CommonErrorEnum.SYSTEM_EXCEPTION.getDescription());
        }
        return result;
    }

    /**
     * 小程序 查询企业付款结果
     *
     * @param outTradeNo
     * @return
     */
    public BaseResult queryResult4CompanyApi(String outTradeNo) {
        BaseResult result = new BaseResult();
        try {
            WXPayConfig config = new WxMiniConfig();
            WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, false);
            WxQuery4CompanyApiReqDto wxQuery4CompanyApiReqDto1 = new WxQuery4CompanyApiReqDto();
            wxQuery4CompanyApiReqDto1.setApp_id(WxMiniUtil.APP_ID);
            wxQuery4CompanyApiReqDto1.setMch_id(WxMiniUtil.MCH_ID);
            wxQuery4CompanyApiReqDto1.setPartner_trade_no(outTradeNo);
            Date now = new Date();
            String nonce_str = String.valueOf(Math.random() * now.getTime() + now.getTime());
            wxQuery4CompanyApiReqDto1.setNonce_str(nonce_str);
            LOGGER.info("queryResult4CompanyApi data={}", JSONObject.toJSONString(wxQuery4CompanyApiReqDto1));
            Map<String, String> data = BeanUtil.transBean2Map2(wxQuery4CompanyApiReqDto1);
            String rst = wxpay.requestWithCert(COMPANY_QUERY_URL, data, config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs());
            LOGGER.info("queryResult4CompanyApi rst={}", rst);
            Map<String, String> stringStringMap = WXPayUtil.xmlToMap(rst);
            LOGGER.info("queryResult4CompanyApi stringStringMap={}", stringStringMap);
            String return_code = stringStringMap.get("return_code");
            String status = stringStringMap.get("status");
            String err_code = stringStringMap.get("err_code");
            String err_code_des = stringStringMap.get("err_code_des");
            result.setRespCode(err_code);
            result.setRespDesc(err_code_des);
            if (!"SUCCESS".equals(return_code)) {
                return null;
            }
            switch (status) {
                case "SUCCESS":
                    result.setSuccess(true);
                    break;
                case "FAILED":
                    result.setSuccess(false);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("companyPay e={}", e);
            result.setRespCode(CommonErrorEnum.SYSTEM_EXCEPTION.getCode());
            result.setRespDesc(CommonErrorEnum.SYSTEM_EXCEPTION.getDescription());
        }
        return result;
    }

    /**
     * 退款申请
     * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4
     *fee 单位 分
     * @param outTradeNo 原商户订单号
     * @return
     *
     * "err_code":"NOTENOUGH" 基本账户余额不足，请充值后重新发起
     */
    public BaseResult refundApi(String outTradeNo, String outRefundNo, int totalFee, int refundFee, String refundMsg) {
        BaseResult result = BaseResult.builder();
        try {
            WXPayConfig config = new WxMiniConfig();
            WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, false);
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("out_trade_no", outTradeNo);
            stringStringHashMap.put("out_refund_no", outRefundNo);
            stringStringHashMap.put("total_fee", Math.abs(totalFee) + "");
            stringStringHashMap.put("refund_fee", Math.abs(refundFee) + "");
            if (StringUtils.isNotBlank(refundMsg)) {
                stringStringHashMap.put("refund_desc", refundMsg);
            }
            LOGGER.info("refundApi start={}", JSONObject.toJSONString(stringStringHashMap));
            Map<String, String> refund = wxpay.refund(stringStringHashMap);
            LOGGER.info("refundApi refund={}", JSONObject.toJSONString(refund));
            String return_code = refund.get("return_code");
            /**
             * SUCCESS退款申请接收成功，结果通过退款查询接口查询
             FAIL 提交业务失败
             */
            String result_code = refund.get("result_code");
            String err_code = refund.get("err_code");
            String err_code_des = refund.get("err_code_des");
            result.setRespCode(err_code);
            result.setRespDesc(err_code_des);
            if (!"SUCCESS".equals(return_code)) {
//                通过日志控制 退款失败的单子 暂时不开放自动补单退款（有风险）
                LOGGER.error("退款错误");
                result.setRespCode(CommonErrorEnum.WX_REFUND_FAIL.getCode());
                result.setRespDesc(CommonErrorEnum.WX_REFUND_FAIL.getDescription());
                result.setSuccess(false);
                return result;
            }
            switch (result_code) {
                case "SUCCESS":
                    result.setSuccess(true);
                    break;
                case "FAIL":
                    LOGGER.error("退款失败");
                    result.setRespCode(CommonErrorEnum.WX_REFUND_FAIL.getCode());
                    result.setRespDesc(CommonErrorEnum.WX_REFUND_FAIL.getDescription());
                    result.setSuccess(false);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("refundApi e={}", e);
            result.setRespCode(CommonErrorEnum.SYSTEM_EXCEPTION.getCode());
            result.setRespDesc(CommonErrorEnum.SYSTEM_EXCEPTION.getDescription());
        }
        return result;
    }

    /**
     * 发送现金红包
     * 暂不关系结果
     *
     * @param amount   单位元
     * @param openId
     * @param miniFlag https://pay.weixin.qq.com/wiki/doc/api/tools/cash_coupon.php?chapter=13_4&index=3
     *                 注意：当状态为FAIL时，存在业务结果未明确的情况。所以如果状态是FAIL，请务必再请求一次查询接口[请务必关注错误代码（err_code字段），通过查询得到的红包状态确认此次发放的结果。]，以确认此次发放的结果。
     * @throws Exception
     */
    public void sendRedPackage(String openId, boolean miniFlag, BigDecimal amount) throws Exception {
        WXPayConfig config = null;
        try {
            if (miniFlag) {
                config = new WxMiniConfig();
            } else {
                config = new WxConfig();
            }
        } catch (Exception e) {
            LOGGER.error("微信支付 加载证书失败 e={}", e);
            return;
        }
        WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, false);

        HashMap<String, String> param = new HashMap<>();
        param.put("mch_billno", System.currentTimeMillis() + "");
        param.put("wxappid", config.getAppID());
        param.put("send_name", "蚂蚁科技积分商城");
        param.put("re_openid", openId);
        BigDecimal bigDecimal = amount.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
        param.put("total_amount", bigDecimal.toString());
        param.put("total_num", 1 + "");
        param.put("wishing", "恭喜您获的红包，点击立即领取红包");
        InetAddress address = InetAddress.getLocalHost();
        param.put("client_ip", address.getHostAddress());
//        param.put("act_name", "感谢您关注公众号");
//         营销信息文案
        param.put("remark", "【蚂蚁科技积分商城】");


        param.put("mch_id", config.getMchID());
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        param.put("sign", WXPayUtil.generateSignature(param, config.getKey(), WXPayConstants.SignType.MD5));
        LOGGER.info("sendRedPackage stringStringMap={}", JSONObject.toJSONString(param));
        String resultXml = wxpay.requestWithCert(SEND_RED_PACKAGE_URL, param, 5000, 5000);
        LOGGER.info("sendRedPackage resultXml={}", resultXml);
        Map<String, String> stringStringMap2 = WXPayUtil.xmlToMap(resultXml);
        String return_code = stringStringMap2.get("return_code");
        String result_code = stringStringMap2.get("result_code");
        if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {
            LOGGER.info("发送红包成功");
        } else {
            LOGGER.error("发送红包失败");
        }
//        SEND_RED_PACKAGE_URL
    }

    /**
     * 发送裂变红包
     * 暂不关系结果
     *
     * @param openId
     * @param miniFlag https://pay.weixin.qq.com/wiki/doc/api/tools/cash_coupon.php?chapter=13_4&index=3
     *                 注意：当状态为FAIL时，存在业务结果未明确的情况。所以如果状态是FAIL，请务必再请求一次查询接口[请务必关注错误代码（err_code字段），通过查询得到的红包状态确认此次发放的结果。]，以确认此次发放的结果。
     * @throws Exception
     */
    public void sendRedPackageGroup(String openId, boolean miniFlag) throws Exception {
        WXPayConfig config = null;
        try {
            if (miniFlag) {
                config = new WxMiniConfig();
            } else {
                config = new WxConfig();
            }
        } catch (Exception e) {
            LOGGER.error("微信支付 加载证书失败 e={}", e);
            return;
        }
        WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, false);

        HashMap<String, String> param = new HashMap<>();
        param.put("mch_billno", System.currentTimeMillis() + "");
        param.put("wxappid", config.getAppID());
        param.put("send_name", "蚂蚁科技积分商城");
        param.put("re_openid", openId);
        double amount = Math.random() * 1;
        if (amount < 0.01) {
            amount = 0.01;
        }
        int amount1 = (int) (amount * 100);
        param.put("total_amount", 1000 + "");
        param.put("total_num", 10 + "");
        param.put("wishing", "点击立即领取红包");
        InetAddress address = InetAddress.getLocalHost();
        param.put("client_ip", address.getHostAddress());
//        param.put("act_name", "感谢您关注公众号");
//         营销信息文案
        param.put("remark", "【蚂蚁科技积分商城】");
        param.put("amt_type", "ALL_RAND");

        param.put("mch_id", config.getMchID());
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        param.put("sign", WXPayUtil.generateSignature(param, config.getKey(), WXPayConstants.SignType.MD5));
        LOGGER.info("sendRedPackageGroup stringStringMap={}", JSONObject.toJSONString(param));
        String resultXml = wxpay.requestWithCert(SEND_GROUP_RED_PACKAGE_URL, param, 5000, 5000);
        LOGGER.info("sendRedPackageGroup resultXml={}", resultXml);
        Map<String, String> stringStringMap2 = WXPayUtil.xmlToMap(resultXml);
        String return_code = stringStringMap2.get("return_code");
        String result_code = stringStringMap2.get("result_code");
        if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {
            LOGGER.info("发送裂变红包成功");
        } else {
            LOGGER.error("发送裂变红包失败");
        }
//        SEND_RED_PACKAGE_URL
    }

    /**
     * 发送小程序红包
     *
     * @param openId
     */
    public WxMiniRedResultDto sendMiniRedPackage(String openId) throws Exception {
        WxMiniRedResultDto wxMiniRedResultDto = new WxMiniRedResultDto();
        WXPayConfig config = null;
        try {
            config = new WxMiniConfig();
        } catch (Exception e) {
            LOGGER.error("微信支付 加载证书失败 e={}", e);
            return wxMiniRedResultDto;
        }
        WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, false);

        HashMap<String, String> param = new HashMap<>();
        param.put("mch_billno", System.currentTimeMillis() + "");
        param.put("wxappid", config.getAppID());
        param.put("send_name", "蚂蚁科技积分商城");
        param.put("re_openid", openId);
//        double amount = Math.random() * 0.1;
//        if (amount < 0.01) {
//            amount = 0.01;
//        }
//        int amount1 = (int) (amount*100);
        param.put("total_amount", 100 + "");
        param.put("total_num", 1 + "");
        param.put("wishing", "点击立即领取红包");
//        act_name字段必填,并且少于32个字符
//        param.put("act_name", "首次登录奖励红包");
//         营销信息文案
        param.put("remark", "xx");
        param.put("notify_way", "MINI_PROGRAM_JSAPI");

        param.put("mch_id", config.getMchID());
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        param.put("sign", WXPayUtil.generateSignature(param, config.getKey(), WXPayConstants.SignType.MD5));
        LOGGER.info("sendMiniRedPackage stringStringMap={}", JSONObject.toJSONString(param));
        String resultXml = wxpay.requestWithCert(SEND_MINI_RED_PACKAGE_URL, param, 5000, 5000);
        LOGGER.info("sendMiniRedPackage resultXml={}", resultXml);
        Map<String, String> stringStringMap2 = WXPayUtil.xmlToMap(resultXml);
        String return_code = stringStringMap2.get("return_code");
        String result_code = stringStringMap2.get("result_code");
        if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {
            LOGGER.info("发送小程序红包成功");
            wxMiniRedResultDto.setPackageStr(stringStringMap2.get("package"));
            wxMiniRedResultDto.setTimeStamp(System.currentTimeMillis() + "");
            wxMiniRedResultDto.setNonceStr(param.get("nonce_str"));
            wxMiniRedResultDto.setSignType(param.get("sign_type"));
            wxMiniRedResultDto.setPaySign(param.get("sign"));
        } else {
            LOGGER.error("发送小程序红包失败");
        }
        return wxMiniRedResultDto;
    }

    /**
     * 发代金券
     *
     * @param openId
     * @return
     * @throws Exception
     */
    public boolean sendCoupon(String openId, boolean miniFlag) throws Exception {
        WXPayConfig config = null;
        try {
            if (miniFlag) {
                config = new WxMiniConfig();
            } else {
                config = new WxConfig();
            }
        } catch (Exception e) {
            LOGGER.error("微信支付 加载证书失败 e={}", e);
            return false;
        }
        WXPay wxpay = new WXPay(config, WXPayConstants.SignType.MD5, false);
        HashMap<String, String> param = new HashMap<>();
        param.put("coupon_stock_id", "9913294");
        param.put("openid_count", "1");
        param.put("partner_trade_no", System.currentTimeMillis() + "");
        param.put("openid", openId);
        param.put("appid", config.getAppID());
        param.put("mch_id", config.getMchID());
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        param.put("sign", WXPayUtil.generateSignature(param, config.getKey(), WXPayConstants.SignType.MD5));
        LOGGER.info("sendCoupon param={}", JSONObject.toJSONString(param));
        String resultXml = wxpay.requestWithCert(SEND_COUPON_URL, param, 5000, 5000);
        LOGGER.info("sendCoupon resultXml={}", resultXml);
        Map<String, String> stringStringMap2 = WXPayUtil.xmlToMap(resultXml);
        String return_code = stringStringMap2.get("return_code");
        String result_code = stringStringMap2.get("result_code");
        if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {
            LOGGER.info("发送代金券成功");
            return true;
        } else {
            LOGGER.error("发送代金券失败");
            return false;
        }
    }

}
