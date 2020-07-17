package org.egg.model.DTO;

import java.io.Serializable;

/**
 * @author cdt
 * @Description 微信支付 通知结果实体
 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
 * @date: 2018/3/30 22:17
 */
public class WxNotifyDto extends WxUDto implements Serializable {

    private static final long serialVersionUID = 3775197545492086097L;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        StringBuilder sbSuper = new StringBuilder(super.toString());
        sbSuper.replace(0, 1, ",");
        sbSuper.deleteCharAt(sbSuper.length() - 1);
        sb.append(sbSuper.toString());
        sb.append('}');
        return sb.toString();
    }
}
