package org.egg.model.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author cdt
 * @Description 获取/刷新accessToken的结果类
 * @date: 2018/1/19 15:41
 */
@Data
public class AccessTokenMiniResult implements Serializable {
    private static final long serialVersionUID = 2899067009190351905L;
    /**
     *用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明。
     * @link {https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html}
     */
    private String unionid;
    /**
     *	用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
     */
    private String openid;
    /**
     * 会话密钥

     */
    @JSONField(serialize=false)
    private String session_key;

    private boolean bindTelFlag;
    private String realStatus;

}
