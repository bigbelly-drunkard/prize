package org.egg.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dataochen
 * @Description 公共util
 * @date: 2019/1/3 19:44
 */
@Slf4j
public class CommonUtil {

    /**
     * 解决sendRedirect https跳转http问题
     * 由于nginx配置的https调用http 故会出现此情况
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param path
     * @throws IOException
     */
    public static void sendRedirectForHttps(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String path) throws IOException {
        String serverName = httpServletRequest.getServerName();
        StringBuilder sbd = new StringBuilder();
        if (path.contains("https")) {
            httpServletResponse.sendRedirect(path);
            return;
        }
        // 强制使用https
        sbd.append("https").append("://").append(serverName).append(path);
        httpServletResponse.sendRedirect(sbd.toString());
    }

    /**
     * 随机分配红包
     *
     * @param total
     * @param userTotal
     * @return
     */
    public static List<BigDecimal> randomRedPackage(BigDecimal total, Integer userTotal) {
        log.info("随机分配红包开始 total={},userTotal={}", total, userTotal);
        ArrayList<BigDecimal> bigDecimals = new ArrayList<>();
        for (int i = 0; i < userTotal - 1; i++) {
            BigDecimal amount = randomAmount(total);
            bigDecimals.add(amount);
            total = total.subtract(amount);
        }
        bigDecimals.add(total);
        log.info("随机后的金额为={}", JSONObject.toJSONString(bigDecimals));
        return bigDecimals;
    }

    private static BigDecimal randomAmount(BigDecimal total) {
        return total.multiply(new BigDecimal(Math.random() + ""));
    }
}
