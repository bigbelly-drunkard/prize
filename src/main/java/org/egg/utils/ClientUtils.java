package org.egg.utils;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import org.egg.enums.ClientChromeEnum;
import org.egg.enums.ClientSourceEnum;
import org.egg.model.DTO.ClientInfo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author cdt
 * @Description 获取客户端信息工具类
 * @date: 2018/3/5 21:29
 */
public class ClientUtils {

    public static UASparser uasParser = null;

    static {
        try {
            uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
            // java.lang.UnsupportedClassVersionError:
            // cz/mallat/uasparser/UASparser : Unsupported major.minor version 51.0
            // 用jdk1.6测试时会报以上错，需要jdk1.7以上版本支持
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从Request对象中获得客户端IP，处理了HTTP代理服务器和Nginx的反向代理截取了ip
     *
     * @param request
     * @return ip
     */

    private static String getIpAddr(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String forwarded = request.getHeader("X-Forwarded-For") != null ? request.getHeader("X-Forwarded-For") : request.getHeader("j-forwarded-for");
        String realIp = request.getHeader("X-Real-IP");
        String ip = null;
        if (realIp == null) {
            if (forwarded == null) {
                ip = remoteAddr;
            } else {
//                ip = remoteAddr + "/" + forwarded.split(",")[0];
                ip = forwarded.split(",")[0];
            }
        } else {
            if (realIp.equals(forwarded)) {
                ip = realIp;
            } else {
                if (forwarded != null) {
                    forwarded = forwarded.split(",")[0];
                }
//                ip = realIp + "/" + forwarded;
                ip = forwarded;
            }
        }
        return ip;
    }

    /**
     * 返回客户端信息
     *
     * @param request
     * @return
     */
    public static ClientInfo getClientInfo(HttpServletRequest request) {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setIp(getIpAddr(request));
        clientInfo.setUserAgent(request.getHeader("user-agent"));
        StringBuffer url = request.getRequestURL();
        String domain = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();
        clientInfo.setHost(domain);

        if (clientInfo.getUserAgent() != null) {

            if (clientInfo.getUserAgent().contains("iPhone")) {
                clientInfo.setClientSourceEnum(ClientSourceEnum.IOS);
            } else if (clientInfo.getUserAgent().contains("Android")) {
                clientInfo.setClientSourceEnum(ClientSourceEnum.ANDROID);
            } else {
                clientInfo.setClientSourceEnum(ClientSourceEnum.PC);
            }
//         降级处理，全部都是微信浏览器，走微信公众号支付
            if (clientInfo.getUserAgent().toLowerCase().contains("micromessenger")) {
                clientInfo.setClientChromeEnum(ClientChromeEnum.WX);
            } else {
                clientInfo.setClientChromeEnum(ClientChromeEnum.OTHER);

            }
        }

        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (ConstantsUtil.APP_FLAG_COOKIE_NAME.equals(cookie.getName())) {
                    clientInfo.setAppFlag(true);
                }
                if (ConstantsUtil.MINI_FLAG_COOKIE_NAME.equals(cookie.getName())) {
                    clientInfo.setMiniProgramFlag(true);
                }
            }
        }
        return clientInfo;
    }

    public static void setCookie(String key, String value, HttpServletResponse response) {
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
