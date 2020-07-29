package org.egg.biz;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.egg.cache.LocalCache;
import org.egg.model.VO.MsgRes;
import org.egg.response.CommonListResult;
import org.egg.template.BizTemplate;
import org.egg.template.TemplateCallBack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/29 18:26
 */
@Component
@Slf4j
public class MsgBiz {
    @Autowired
    private BizTemplate bizTemplate;
    @Autowired
    private LocalCache localCache;

    /**
     * 查询所有消息
     * @return
     */
    public CommonListResult<MsgRes> queryMsgList() {
        log.info("queryMsgList start");
        CommonListResult<MsgRes> msgResCommonListResult = new CommonListResult<>();
        bizTemplate.process(msgResCommonListResult, new TemplateCallBack() {
            @Override
            public void doCheck() {

            }

            @Override
            public void doAction() {
                Map<String, String> msgMap = localCache.getMsgMap();
                ArrayList<MsgRes> msgRes1 = new ArrayList<>(msgMap.size());
                msgMap.forEach((key, value) -> {
                    MsgRes msgRes = new MsgRes();
                    Date date = new Date();
                    date.setTime(Long.valueOf(key));
                    msgRes.setDate(date);
                    msgRes.setMsg(value);
                    msgRes1.add(msgRes);
                });
                msgResCommonListResult.setData(msgRes1);
            }
        });
        log.info("queryMsgList msgResCommonListResult={}", JSONObject.toJSONString(msgResCommonListResult));
        return msgResCommonListResult;
    }

    public void robotMsg() {

    }

}
