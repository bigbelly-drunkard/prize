package org.egg.utils;

import com.alibaba.fastjson.JSONObject;
import org.egg.enums.CommonErrorEnum;
import org.egg.exception.CommonException;
import org.egg.model.DO.WxAccessToken;
import org.egg.model.DO.WxAccessTokenUser;
import org.egg.model.DTO.WxTemplateMsgData;
import org.egg.model.DTO.WxTemplateMsgDataItem;
import org.egg.model.DTO.WxTemplateMsgReqDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author cdt
 * @Description 微信工具类
 * @date: 2018/1/19 15:20
 */
@Component
public class WxUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxUtil.class);
    /**
     * 根据code获取网页授权accessToken url
     */
    @Value("${access.token.url}")
    private String ACCESS_TOKEN_URL;
    /**
     * 刷新网页授权access_token url
     */
    @Value("${refresh.token.url}")
    private String REFRESH_TOKEN_URL;
    /**
     * 拉取用户信息(需scope为 snsapi_userinfo) url
     */
    @Value("${user.info.url}")
    private String USER_INFO_URL;
    /**
     * 检验授权凭证（网页授权access_token）是否有效 url
     */
    @Value("${check.token.url}")
    private String CHECK_TOKEN_URL;

    /**
     * 获取accessToken url
     */
    @Value("${access.token.merchant.url}")
    private String ACCESS_TOKEN_MERCHANT_URL;
    /**
     * 个性化菜单 url
     */
    @Value("${wx.menu.url}")
    private String WX_MENU_URL;


    /**
     * jsapi_ticket url
     */
    @Value("${jsapi_ticket.url}")
    private String JSAPI_TICKET_URL;
    /**
     * 公众号的唯一标识
     */
    @Value("${app.secret}")
    private String SECRET;

    /**
     * 发送微信模板消息url
     */
    @Value("${wx.message.send.url}")
    private String WX_MESSAGE_SEND_URL;

    /**
     * 公众号的appid
     */
    public static String APP_ID = PropertiesUtil.getProperty("app.id");
    /**
     * 商户号
     */
    public static String MCH_ID = PropertiesUtil.getProperty("mch.id");
    /**
     * 商户平台密钥
     */
    public static String KEY = PropertiesUtil.getProperty("mch.key");

    @Value("${app.id}")
    public void setAppId(String appId) {
        if (null != appId) {
            APP_ID = appId;
        } else {
            APP_ID = PropertiesUtil.getProperty("app.id");
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
                .append("&secret=").append(SECRET).append("&code=").append(code).append("&grant_type=authorization_code")
                .toString();
        String result = HttpRequestUtil.get(url);
        LOGGER.debug("getAccessToken result={}", result);
        return result;

    }


    /**
     * 网页授权refresh_token
     *
     * @param refresh_token
     * @return
     */
    public String refreshToken(String refresh_token) {
        LOGGER.debug("refreshToken code={}", refresh_token);
        String url = new StringBuilder(REFRESH_TOKEN_URL).append("?").append("appid=").append(APP_ID)
                .append("&grant_type=refresh_token&refresh_token=").append(refresh_token)
                .toString();
        String result = HttpRequestUtil.get(url);
        LOGGER.debug("refreshToken result={}", result);
        return result;
    }

    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     *
     * @param access_token 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     * @return
     */
    public String getWxUserInfo(String access_token) {
        LOGGER.debug("getWxUserInfo code={}", access_token);
        String url = new StringBuilder(USER_INFO_URL).append("?").append("access_token=").append(access_token)
                .append("&openid=").append(APP_ID).append("&lang=zh_CN")
                .toString();
        String result = HttpRequestUtil.get(url);
        LOGGER.debug("getWxUserInfo result={}", result);
        return result;
    }

    /**
     * 检验授权凭证（网页授权access_token）是否有效
     *
     * @param access_token
     * @return
     */
    public String checkAccessToken(String access_token) {
        LOGGER.debug("checkAccessToken code={}", access_token);
        String url = new StringBuilder(CHECK_TOKEN_URL).append("?").append("access_token=").append(access_token)
                .append("&openid=").append(APP_ID)
                .toString();
        String result = HttpRequestUtil.get(url);
        LOGGER.debug("checkAccessToken result={}", result);
        return result;
    }

    /**
     * 获取微信公众号普通access_token
     * <p>
     * {"access_token":"ACCESS_TOKEN","expires_in":7200}
     * {"errcode":40013,"errmsg":"invalid appid"}
     * 返回码	说明
     * -1	系统繁忙，此时请开发者稍候再试
     * 0	请求成功
     * 40001	AppSecret错误或者AppSecret不属于这个公众号，请开发者确认AppSecret的正确性
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

    /**
     * 检查网页授权token是否超时
     *
     * @param wxAccessTokenUser
     * @return
     */
    public Boolean checkExpireAccessToken(WxAccessTokenUser wxAccessTokenUser) {
        //失效时间=modified_date+expire_in;微信平台 失效时间会延迟5分钟，新老token都可用
        Date modifiedDate = wxAccessTokenUser.getModifiedDate();
        Integer expiresIn = wxAccessTokenUser.getExpiresIn();
        if (null == modifiedDate) {
            modifiedDate = wxAccessTokenUser.getCreatedDate();
        }
        Date date = DateUtil.addSecond(wxAccessTokenUser.getExpiresIn(), modifiedDate);
        if (new Date().compareTo(date) == 1) {
//            超时
            return true;
        }
        return false;
    }

    /**
     * 获取公众号jsapi_ticket
     * {
     * "errcode":0,
     * "errmsg":"ok",
     * "ticket":"bxLdikRXVbTPdHSM05e5u5sUoXNKd8-41ZO3MhKoyN5OfkWITDGgnr2fwJ0m9E8NYzWKVZvdVtaUgWvsdshFKA",
     * "expires_in":7200
     * }
     *
     * @param accessToken
     * @return
     */
    public String getJsapiTicket(String accessToken) {
        String url = new StringBuilder(JSAPI_TICKET_URL).append("?access_token=")
                .append(accessToken).append("&type=jsapi").toString();
        String result = HttpRequestUtil.get(url);
        LOGGER.debug("getJsapiTicket result={}", result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!Integer.valueOf(0).equals(jsonObject.get("errcode"))) {
            throw new CommonException(new StringBuffer(CommonErrorEnum.WX_JSAPI_TICKET_ERROR.getCode())
                    .append("-").append(jsonObject.get("errcode")).toString(), new StringBuffer(CommonErrorEnum.WX_JSAPI_TICKET_ERROR.getDescription())
                    .append("-").append(jsonObject.get("errmsg")).toString());
        }
        return result;
    }

    /**
     * 发送微信模板消息
     *
     * @param wxTemplateMsgReqDto
     * @return
     */
    public String sendTemplateMessage(WxTemplateMsgReqDto wxTemplateMsgReqDto, String access_token) {
        LOGGER.info("sendTemplateMessage wxTemplateMsgReqDto={}", JSONObject.toJSONString(wxTemplateMsgReqDto));
        String result = HttpRequestUtil.post(WX_MESSAGE_SEND_URL.concat(access_token), JSONObject.toJSONString(wxTemplateMsgReqDto));
        LOGGER.info("sendTemplateMessage result={}", result);
        return result;
    }

    public static String sendTemplateMessageTest() {
        WxTemplateMsgReqDto wxTemplateMsgReqDto = new WxTemplateMsgReqDto();
        String openId = "oztjd0g6e_Pcf9zDKijzhqaOVJTU";
        String accessToken = "12_JnINMapx7YR7wW6Zu9EZTjdmWivSisL-5fn3GzmibtsUOas9BkCEbwRJ7JzFr3Vk1U5Tfn99ZxKUgkrwpd-KsIQT_8Q8B9dW1-KEZqSdu4JTb_Mmr80M-1gboZcmnbi3R96pNeftM8_DUpOeFVTiAEAIYW";
        String msgId = "-ppyREifhswcB1jFIe1336Er0zWfxe_-TwoMIZHUX5Y";
        String url = "https://www.baidu.com";
        wxTemplateMsgReqDto.setTouser(openId);
        wxTemplateMsgReqDto.setTemplate_id(msgId);
        wxTemplateMsgReqDto.setUrl(url);
        WxTemplateMsgData wxTemplateMsgData = new WxTemplateMsgData();
        WxTemplateMsgDataItem wxTemplateMsgDataItem = new WxTemplateMsgDataItem("昵称test");
        WxTemplateMsgDataItem wxTemplateMsgDataItem2 = new WxTemplateMsgDataItem("orderTest");
        WxTemplateMsgDataItem wxTemplateMsgDataItem3 = new WxTemplateMsgDataItem("已发布成功");
        WxTemplateMsgDataItem wxTemplateMsgDataItem4 = new WxTemplateMsgDataItem("2018-7-27 17:05:31");
        WxTemplateMsgDataItem wxTemplateMsgDataItem5 = new WxTemplateMsgDataItem("订单已发布，请支付");
        wxTemplateMsgData.setFirst(wxTemplateMsgDataItem);
        wxTemplateMsgData.setKeyword1(wxTemplateMsgDataItem2);
        wxTemplateMsgData.setKeyword2(wxTemplateMsgDataItem3);
        wxTemplateMsgData.setKeyword3(wxTemplateMsgDataItem4);
        wxTemplateMsgData.setRemark(wxTemplateMsgDataItem5);
        wxTemplateMsgReqDto.setData(wxTemplateMsgData);

        LOGGER.info("sendTemplateMessageTest wxTemplateMsgReqDto={}", JSONObject.toJSONString(wxTemplateMsgReqDto));
        String result = HttpRequestUtil.post("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken, JSONObject.toJSONString(wxTemplateMsgReqDto));
        LOGGER.info("sendTemplateMessageTest result={}", result);
        return result;
    }

    /**
     * 创建个性化菜单
     * https://developers.weixin.qq.com/doc/offiaccount/Custom_Menus/Personalized_menu_interface.html
     */
    public void createdMenu(String access_token) {
        String menu = "{\"button\":[{\"type\":\"miniprogram\",\"name\":\"立即下单\",\"url\":\"https://www.jingdakongjian.com/\",\"appid\":\"wx7d53782fc19bb0f4\",\"pagepath\":\"pages/index/index\"},{\"name\":\"跑手接单\",\"sub_button\":[{\"type\":\"miniprogram\",\"name\":\"跑手接单\",\"url\":\"https://m.jingdakongjian.com/\",\"appid\":\"wx7d53782fc19bb0f4\",\"pagepath\":\"pages/index/index\"},{\"type\":\"miniprogram\",\"name\":\"申请跑手\",\"url\":\"https://m.jingdakongjian.com/\",\"appid\":\"wx7d53782fc19bb0f4\",\"pagepath\":\"pages/approve/approve\"}]},{\"name\":\"活动中心\",\"sub_button\":[{\"type\":\"miniprogram\",\"name\":\"寝室组队\",\"url\":\"https://m.jingdakongjian.com/activeIndex\",\"appid\":\"wx7d53782fc19bb0f4\",\"pagepath\":\"/pages/index/index?toUrl=/pages/web/web\"},{\"type\":\"view\",\"name\":\"支付有礼\",\"url\":\"https://m.jingdakongjian.com/activeMore/003\"},{\"type\":\"view\",\"name\":\"关注有奖\",\"url\":\"https://m.jingdakongjian.com/activeMore/002\"}]}],\"matchrule\":{\"country\":\"中国\"}}";
        String post = HttpRequestUtil.post(WX_MENU_URL+access_token, menu);
    }
}
