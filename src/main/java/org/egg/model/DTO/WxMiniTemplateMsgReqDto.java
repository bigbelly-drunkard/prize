package org.egg.model.DTO;

import java.util.Map;

/**
 * 微信模板消息请求实体
 *
 * 注：url和miniprogram都是非必填字段，若都不传则模板无跳转；
 * 若都传，会优先跳转至小程序。开发者可根据实际需要选择其中一种跳转方式即可。当用户的微信客户端版本不支持跳小程序时，将会跳转至url。
 * Created by chendatao on 2018/7/27.
 */
public class WxMiniTemplateMsgReqDto {
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
    private String page;
    /**
     * remark
     */
    private Map<String,WxTemplateMsgDataItem> data;

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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Map<String, WxTemplateMsgDataItem> getData() {
        return data;
    }

    public void setData(Map<String, WxTemplateMsgDataItem> data) {
        this.data = data;
    }
}
