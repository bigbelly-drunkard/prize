package org.egg.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 微信支付工具类
 * @author chendatao
 */
public class WxPayUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxPayUtil.class);
//    /**
//     * 组装参数
//     *
//     * @param request
//     * @param publishOrderRes
//     * @param wxPrePayRequestDto
//     * @param engineContent
//     */
//    public static void convertPublishPay(HttpServletRequest request, PublishOrderRes publishOrderRes, WxPrePayRequestDto wxPrePayRequestDto, EngineContent engineContent,BigDecimal payAmount) {
//        wxPrePayRequestDto.setClientInfo(ClientUtils.getClientInfo(request));
//        wxPrePayRequestDto.setTotalAmount(payAmount);
//        EngineRequest engineRequest = engineContent.getEngineRequest();
//        engineRequest.setOrderNo(publishOrderRes.getOrderNo());
//        engineRequest.setTotalAmount(publishOrderRes.getPrice());
//        engineRequest.setPayAmount(payAmount);
//        engineRequest.setRemark("publish");
//        engineRequest.setPubAccountNo(publishOrderRes.getUserNo());
//        engineContent.setEngineRequest(engineRequest);
//    }

    /**
     * jsSdk签名
     * 用于初始化wx.config的参数
     *
     * @param paraMap
     * @return
     */
    public static String jsSdkSign(Map<String,String> paraMap) {
//        步骤1. 对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串string1：
        String s = EncryptionUtil.formatUrlMap(paraMap,false,true);
        LOGGER.info("jsSDKSign s={}",s);
//        步骤2. 对string1进行sha1签名，得到signature：
        String sha1 = EncryptionUtil.getSha1(s);
        LOGGER.info("jsSDKSign s={}",s);
        return sha1;
    }
    /**
     * 统一支付签名
     * @param paraMap
     * @return
     */
    public static String paySign(Map<String, String> paraMap) {

        //        步骤1. 对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串string1：
        String s = EncryptionUtil.formatUrlMap(paraMap,false,false);
        LOGGER.debug("pay sign s={}",s);
//        步骤2. 在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值signValue
        String s1 = EncryptionUtil.md5(s+"&key="+WxUtil.KEY , "utf-8").toUpperCase();
        LOGGER.debug("pay sign key={}",WxUtil.KEY);
        LOGGER.debug("pay sign s1={}", s1);
        return s1;
    }

}
