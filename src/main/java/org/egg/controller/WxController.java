package org.egg.controller;

import com.alibaba.fastjson.JSONObject;
import org.egg.biz.CustomerBiz;
import org.egg.biz.PayBiz;
import org.egg.enums.CommonErrorEnum;
import org.egg.exception.CommonException;
import org.egg.integration.wx.WxCommonApi;
import org.egg.model.DO.Customer;
import org.egg.model.DTO.WxNotifyDto;
import org.egg.model.wechat.AccessTokenMiniResult;
import org.egg.response.CommonSingleResult;
import org.egg.service.impl.WechatServiceImpl;
import org.egg.template.BizTemplate;
import org.egg.utils.ClientUtils;
import org.egg.utils.ConstantsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author cdt
 * @Description 微信回执接口
 * @date: 2018/1/19 14:50
 * //
 */
@Controller
@RequestMapping("/wx")
public class WxController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxController.class);

    @Autowired
    private WechatServiceImpl wechatService;
    @Autowired
    private WxCommonApi wxCommonApi;
    @Autowired
    private BizTemplate bizTemplate;
    @Value("${token}")
    private String token;
    @Autowired
    private CustomerBiz customerBiz;
    @Autowired
    private PayBiz payBiz;

//    /**
//     * 回执获取网页授权
//     * https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code
//     * &scope=snsapi_userinfo&state=STATE#wechat_redirect
//     * 用于自定义菜单 快速登录
//     *
//     * @param code
//     * @param returnUrl 跳转页
//     * @param response
//     */
//    @RequestMapping("/show")
//    public void accessAuthWxCode(String code, String returnUrl, HttpServletRequest request, HttpServletResponse response) {
//        LOGGER.info("accesAuthWxCode code={}", code);
//        //1.请求access_token
//        AccessTokenResult accessToken = new AccessTokenResult();
//        try {
//            accessToken = wechatService.getAccessToken(code);
//        } catch (Exception e) {
//            LOGGER.error("获取openId异常 e={}", e);
//        }
//        if (!StringUtils.isEmpty(accessToken.getOpenid())) {
////            校验是否已经绑定
//            UserQueryReq userStuQueryReq = new UserQueryReq();
//            userStuQueryReq.setWxOpenId(accessToken.getOpenid());
//            List<User> users = userService.queryList(userStuQueryReq);
////            自动登录
//            if (!CollectionUtils.isEmpty(users)) {
//                HttpSession session = request.getSession();
//                session.setAttribute(ConstantsUtil.USER_KEY, users.get(0));
//                session.setAttribute(ConstantsUtil.OPEN_ID_KEY, accessToken.getOpenid());
//                setCookie(response, ConstantsUtil.UMU, users.get(0).getUserNo());
//                HashMap<String, String> stringStringHashMap = new HashMap<>();
//                stringStringHashMap.put("un", users.get(0).getUserNo());
//                setCookie(response, ConstantsUtil.SNO, EncryptionUtil.generateRequestSign(stringStringHashMap));
//
////             user token 入库
//                wxTokenUserBiz.updateToken(accessToken, users.get(0).getUserNo());
//            }
//        }
//        //            跳转到首页，无登录状态
//        try {
//            CommonUtil.sendRedirectForHttps(request, response, returnUrl);
//            return;
////            response.sendRedirect(returnUrl);
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//
//    }


    @RequestMapping("/wxNotify")
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) {
        String result = "init";
        try {
            String resultXml = convertString4Request(request);
            LOGGER.info("微信支付通知，resultXml={}", resultXml);
            WxNotifyDto wxNotifyDto = new WxNotifyDto();
            if (!wxCommonApi.checkNotifySign(resultXml, wxNotifyDto)) {
                LOGGER.error("wxNotify,sign fail");
                result = "sign fail";
                return;
            }
            LOGGER.info("wxNotifyDto={}", wxNotifyDto);
            try {
//
                payBiz.syncStatus4WxPay(wxNotifyDto.getOut_trade_no(),null);

                result = setXml("SUCCESS", "OK");
            } catch (CommonException e) {
                switch (e.getErrorCode()) {
                    case "PRO2200004":
                        result = "{\"return_code\":\"FAIL\",\"return_msg\":\"wx flow detail not find\"}";
                        break;
                    case "PRO2200005":
                        result = setXml("SUCCESS", "OK");
                        //result = "<WxNotifyResultDto><return_code><![CDATA[SUCCESS]]></return_code>" +
                        //        "<return_msg><![CDATA[OK]]></return_msg>" +
                        //        "</WxNotifyResultDto>";
                        break;
                    case "PRO2200006":
                        result = "{\"return_code\":\"FAIL\",\"return_msg\":\"amount inconformity\"}";
                        break;
                    case "PRO2200007":
                        result = "{\"return_code\":\"FAIL\",\"return_msg\":\"param is error\"}";
                        break;
                    case "PRO9900001":
                        result = "{\"return_code\":\"FAIL\",\"return_msg\":\"update db fail\"}";
                        break;
                    default:
                        result = "{\"return_code\":\"FAIL\",\"return_msg\":\"other exception\"}";
                        break;
                }
            }
            LOGGER.info("微信支付通知，result={}", result);

        } catch (Exception e) {
            LOGGER.error("wxNotify e={}", e);
            result = "wxNotify have benn exception";
        } finally {
            // 支付成功，商户处理后同步返回给微信参数
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
                writer.write(result);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                writer.flush();
                writer.close();
            }
        }
    }

    //通过xml 发给微信消息
    private static String setXml(String return_code, String return_msg) {
        SortedMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("return_code", return_code);
        parameters.put("return_msg", return_msg);
        return "<xml><return_code><![CDATA[" + return_code + "]]>" +
                "</return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }


    /**
     * 初始化小程序
     * <p>
     * 1.初始化小程序来源标示
     * 2.获取openId,返回
     * 3.登录或注册
     * 4.更新sessionKey
     */
    @RequestMapping("/mini")
    @ResponseBody
    public CommonSingleResult<AccessTokenMiniResult> init(String code, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("init code={}", code);
        CommonSingleResult<AccessTokenMiniResult> accessTokenMiniResultCommonSingleResult = new CommonSingleResult<>();
        AccessTokenMiniResult accessToken = new AccessTokenMiniResult();
//        初始化小程序来源标示
        ClientUtils.setCookie(ConstantsUtil.MINI_FLAG_COOKIE_NAME, "true", response);
        //1.请求access_token
        if (StringUtils.isEmpty(code)) {
            accessTokenMiniResultCommonSingleResult.setError(CommonErrorEnum.PARAM_ERROR);
            return accessTokenMiniResultCommonSingleResult;
        }
        accessToken = wechatService.getAccessTokenForMini(code);
        String openId = accessToken.getOpenid();
//登录
        CommonSingleResult<Customer> customerCommonSingleResult = customerBiz.miniLoginFast(accessToken);
        accessTokenMiniResultCommonSingleResult.setSuccess(true);
        accessTokenMiniResultCommonSingleResult.setError(CommonErrorEnum.SUCCESS);
        accessTokenMiniResultCommonSingleResult.setData(accessToken);
        LOGGER.info("init openId={}", JSONObject.toJSONString(openId));
        LOGGER.info("accessTokenMiniResultCommonSingleResult={}", JSONObject.toJSONString(accessTokenMiniResultCommonSingleResult));
        return accessTokenMiniResultCommonSingleResult;
    }


    private String convertString4Request(HttpServletRequest request) throws IOException {

        InputStream inputStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, length);
        }
        outSteam.close();
        inputStream.close();

        // 获取微信调用我们notify_url的返回信息
        String resultXml = new String(outSteam.toByteArray(), "utf-8");
        return resultXml;
    }
}
