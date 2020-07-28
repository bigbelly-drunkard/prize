//package org.egg.service.impl;
//
//import com.alibaba.fastjson.JSONObject;
//import org.apache.commons.lang3.StringUtils;
//import org.egg.enums.ChannelEnum;
//import org.egg.enums.CommonErrorEnum;
//import org.egg.enums.WxTemplateMsgTypeEnum;
//import org.egg.exception.CommonException;
//import org.egg.model.DO.User;
//import org.egg.model.DO.WxAccessToken;
//import org.egg.model.DO.WxAccessTokenUser;
//import org.egg.model.DTO.WxTemplateMsgData;
//import org.egg.model.DTO.WxTemplateMsgDataItem;
//import org.egg.model.DTO.WxTemplateMsgReqDto;
//import org.egg.model.DTO.WxTemplateMsgResDto;
//import org.egg.model.VO.UserQueryReq;
//import org.egg.model.VO.WxAccessTokenQueryReq;
//import org.egg.model.VO.WxAccessTokenUserQueryReq;
//import org.egg.model.VO.WxSignVo;
//import org.egg.model.wechat.AccessTokenMiniResult;
//import org.egg.model.wechat.AccessTokenResult;
//import org.egg.model.wechat.WxBaseResult;
//import org.egg.model.wechat.WxUserInfo;
//import org.egg.utils.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author cdt
// * @Description 微信处理类
// * @date: 2018/1/19 15:38
// */
//@Service
//public class WechatServiceImpl {
//    private static final Logger LOGGER = LoggerFactory.getLogger(WechatServiceImpl.class);
//
//    @Autowired
//    private WxAccessTokenServiceImpl wxAccessTokenService;
//    @Autowired
//    private WxAccessTokenUserServiceImpl wxAccessTokenUserService;
//
//    @Autowired
//    private WxUtil wxUtil;
//    @Autowired
//    private WxMiniUtil wxMiniUtil;
//    @Autowired
//    private UserServiceImpl userService;
//    @Value("${black.user.no}")
//    private String BLANK_USER_NO;
//    @Value("${environment}")
//    private String ENVIRONMENT;
//
//    @Value("${msg.one}")
//    private String MSG_ONE;
//    @Value("${msg.two}")
//    private String MSG_TWO;
//
//    @Value("${wx.msg.template.id.publish.succ}")
//    private String MSG_ID_ORDER_PUBLISH_SUCC;
//    @Value("${wx.msg.template.id.publish.pay.succ}")
//    private String MSG_ID_ORDER_PUBLISH_PAY_SUCC;
//    @Value("${wx.msg.template.id.accept.game.succ}")
//    private String MSG_ID_ORDER_ACCEPT_GAME_SUCC;
//    @Value("${wx.msg.template.id.accept.play.succ}")
//    private String MSG_ID_ORDER_ACCEPT_PLAY_SUCC;
//    @Value("${wx.msg.template.id.recharge}")
//    private String MSG_ID_RECHARGE;
//    @Value("${wx.msg.template.id.cash.pending}")
//    private String MSG_ID_CASH_PENDING;
//    @Value("${wx.msg.template.id.cash.succ}")
//    private String MSG_ID_CASH_SUCC;
//    @Value("${wx.msg.template.id.check.fail}")
//    private String MSG_ID_CHECK_FAIL;
//    @Value("${wx.msg.template.id.delay}")
//    private String MSG_ID_DELAY;
//    @Value("${wx.msg.template.id.finish}")
//    private String MSG_ID_FINISH;
//    @Value("${wx.msg.template.id.recommend.register}")
//    private String MSG_ID_RECOMMEND_REGISTER;
//    @Value("${wx.msg.template.id.recharge.test}")
//    private String TEST_RER;
//    @Value("${wx.msg.subscription}")
//    private String MSG_SUBSCRIPTION;
//
//
//    @Value("${wx.msg.template.url.publish.succ}")
//    private String MSG_URL_PUB_SUCC;
//    @Value("${wx.msg.template.url.publish.pay.succ}")
//    private String MSG_URL_PUB_PAY_SUCC;
//    @Value("${wx.msg.template.url.accept.game.succ}")
//    private String MSG_URL_ACCEPT_GAME_SUCC;
//    @Value("${wx.msg.template.url.accept.play.succ}")
//    private String MSG_URL_ACCEPT_PLAY_SUCC;
//    @Value("${wx.msg.template.url.recharge}")
//    private String MSG_URL_RECHARGE_SUCC;
//    @Value("${wx.msg.template.url.cash.pending}")
//    private String MSG_URL_CASH_PENDING;
//    @Value("${wx.msg.template.url.cash.succ}")
//    private String MSG_URL_CASH_SUCC;
//    @Value("${wx.msg.template.url.check.fail}")
//    private String MSG_URL_CHECK_FAIL;
//    @Value("${wx.msg.template.url.delay.game}")
//    private String MSG_URL_DELAY_GAME;
//    @Value("${wx.msg.template.url.delay.play}")
//    private String MSG_URL_DELAY_PLAY;
//    @Value("${wx.msg.template.url.finish.game.detail}")
//    private String MSG_URL_FINISH_GAME_DETAIL;
//    @Value("${wx.msg.template.url.finish.play.detail}")
//    private String MSG_URL_FINISH_PLAY_DETAIL;
//    @Value("${wx.msg.template.url.finish.balance}")
//    private String MSG_URL_FINISH_BALANCE;
//    @Value("${wx.msg.template.url.recommend.register}")
//    private String MSG_URL_RECOMMEND_REGISTER;
//    @Value("${wx.msg.template.url.test.recharge}")
//    private String MSG_URL_TEST_RECHARGE;
//    @Value("${wx.msg.subscription.url}")
//    private String MSG_SUBSCRIPTION_URL;
//
//
//    /**
//     * 获取网页授权accessToken
//     *
//     * @param code
//     * @return
//     */
//    public AccessTokenResult getAccessToken(String code) throws CommonException {
//        String accessToken = wxUtil.getAccessToken(code);
//        AccessTokenResult accessTokenResult = JSONObject.parseObject(accessToken, AccessTokenResult.class);
//        if (StringUtils.isEmpty(accessTokenResult.getOpenid())) {
//            LOGGER.error("获取openId异常，accessToken={}", accessToken);
//            throw new CommonException(CommonErrorEnum.WX_ACCESS_TOKEN_ERROR);
//        }
//        return accessTokenResult;
//    }
//
//    /**
//     * 获取网页授权accessToken
//     *
//     * @param code
//     * @return
//     */
//    public AccessTokenMiniResult getAccessTokenForMini(String code) throws CommonException {
//        String accessToken = wxMiniUtil.getAccessToken(code);
//        AccessTokenMiniResult accessTokenResult = JSONObject.parseObject(accessToken, AccessTokenMiniResult.class);
//        if (StringUtils.isEmpty(accessTokenResult.getOpenid())) {
//            LOGGER.error("获取openId异常，accessToken={}", accessToken);
//            throw new CommonException(CommonErrorEnum.WX_ACCESS_TOKEN_ERROR);
//        }
//        return accessTokenResult;
//    }
//
//    /**
//     * 刷新网页授权accessToken
//     *
//     * @param refreshToken
//     * @return
//     */
//    private AccessTokenResult refreshToken(String refreshToken) {
//
//        String accessToken = wxUtil.refreshToken(refreshToken);
//        return JSONObject.parseObject(accessToken, AccessTokenResult.class);
//    }
//
//    /**
//     * 刷新网页授权accessToken
//     *
//     * @param wxAccessTokenUser
//     * @return
//     */
//    private AccessTokenResult refreshToken(WxAccessTokenUser wxAccessTokenUser) {
//
//        String accessToken = wxUtil.refreshToken(wxAccessTokenUser.getRefreshToken());
//        AccessTokenResult accessTokenResult = null;
//        try {
//            accessTokenResult = JSONObject.parseObject(accessToken, AccessTokenResult.class);
//        } catch (Exception e) {
//            LOGGER.warn("refreshToken异常 有可能是refreshToken 30天有效期过期 需要用户重新授权");
//            return accessTokenResult;
//        }
//        wxAccessTokenUser.setRefreshToken(accessTokenResult.getRefresh_token());
//        wxAccessTokenUser.setExpiresIn(accessTokenResult.getExpires_in());
//        wxAccessTokenUser.setAccessToken(accessTokenResult.getAccess_token());
//        wxAccessTokenUserService.updateByPrimaryKeySelective(wxAccessTokenUser);
//        return accessTokenResult;
//    }
//
//    /**
//     * 拉取用户信息
//     *
//     * @param accessToken
//     * @return
//     */
//    private WxUserInfo getWxUserInfo(String accessToken) {
//        String wxUserInfo = wxUtil.getWxUserInfo(accessToken);
//        return JSONObject.parseObject(wxUserInfo, WxUserInfo.class);
//    }
//
//    /**
//     * 拉取用户信息
//     *
//     * @param accessToken
//     * @param userNo
//     * @return
//     */
//    public WxUserInfo getWxUserInfo(String accessToken, String userNo) {
//        String wxUserInfo = wxUtil.getWxUserInfo(accessToken);
//        WxUserInfo wxUserInfo1 = JSONObject.parseObject(wxUserInfo, WxUserInfo.class);
//        UserQueryReq userStuQueryReq = new UserQueryReq();
//        userStuQueryReq.setUserNo(userNo);
//        List<User> users = userService.queryList(userStuQueryReq);
//        if (CollectionUtils.isEmpty(users)) {
//            throw new CommonException(CommonErrorEnum.PARAM_ERROR);
//        }
////        判断wxUserInfo如果返回错误，则直接把access_token字段置为空，然后wx用户信息同步job不拉取这种数据，等待用户再次从wx/show入口进入，
//// 重新覆盖新access_token字段  全局错误码https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433747234
//        if (wxUserInfo1.getErrcode() != null && !"0".equals(wxUserInfo1.getErrcode())) {
//            WxAccessTokenUserQueryReq wxAccessTokenUserQueryReq = new WxAccessTokenUserQueryReq();
//            wxAccessTokenUserQueryReq.setUserNo(userNo);
//            List<WxAccessTokenUser> wxAccessTokenUsers = wxAccessTokenUserService.queryList(wxAccessTokenUserQueryReq);
//            if (!CollectionUtils.isEmpty(wxAccessTokenUsers)) {
//                WxAccessTokenUser wxAccessTokenUser = wxAccessTokenUsers.get(0);
//                wxAccessTokenUser.setAccessToken("");
//                wxAccessTokenUserService.updateByPrimaryKeySelective(wxAccessTokenUser);
//                return wxUserInfo1;
//            }
//        }
//        User user = users.get(0);
//        user.setUserNo(userNo);
//        user.setWxOpenId(wxUserInfo1.getOpenId());
//        user.setUserHeadPic(wxUserInfo1.getHeadimgurl());
//        user.setNickName(wxUserInfo1.getNickname());
//        userService.update(user);
//        return wxUserInfo1;
//    }
//
//
//    /**
//     * 获取普通access_token
//     * ticket
//     *
//     * @return
//     */
//    public WxAccessToken getAccessTokenForMerchant(ChannelEnum channelCode) {
//        WxAccessToken wxAccessToken = new WxAccessToken();
//        String access_token = "";
//        Integer expires_in = -1;
//        String ticket = "";
//        Integer ticket_expires_in = -1;
//        //1.获取库中accessToken
//        WxAccessTokenQueryReq wxAccessTokenQueryReq = new WxAccessTokenQueryReq();
//        wxAccessTokenQueryReq.setChannelCode(channelCode.getCode());
//        List<WxAccessToken> wxAccessTokens = wxAccessTokenService.queryList(wxAccessTokenQueryReq);
//        //2.判断是否超时或无数据，如果超时或无数据，重新获取并更新入库
//        if (CollectionUtils.isEmpty(wxAccessTokens) || wxUtil.checkExpireAccessTokenForMerchant(wxAccessTokens.get(0))) {
//            try {
//                String wxMerchantAccessToken = "";
//                switch (channelCode) {
//                    case WX:
//                        wxMerchantAccessToken = wxUtil.getWxMerchantAccessToken();
//                        JSONObject jsonObject = JSONObject.parseObject(wxMerchantAccessToken);
//                        access_token = jsonObject.get("access_token").toString();
//                        String jsapiTicket = wxUtil.getJsapiTicket(access_token);
//                        JSONObject jsonObject1 = JSONObject.parseObject(jsapiTicket);
//                        ticket = jsonObject1.get("ticket").toString();
//                        ticket_expires_in = (Integer) jsonObject1.get("expires_in");
//                        break;
//                    case WX_MINI:
//                        wxMerchantAccessToken = wxMiniUtil.getWxMerchantAccessToken();
//                        break;
//                    default:
//                        break;
//                }
//                JSONObject jsonObject = JSONObject.parseObject(wxMerchantAccessToken);
//                access_token = jsonObject.get("access_token").toString();
//                expires_in = (Integer) jsonObject.get("expires_in");
//
//                if (CollectionUtils.isEmpty(wxAccessTokens)) {
//                    wxAccessToken.setAccessToken(access_token);
//                    wxAccessToken.setExpiresIn(expires_in);
//                    Date date = new Date();
//                    wxAccessToken.setCreatedDate(date);
//                    wxAccessToken.setModifiedDate(date);
//                    wxAccessToken.setJsapiTicket(ticket);
//                    wxAccessToken.setTicketExpiresIn(ticket_expires_in);
//                    wxAccessToken.setChannelCode(channelCode.getCode());
//                    wxAccessTokenService.insertSelective(wxAccessToken);
//                } else {
//                    wxAccessToken = wxAccessTokens.get(0);
//                    wxAccessToken.setExpiresIn(expires_in);
//                    wxAccessToken.setAccessToken(access_token);
//                    wxAccessToken.setJsapiTicket(ticket);
//                    wxAccessToken.setTicketExpiresIn(ticket_expires_in);
//                    wxAccessToken.setChannelCode(channelCode.getCode());
//                    wxAccessTokenService.updateByPrimaryKeySelective(wxAccessToken);
//                }
//            } catch (Exception e) {
//                LOGGER.error("获取普通access_token e={}", e);
//            }
//        } else {
//            wxAccessToken = wxAccessTokens.get(0);
//        }
//        return wxAccessToken;
//    }
//
//    /**
//     * 获取用户access_token
//     *
//     * @param wxAccessTokenUser
//     */
//    public void getUserToken(WxAccessTokenUser wxAccessTokenUser) {
//        if (wxUtil.checkExpireAccessToken(wxAccessTokenUser)) {
////                    超时 更新并入库
//            AccessTokenResult accessTokenResult = refreshToken(wxAccessTokenUser);
//            if (accessTokenResult != null) {
//                wxAccessTokenUser.setAccessToken(accessTokenResult.getAccess_token());
//                wxAccessTokenUser.setExpiresIn(accessTokenResult.getExpires_in());
//                wxAccessTokenUser.setRefreshToken(accessTokenResult.getRefresh_token());
//            }
//        }
//    }
//
//    /**
//     * 微信微信js-sdk签名
//     * url:当前网页
//     *
//     * @return
//     */
//    public WxSignVo jsSDKSign(HttpServletRequest request) {
//        WxSignVo wxSignVo = new WxSignVo();
//        if (!ConstantsUtil.WX_JOB_SWITH) {
//            return wxSignVo;
//        }
//        String s = request.getRequestURL().toString();
////        此处获取的是http 实际上是https访问到NGINX，NGINX给转向内部http，但是为了保证签名一致，签名用https
//        String replace = s.replace("http", "https");
//        StringBuilder stringBuilder = new StringBuilder(replace);
//        if (!StringUtils.isEmpty(request.getQueryString())) {
//            stringBuilder.append("?").append(request.getQueryString());
//        }
//
//        Date now = new Date();
//        String nonceStr = String.valueOf(Math.random() * now.getTime() + now.getTime());
//        wxSignVo.setAppId(WxUtil.APP_ID);
//        wxSignVo.setNonceStr(nonceStr);
//        wxSignVo.setTimestamp(now.getTime());
//        WxAccessToken accessTokenForMerchant = getAccessTokenForMerchant(ChannelEnum.WX);
//        Map<String, String> paraMap = new HashMap<String, String>();
//        paraMap.put("noncestr", nonceStr);
//        paraMap.put("jsapi_ticket", accessTokenForMerchant.getJsapiTicket());
//        paraMap.put("timestamp", String.valueOf(now.getTime()));
////        当前网页的URL，不包含#及其后面部分
//        paraMap.put("url", stringBuilder.toString().split("#")[0]);
//        wxSignVo.setSignature(WxPayUtil.jsSdkSign(paraMap));
//        return wxSignVo;
//    }
//
//    /**
//     * 发送微信模板消息
//     *
//     * @param userNo
//     * @param wxTemplateMsgData
//     * @param wxTemplateMsgTypeEnum
//     * @return
//     */
//    public WxTemplateMsgResDto sendWxTemplateMsg(String userNo, WxTemplateMsgData wxTemplateMsgData, WxTemplateMsgTypeEnum wxTemplateMsgTypeEnum) {
////        黑名单用户不发送
//        if (BLANK_USER_NO.equals(userNo) || ENVIRONMENT.equals("test")) {
//            LOGGER.info("黑名单用户或测试环境不发送，userNo={}，ENVIRONMENT={}", userNo, ENVIRONMENT);
//            return null;
//        }
////        1.查询用户是否有openId 如果没有 打印日志跳过
//        UserQueryReq userStuQueryReq = new UserQueryReq();
//        userStuQueryReq.setUserNo(userNo);
//        List<User> users = userService.queryList(userStuQueryReq);
//        if (CollectionUtils.isEmpty(users)) {
//            LOGGER.error("sendWxTemplateMsg WARN_ERROR_TIP");
//            throw new CommonException(CommonErrorEnum.WARN_ERROR_TIP);
//        }
//        if (StringUtils.isBlank(users.get(0).getWxOpenId())) {
//            LOGGER.warn("此用户发布单时没有openId");
//            return null;
//        }
//        return sendWxTemplateMsg4Common(users.get(0).getWxOpenId(), wxTemplateMsgData, wxTemplateMsgTypeEnum);
//    }
//
//    /**
//     * {{first.DATA}}
//     * 会员昵称：{{keyword1.DATA}}
//     * 关注时间：{{keyword2.DATA}}
//     * {{remark.DATA}}
//     *
//     * @param openId
//     * @return
//     */
//    public WxTemplateMsgResDto sendWxTemplateMsg4Subscription(String openId) {
//        WxTemplateMsgData wxTemplateMsgData = new WxTemplateMsgData();
//        wxTemplateMsgData.setFirst(new WxTemplateMsgDataItem("感谢您关注公众号"));
//        wxTemplateMsgData.setRemark(new WxTemplateMsgDataItem("关注即送代金券，点击查看详情领取代金券；【首发快递代拿订单只需0.01元，立即下单使用代金券吧】"));
//        wxTemplateMsgData.setKeyword1(new WxTemplateMsgDataItem("快递代拿尊贵会员"));
//        wxTemplateMsgData.setKeyword2(new WxTemplateMsgDataItem(DateUtil.format(new Date(),DateUtil.YMDHMS)));
//        return sendWxTemplateMsg4Common(openId, wxTemplateMsgData, WxTemplateMsgTypeEnum.SUBSCRIPTION);
//    }
//
//    public WxTemplateMsgResDto sendWxTemplateMsg4Common(String openId, WxTemplateMsgData wxTemplateMsgData, WxTemplateMsgTypeEnum wxTemplateMsgTypeEnum) {
//        if (!ConstantsUtil.WX_JOB_SWITH) {
//            return null;
//        }
//
//        WxTemplateMsgReqDto wxTemplateMsgReqDto = new WxTemplateMsgReqDto();
//        wxTemplateMsgReqDto.setTouser(openId);
//        wxTemplateMsgReqDto.setData(wxTemplateMsgData);
//        // TODO: 2018/7/27 二期
//        switch (wxTemplateMsgTypeEnum) {
//            case PUB_SUCC:
//                wxTemplateMsgReqDto.setTemplate_id(MSG_ID_ORDER_PUBLISH_SUCC);
//                wxTemplateMsgReqDto.setUrl(MSG_URL_PUB_SUCC.replaceAll("\\{cdt\\}", wxTemplateMsgData.getKeyword1().getValue()));
//                break;
//            case PUB_PAY_SUCC:
//                wxTemplateMsgReqDto.setTemplate_id(MSG_ID_ORDER_PUBLISH_PAY_SUCC);
//                wxTemplateMsgReqDto.setUrl(MSG_URL_PUB_PAY_SUCC.replaceAll("\\{cdt\\}", wxTemplateMsgData.getKeyword2().getValue()));
//                break;
//            case ACCEPT_GAME_SUCC:
//                wxTemplateMsgReqDto.setTemplate_id(MSG_ID_ORDER_ACCEPT_GAME_SUCC);
//                wxTemplateMsgReqDto.setUrl(MSG_URL_ACCEPT_GAME_SUCC.replaceAll("\\{cdt\\}", wxTemplateMsgData.getKeyword1().getValue()));
//                break;
//            case ACCEPT_PLAY_SUCC:
//                wxTemplateMsgReqDto.setTemplate_id(MSG_ID_ORDER_ACCEPT_PLAY_SUCC);
//                wxTemplateMsgReqDto.setUrl(MSG_URL_ACCEPT_PLAY_SUCC.replaceAll("\\{cdt\\}", wxTemplateMsgData.getKeyword1().getValue()));
//                break;
//            case RECHARGE_SUCC:
//                wxTemplateMsgReqDto.setTemplate_id(MSG_ID_RECHARGE);
//                wxTemplateMsgReqDto.setUrl(MSG_URL_RECHARGE_SUCC);
//                break;
//            case CASH_PENDING:
//                wxTemplateMsgReqDto.setTemplate_id(MSG_ID_CASH_PENDING);
//                wxTemplateMsgReqDto.setUrl(MSG_URL_CASH_PENDING);
//                break;
//            case CASH_SUCC:
//                wxTemplateMsgReqDto.setTemplate_id(MSG_ID_CASH_SUCC);
//                wxTemplateMsgReqDto.setUrl(MSG_URL_CASH_SUCC);
//                break;
//            case DELAY_GAME:
//                wxTemplateMsgReqDto.setTemplate_id(MSG_ID_DELAY);
//                wxTemplateMsgReqDto.setUrl(MSG_URL_DELAY_GAME.replaceAll("\\{cdt\\}", wxTemplateMsgData.getKeyword1().getValue()));
//                break;
//            case DELAY_PALY:
//                wxTemplateMsgReqDto.setTemplate_id(MSG_ID_DELAY);
//                wxTemplateMsgReqDto.setUrl(MSG_URL_DELAY_PLAY.replaceAll("\\{cdt\\}", wxTemplateMsgData.getKeyword1().getValue()));
//                break;
////            case FINISH_SUCC_GAME_DETAIL:
////                wxTemplateMsgReqDto.setTemplate_id(MSG_ID_FINISH);
////                wxTemplateMsgReqDto.setUrl(MSG_URL_FINISH_GAME_DETAIL);
////                break;
////            case FINISH_SUCC_PLAY_DETAIL:
////                wxTemplateMsgReqDto.setTemplate_id(MSG_ID_FINISH);
////                wxTemplateMsgReqDto.setUrl(MSG_URL_FINISH_PLAY_DETAIL);
////                break;
//            case FINISH_SUCC_BALANCE:
//                wxTemplateMsgReqDto.setTemplate_id(MSG_ID_FINISH);
//                wxTemplateMsgReqDto.setUrl(MSG_URL_FINISH_BALANCE);
//                break;
//            case CHECK_FAIL:
//                wxTemplateMsgReqDto.setTemplate_id(MSG_ID_CHECK_FAIL);
//                wxTemplateMsgReqDto.setUrl(MSG_URL_CHECK_FAIL.replaceAll("\\{cdt\\}", wxTemplateMsgData.getKeyword1().getValue()));
//                break;
//            case RECOMMEND_REGISTER:
//                wxTemplateMsgReqDto.setTemplate_id(MSG_ID_RECOMMEND_REGISTER);
//                wxTemplateMsgReqDto.setUrl(MSG_URL_RECOMMEND_REGISTER.replaceAll("\\{cdt\\}", wxTemplateMsgData.getKeyword1().getValue()));
//                break;
//            case RECOMMEND_AMOUNT:
//                wxTemplateMsgReqDto.setTemplate_id(MSG_ID_CHECK_FAIL);
//                wxTemplateMsgReqDto.setUrl(MSG_URL_CHECK_FAIL.replaceAll("\\{cdt\\}", wxTemplateMsgData.getKeyword1().getValue()));
//                break;
//            case TEST_RECHARGE:
//                wxTemplateMsgReqDto.setTemplate_id(TEST_RER);
//                wxTemplateMsgReqDto.setUrl(MSG_URL_TEST_RECHARGE.replaceAll("\\{cdt\\}", wxTemplateMsgData.getKeyword1().getValue()));
//                break;
//            case SUBSCRIPTION:
//                wxTemplateMsgReqDto.setTemplate_id(MSG_SUBSCRIPTION);
//                wxTemplateMsgReqDto.setUrl(MSG_SUBSCRIPTION_URL);
//                break;
//            default:
//                break;
//        }
////        获取平台access_token
//        WxAccessToken accessTokenForMerchant = getAccessTokenForMerchant(ChannelEnum.WX);
//        String s = wxUtil.sendTemplateMessage(wxTemplateMsgReqDto, accessTokenForMerchant.getAccessToken());
//        return JSONObject.parseObject(s, WxTemplateMsgResDto.class);
//    }
//
////    public WxTemplateMsgResDto sendWxTemplateMsg4Mini(String userNo, PubOrder pubOrder, AcceptOrder acceptOrder, WxTemplateMsgTypeEnum wxTemplateMsgTypeEnum) {
////        WxMiniTemplateMsgReqDto wxMiniTemplateMsgReqDto = new WxMiniTemplateMsgReqDto();
////        //        1.查询用户是否有openId 如果没有 打印日志跳过
////        UserQueryReq userStuQueryReq = new UserQueryReq();
////        userStuQueryReq.setUserNo(userNo);
////        List<User> users = userService.queryList(userStuQueryReq);
////        if (CollectionUtils.isEmpty(users)) {
////            LOGGER.error("sendWxTemplateMsg4Mini WARN_ERROR_TIP");
////            throw new CommonException(CommonErrorEnum.WARN_ERROR_TIP);
////        }
////        if (StringUtils.isBlank(users.get(0).getWxMiniOpenId())) {
////            LOGGER.warn("此用户发布单时没有openId");
////            return null;
////        }
////        wxMiniTemplateMsgReqDto.setTouser(users.get(0).getWxMiniOpenId());
////        HashMap<String, WxTemplateMsgDataItem> stringObjectHashMap = new HashMap<>();
////        wxMiniTemplateMsgReqDto.setData(stringObjectHashMap);
////        switch (wxTemplateMsgTypeEnum) {
////            case PUB_ACCEPT_PENDING:
////            case PUB_SUCC_MINI:
////            case ADMIN_CLOSE_MINI_PUB:
////                wxMiniTemplateMsgReqDto.setTemplate_id(MSG_ONE);
////                wxMiniTemplateMsgReqDto.setPage("/pages/orderDetail/orderDetail?pubOrderNo=" + pubOrder.getOrderNo());
////                WxTemplateMsgDataItem wxTemplateMsgDataItem = new WxTemplateMsgDataItem(pubOrder.getOrderNo());
////                stringObjectHashMap.put("character_string1", wxTemplateMsgDataItem);
////                WxTemplateMsgDataItem wxTemplateMsgDataItem2 = new WxTemplateMsgDataItem(DateUtil.format(new Date(), DateUtil.YMDHMS));
////                stringObjectHashMap.put("date3", wxTemplateMsgDataItem2);
////                if (wxTemplateMsgTypeEnum == WxTemplateMsgTypeEnum.PUB_ACCEPT_PENDING) {
////                    WxTemplateMsgDataItem wxTemplateMsgDataItem3 = new WxTemplateMsgDataItem("跑手已接单");
////                    stringObjectHashMap.put("phrase2", wxTemplateMsgDataItem3);
////                } else if (wxTemplateMsgTypeEnum == WxTemplateMsgTypeEnum.PUB_SUCC_MINI) {
////                    WxTemplateMsgDataItem wxTemplateMsgDataItem3 = new WxTemplateMsgDataItem("订单已完成");
////                    stringObjectHashMap.put("phrase2", wxTemplateMsgDataItem3);
////
////                } else {
////                    WxTemplateMsgDataItem wxTemplateMsgDataItem3 = new WxTemplateMsgDataItem("已关闭订单");
////                    stringObjectHashMap.put("phrase2", wxTemplateMsgDataItem3);
////                }
////                WxTemplateMsgDataItem wxTemplateMsgDataItem4 = new WxTemplateMsgDataItem("您可点击此消息进入订单详情页查看详情");
////                stringObjectHashMap.put("thing4", wxTemplateMsgDataItem4);
////                break;
////            case ADMIN_CLOSE_MINI_ACC:
////                wxMiniTemplateMsgReqDto.setTemplate_id(MSG_ONE);
////                wxMiniTemplateMsgReqDto.setPage("/pages/orderDetail/orderDetail?acceptOrderNo=" + acceptOrder.getOrderNo());
////                WxTemplateMsgDataItem wxTemplateMsgDataItem111 = new WxTemplateMsgDataItem(acceptOrder.getOrderNo());
////                stringObjectHashMap.put("character_string1", wxTemplateMsgDataItem111);
////                WxTemplateMsgDataItem wxTemplateMsgDataItem222 = new WxTemplateMsgDataItem(DateUtil.format(new Date(), DateUtil.YMDHMS));
////                stringObjectHashMap.put("date3", wxTemplateMsgDataItem222);
////                WxTemplateMsgDataItem wxTemplateMsgDataItem333 = new WxTemplateMsgDataItem("已关闭订单");
////                stringObjectHashMap.put("phrase2", wxTemplateMsgDataItem333);
////                WxTemplateMsgDataItem wxTemplateMsgDataItem444 = new WxTemplateMsgDataItem("您可点击此消息进入订单详情页查看详情");
////                stringObjectHashMap.put("thing4", wxTemplateMsgDataItem444);
////                break;
////            case ACCEPT_FLOW_SUCC:
////                wxMiniTemplateMsgReqDto.setTemplate_id(MSG_TWO);
////                wxMiniTemplateMsgReqDto.setPage("/pages/orderDetail/orderDetail?acceptOrderNo=" + acceptOrder.getOrderNo());
////                WxTemplateMsgDataItem wxTemplateMsgDataItem11 = new WxTemplateMsgDataItem(acceptOrder.getOrderNo());
////                BigDecimal amout = acceptOrder.getAmout();
////                StringBuilder stringBuilder = new StringBuilder("您的订单结算成功，已转入微信余额 " + amout.abs().toString() + " 元");
////                stringObjectHashMap.put("thing1", wxTemplateMsgDataItem11);
////                WxTemplateMsgDataItem wxTemplateMsgDataItem22 = new WxTemplateMsgDataItem(DateUtil.format(new Date(), DateUtil.YMDHMS));
////                stringObjectHashMap.put("date2", wxTemplateMsgDataItem22);
////                break;
////            default:
////                break;
////        }
////        //        获取平台access_token
////        WxAccessToken accessTokenForMerchant = getAccessTokenForMerchant(ChannelEnum.WX_MINI);
////        String s = wxMiniUtil.sendTemplateMessage(wxMiniTemplateMsgReqDto, accessTokenForMerchant.getAccessToken());
////        return JSONObject.parseObject(s, WxTemplateMsgResDto.class);
////    }
//
//    /**
//     * 检验授权凭证（网页授权access_token）是否有效
//     *
//     * @param accessToken
//     * @return
//     */
//    @Deprecated
//    private WxBaseResult checkAccessToken(String accessToken) {
//        String s = wxUtil.checkAccessToken(accessToken);
//        return JSONObject.parseObject(s, WxBaseResult.class);
//    }
//
//    public void sendRedPackage(String openId) {
//
//    }
//
//    public void createdMenu() {
//        WxAccessToken accessTokenForMerchant = getAccessTokenForMerchant(ChannelEnum.WX);
//        wxUtil.createdMenu(accessTokenForMerchant.getAccessToken());
//    }
//
//}
