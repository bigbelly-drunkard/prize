package org.egg.model.DTO;

/**
 * 微信模板消息请求实体
 *
 * 注：url和miniprogram都是非必填字段，若都不传则模板无跳转；
 * 若都传，会优先跳转至小程序。开发者可根据实际需要选择其中一种跳转方式即可。当用户的微信客户端版本不支持跳小程序时，将会跳转至url。
 * Created by chendatao on 2018/7/27.
 */
public class WxTemplateMsgReqDto {
    /**
     *接收者openid
     */
    private String touser;
    /**
     *模板ID
     */
    private String template_id;
    /**
     *模板跳转链接
     */
    private String url;
    /**
     *跳小程序所需数据，不需跳小程序可不用传该数据
     */
    private WxTemplateMsgMinipro miniprogram;
    /**
     * remark
     */
    private WxTemplateMsgData data;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public WxTemplateMsgMinipro getMiniprogram() {
        return miniprogram;
    }

    public void setMiniprogram(WxTemplateMsgMinipro miniprogram) {
        this.miniprogram = miniprogram;
    }

    public WxTemplateMsgData getData() {
        return data;
    }

    public void setData(WxTemplateMsgData data) {
        this.data = data;
    }

}
