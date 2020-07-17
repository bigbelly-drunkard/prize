package org.egg.Interceptor;

import org.egg.cache.LocalCache;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dataochen
 * @Description login interceptor
 * @date: 2017/11/7 16:36
 * @since 2020-1-17 14:20:14 目前小程序没有真正的管理session
 */
public class MiniLoginInterceptor implements HandlerInterceptor {
    public MiniLoginInterceptor(LocalCache localCache) {
        this.localCache = localCache;
    }

    private LocalCache localCache;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String openId = httpServletRequest.getHeader("openId");
        if (openId == null || localCache.getUserByMiniOpenId(openId) == null) {
            httpServletResponse.setStatus(403);
            return false;
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
