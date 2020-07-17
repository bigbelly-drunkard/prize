//package org.egg.Interceptor;
//
//import com.alibaba.fastjson.JSONObject;
//import org.apache.commons.lang3.StringUtils;
//import org.egg.enums.CommonErrorEnum;
//import org.egg.response.BaseResult;
//import org.egg.utils.ConstantsUtil;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.URLEncoder;
//
///**
// * @author cdt
// * @Description login interceptor
// * @date: 2017/11/7 16:36
// */
//public class BossLoginInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
////         登录拦截
//        HttpSession session = httpServletRequest.getSession();
//        if (session == null || session.getAttribute(ConstantsUtil.BOSS_USER_KEY) == null) {
//            if (!StringUtils.isBlank(httpServletRequest.getHeader("x-requested-with")) && httpServletRequest.getHeader("x-requested-with").equals("XMLHttpRequest")) {
//                print(httpServletResponse);
//            } else {
//                StringBuilder stringBuilder = new StringBuilder(httpServletRequest.getRequestURI());
//                if (null != httpServletRequest.getQueryString()) {
//                    stringBuilder.append("?").append(httpServletRequest.getQueryString());
//                }
//                httpServletResponse.sendRedirect("/boss/toBossLogin?returnUrl="+URLEncoder.encode(stringBuilder.toString(),"UTF-8"));
//            }
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//
//    }
//
//    private void print(HttpServletResponse httpServletResponse) {
//        httpServletResponse.setCharacterEncoding("UTF-8");
//        httpServletResponse.setContentType("application/json; charset=utf-8");
//        BaseResult baseResult = new BaseResult(CommonErrorEnum.ACCOUNT_LOGIN_NOT);
//        String jsonStr = JSONObject.toJSONString(baseResult);
//        PrintWriter out = null;
//        try {
//            out = httpServletResponse.getWriter();
//            out.write(jsonStr);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (out != null) {
//                out.close();
//            }
//        }
//    }
//}
