package org.egg.model.DTO;

/**
 * 微信模板消息入参中的data相关实体信息
 * 目前支持到keyword5 如果没有不写就行
 * Created by chendatao on 2018/7/27.
 */
public class WxTemplateMsgData {

    private WxTemplateMsgDataItem first;
    private WxTemplateMsgDataItem keyword1;
    private WxTemplateMsgDataItem keyword2;
    private WxTemplateMsgDataItem keyword3;
    private WxTemplateMsgDataItem keyword4;
    private WxTemplateMsgDataItem keyword5;
    private WxTemplateMsgDataItem remark;

    public WxTemplateMsgDataItem getFirst() {
        return first;
    }

    public void setFirst(WxTemplateMsgDataItem first) {
        this.first = first;
    }

    public WxTemplateMsgDataItem getKeyword1() {
        return keyword1;
    }

    public void setKeyword1(WxTemplateMsgDataItem keyword1) {
        this.keyword1 = keyword1;
    }

    public WxTemplateMsgDataItem getKeyword2() {
        return keyword2;
    }

    public void setKeyword2(WxTemplateMsgDataItem keyword2) {
        this.keyword2 = keyword2;
    }

    public WxTemplateMsgDataItem getKeyword3() {
        return keyword3;
    }

    public void setKeyword3(WxTemplateMsgDataItem keyword3) {
        this.keyword3 = keyword3;
    }

    public WxTemplateMsgDataItem getKeyword4() {
        return keyword4;
    }

    public void setKeyword4(WxTemplateMsgDataItem keyword4) {
        this.keyword4 = keyword4;
    }

    public WxTemplateMsgDataItem getKeyword5() {
        return keyword5;
    }

    public void setKeyword5(WxTemplateMsgDataItem keyword5) {
        this.keyword5 = keyword5;
    }

    public WxTemplateMsgDataItem getRemark() {
        return remark;
    }

    public void setRemark(WxTemplateMsgDataItem remark) {
        this.remark = remark;
    }
}
