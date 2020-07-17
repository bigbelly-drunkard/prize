package org.egg.Interceptor;

import org.apache.commons.lang3.StringUtils;
import org.egg.utils.ClientUtils;
import org.egg.utils.ConstantsUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cdt
 * @Description app interceptor判断是否是app
 * @date: 2017/11/7 16:36
 */
public class AppInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String isApp = httpServletRequest.getParameter("isApp");
        if (StringUtils.isNotBlank(isApp) && Boolean.parseBoolean(isApp)) {
//            请求来自app
            ClientUtils.setCookie(ConstantsUtil.APP_FLAG_COOKIE_NAME,"true",httpServletResponse);
        } else {
            ClientUtils.setCookie(ConstantsUtil.APP_FLAG_COOKIE_NAME,"false",httpServletResponse);

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }


}
