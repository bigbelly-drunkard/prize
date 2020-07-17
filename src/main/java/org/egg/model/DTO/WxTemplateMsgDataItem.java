package org.egg.model.DTO;

/**
 * 微信模板消息入参中的data中item相关实体信息
 *
 * Created by chendatao on 2018/7/27.
 */
public class WxTemplateMsgDataItem {
    public WxTemplateMsgDataItem(String value) {
        this.value = value;
    }

    public WxTemplateMsgDataItem(String value, String color) {
        this.value = value;
        this.color = color;
    }

    /**
     *	模板数据
     */
    private String value;
    /**
     *	模板内容字体颜色，不填默认为黑色
     */
    private String color;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
