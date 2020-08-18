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
import org.springframework.web.bind.annotation.PostMapping;
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

    /**
     * 微信支付结果通知
     *
     * @param request
     * @param response
     */

    @PostMapping("/wxNotify")
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
                payBiz.syncStatus4WxPay(wxNotifyDto.getOut_trade_no(), null);

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


    /**
     * 初始化小程序
     * <p>
     * 1.初始化小程序来源标示
     * 2.获取openId,返回
     * 3.登录或注册
     * 4.更新sessionKey
     */
    @PostMapping("/mini")
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
        CommonSingleResult<Customer> customerCommonSingleResult = customerBiz.miniLoginFast(accessToken.getOpenid());
        accessTokenMiniResultCommonSingleResult.setSuccess(true);
        accessTokenMiniResultCommonSingleResult.setError(CommonErrorEnum.SUCCESS);
        accessTokenMiniResultCommonSingleResult.setData(accessToken);
        LOGGER.info("init openId={}", JSONObject.toJSONString(openId));
        LOGGER.info("accessTokenMiniResultCommonSingleResult={}", JSONObject.toJSONString(accessTokenMiniResultCommonSingleResult));
        return accessTokenMiniResultCommonSingleResult;
    }
    @PostMapping("/miniMock")
    @ResponseBody
    public CommonSingleResult<Customer> mockFastMini() {
        String openId = "cdt";
        CommonSingleResult<Customer> customerCommonSingleResult = customerBiz.miniLoginFast(openId);
        return customerCommonSingleResult;
    }
    //================================================ private ==================================================
    //通过xml 发给微信消息
    private static String setXml(String return_code, String return_msg) {
        SortedMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("return_code", return_code);
        parameters.put("return_msg", return_msg);
        return "<xml><return_code><![CDATA[" + return_code + "]]>" +
                "</return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
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
