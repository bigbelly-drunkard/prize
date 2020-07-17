package org.egg.utils;

import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.egg.enums.CommonErrorEnum;
import org.egg.exception.CommonException;
import org.egg.model.DO.WxAccessToken;
import org.egg.model.DTO.WxMiniTemplateMsgReqDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;
import java.util.Date;

/**
 * @author cdt
 * @Description 微信工具类
 * @date: 2018/1/19 15:20
 */
@Component
public class WxMiniUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxMiniUtil.class);
    /**
     * 根据code获取网页授权accessToken url
     */
    @Value("${mini.access.token.url}")
    private String ACCESS_TOKEN_URL;

    /**
     * 获取accessToken url
     */
    @Value("${access.token.merchant.url}")
    private String ACCESS_TOKEN_MERCHANT_URL;

    /**
     * 小程序的唯一标识
     */
    @Value("${mini.app.secret}")
    private String SECRET;
    /**
     * 发送微信小程序模板消息url
     */
    @Value("${mini.message.send.url}")
    private String WX_MINI_MESSAGE_SEND_URL;

    /**
     * 小程序的appid
     */
    public static String APP_ID = PropertiesUtil.getProperty("mini.app.id");
    /**
     * 商户号
     */
    public static String MCH_ID = PropertiesUtil.getProperty("mch.id");
    /**
     * 商户平台密钥
     */
    public static String KEY = PropertiesUtil.getProperty("mch.key");


    @Value("${mini.app.id}")
    public void setAppId(String appId) {
        if (null != appId) {
            APP_ID = appId;
        } else {
            APP_ID = PropertiesUtil.getProperty("mini.app.id");
        }
    }

    @Value("${mch.id}")
    public void setMchId(String mchId) {
        if (null != mchId) {
            MCH_ID = mchId;
        } else {
            MCH_ID = PropertiesUtil.getProperty("mch.id");
        }
    }

    @Value("${mch.key}")
    public void setKEY(String KEY) {

        if (null != KEY) {
            KEY = KEY;
        } else {
            KEY = PropertiesUtil.getProperty("mch.key");
        }
    }

    @Value("${wx.mock.flag}")
    private String MOCK_FLAG;

    /**
     * 获取网页授权accessToken
     *
     * @param code
     * @return
     */
    public String getAccessToken(String code) {
        LOGGER.debug("getAccessToken code={}", code);
        String url = new StringBuilder(ACCESS_TOKEN_URL).append("?").append("appid=").append(APP_ID)
                .append("&secret=").append(SECRET).append("&js_code=").append(code).append("&grant_type=authorization_code")
                .toString();
        String result = HttpRequestUtil.get(url);
        LOGGER.debug("getAccessToken result={}", result);
        return result;

    }


    /**
     * 获取微信小程序普通access_token
     * <p>
     * {"access_token":"ACCESS_TOKEN","expires_in":7200}
     * {"errcode":40013,"errmsg":"invalid appid"}
     * 返回码	说明
     * -1	系统繁忙，此时请开发者稍候再试
     * 0	请求成功
     * 40001	AppSecret错误或者AppSecret不属于这个小程序，请开发者确认AppSecret的正确性
     * 40002	请确保grant_type字段值为client_credential
     * 40164	调用接口的IP地址不在白名单中，请在接口IP白名单中进行设置
     *
     * @return {"access_token":"ACCESS_TOKEN","expires_in":7200}
     */
    public String getWxMerchantAccessToken() throws CommonException {
        String url = new StringBuilder(ACCESS_TOKEN_MERCHANT_URL).append("?grant_type=client_credential&").
                append("appid=").append(APP_ID)
                .append("&secret=").append(SECRET)
                .toString();
        String result = HttpRequestUtil.get(url);
        LOGGER.debug("getWxMerchantAccessToken result={}", result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (null != jsonObject.get("errcode")) {
            throw new CommonException(new StringBuffer(CommonErrorEnum.WX_ACCESS_TOKEN_MERCHANT_ERROR.getCode())
                    .append("-").append(jsonObject.get("errcode")).toString(), new StringBuffer(CommonErrorEnum.WX_ACCESS_TOKEN_MERCHANT_ERROR.getDescription())
                    .append("-").append(jsonObject.get("errmsg")).toString());
        }
        return result;
    }


    /**
     * 检查普通token是否超时
     *
     * @param wxAccessToken
     * @return
     */
    public Boolean checkExpireAccessTokenForMerchant(WxAccessToken wxAccessToken) {
        //失效时间=modified_date+expire_in;微信平台 失效时间会延迟5分钟，新老token都可用
        Date modifiedDate = wxAccessToken.getModifiedDate();
        Integer expiresIn = wxAccessToken.getExpiresIn();
        Date date = DateUtil.addSecond(wxAccessToken.getExpiresIn(), modifiedDate);
        if (new Date().compareTo(date) == 1) {
//            超时
            return true;
        }
        return false;
    }

    private static boolean hasInited = false;

    public static void init() {
        if (hasInited) {
            return;
        }
        Security.addProvider(new BouncyCastleProvider());
        hasInited = true;
    }

    public static String decryData(String encryptedData, String iv, String sessionKey) throws Exception {

        String decryptString = "";
        init();
        byte[] sessionKeyByte = EncryptionUtil.decryptBASE64(sessionKey);
        byte[] ivByte = EncryptionUtil.decryptBASE64(iv);
        byte[] encryptDataByte = EncryptionUtil.decryptBASE64(encryptedData);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        Key key = new SecretKeySpec(sessionKeyByte, "AES");
        AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("AES");
        algorithmParameters.init(new IvParameterSpec(ivByte));
        cipher.init(Cipher.DECRYPT_MODE, key, algorithmParameters);
        byte[] bytes = cipher.doFinal(encryptDataByte);
        decryptString = new String(bytes);

        return decryptString;
    }
    public String sendTemplateMessage(WxMiniTemplateMsgReqDto wxMiniTemplateMsgReqDto, String access_token) {
        LOGGER.info("sendTemplateMessage wxTemplateMsgReqDto={}", JSONObject.toJSONString(wxMiniTemplateMsgReqDto));
        String result = HttpRequestUtil.post(WX_MINI_MESSAGE_SEND_URL.concat(access_token), JSONObject.toJSONString(wxMiniTemplateMsgReqDto));
        LOGGER.info("sendTemplateMessage result={}", result);
        return result;
    }
    public static void main(String[] args) throws Exception {
        String $appid = "wx4f4bc4dec97d474b";
        String $sessionKey = "tiihtNczf5v6AKRyjwEUhQ==";

        String $encryptedData = "CiyLU1Aw2KjvrjMdj8YKliAjtP4gsMZMQmRzooG2xrDcvSnxIMXFufNstNGTyaGS9uT5geRa0W4oTOb1WT7fJlAC+oNPdbB+3hVbJSRgv+4lGOETKUQz6OYStslQ142dNCuabNPGBzlooOmB231qMM85d2/fV6ChevvXvQP8Hkue1poOFtnEtpyxVLW1zAo6/1Xx1COxFvrc2d7UL/lmHInNlxuacJXwu0fjpXfz/YqYzBIBzD6WUfTIF9GRHpOn/Hz7saL8xz+W//FRAUid1OksQaQx4CMs8LOddcQhULW4ucetDf96JcR3g0gfRK4PC7E/r7Z6xNrXd2UIeorGj5Ef7b1pJAYB6Y5anaHqZ9J6nKEBvB4DnNLIVWSgARns/8wR2SiRS7MNACwTyrGvt9ts8p12PKFdlqYTopNHR1Vf7XjfhQlVsAJdNiKdYmYVoKlaRv85IfVunYzO0IKXsyl7JCUjCpoG20f0a04COwfneQAGGwd5oa+T8yO5hzuyDb/XcxxmK01EpqOyuxINew==";

        String $iv = "r7BXXKkLb8qrSNn05n0qiA==";
        String s = decryData($encryptedData, $iv, $sessionKey);
        System.out.println(s);

    }
}
