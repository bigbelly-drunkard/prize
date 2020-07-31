package org.egg.controller;

import org.egg.cache.LocalCache;
import org.egg.utils.ConstantsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by chendatao on 2017/12/10.
 */
@Component
public class BaseController {
    public static final String ERROR_404 = "/error/404";
    public static final String ERROR_500 = "/error/500";
    @Autowired
    private LocalCache localCache;
    /**
     * 获取session用户OPENID
     *
     * @param request
     * @return
     */
    public String getOpenId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String attribute = (String) session.getAttribute(ConstantsUtil.OPEN_ID_KEY);
        return attribute;
    }

    /**
     * 小程序获取openId
     * @param request
     * @return
     */
    public String getOpenId4Mini(HttpServletRequest request) {
        String header = request.getHeader(ConstantsUtil.OPEN_ID_KEY);
        return header;
    }

    /**
     * 从cookie中获取值
     *
     * @param request
     * @param key
     * @return
     */
    public String getValueForCookieByKey(HttpServletRequest request, String key) {
        String result = null;
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            if (cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(key)) {
                        return cookie.getValue();
                    }
                }
            }
        }
        return result;
    }

    /**
     * 无有效期写入cookie
     *
     * @param response
     * @param key
     * @param value
     */
    public void setCookie(HttpServletResponse response, String key, String value) {
        key = key.trim();
//        写入cookie中
        Cookie cookie = new Cookie(key, value);
        // tomcat下多应用共享
        cookie.setPath("/");
        // 如果cookie的值中含有中文时，需要对cookie进行编码，不然会产生乱码
        try {
            URLEncoder.encode(key, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        cookie.setMaxAge(-1);
        // 将Cookie添加到Response中,使之生效
        response.addCookie(cookie); // addCookie后，如果已经存在相同名字的cookie，则最新的覆盖旧的cookie
    }

}
