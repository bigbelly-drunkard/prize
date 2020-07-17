package org.egg.integration.wx;

import com.github.wxpay.sdk.WXPayConfig;
import org.egg.utils.PropertiesUtil;
import org.egg.utils.WxUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author cdt
 * @Description
 * @date: 2018/3/29 16:37
 */
public class WxConfig implements WXPayConfig {
    private byte[] certData;

    public WxConfig() throws Exception {
        String certPath = PropertiesUtil.getProperty("mch.cert.path");
//        if ("test".equals(PropertiesUtil.getProperty("mch.cert.path"))) {
//             certPath = "D:\\commonSFW\\DOWNLOAD\\WXCertUtil\\cert\\apiclient_cert.p12";
//        } else {
//            certPath = "D:\\commonSFW\\DOWNLOAD\\WXCertUtil\\cert\\apiclient_cert.p12";
//        }
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    @Override
    public String getAppID() {
        return WxUtil.APP_ID;
    }

    @Override
    public String getMchID() {
        return WxUtil.MCH_ID;
    }

    @Override
    public String getKey() {
        return WxUtil.KEY;
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

}
