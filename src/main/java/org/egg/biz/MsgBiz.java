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

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

    private ArrayList<String> nickNameCache = new ArrayList<>(1024);

    @PostConstruct
    private void init() throws IOException {
//        初始化昵称
        try {
            File file = new File("file/nickName.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                nickNameCache.add(str);
            }
        } catch (Exception e) {
            log.error("读取nickName文件错误 初始化失败");
            throw e;
        }
    }

    /**
     * 查询所有消息
     *
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
                    Date date = new Date(Long.valueOf(key));
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

    /**
     * 一秒一次
     * 每次随机创建0~5个消息
     */
    public void robotMsg() {
        int v = (int)Math.random() * 5;
        for (int i = 0; i <v ; i++) {
            String msg = getMsg();
            localCache.addMsg(msg);
        }
    }

    private String getNickName() {
        double v = Math.random() * nickNameCache.size();
        return nickNameCache.get((int) v);
    }

    //恭喜 张三 在 积分商城 中获得120积分；恭喜张三在积分活动中获得100积分；恭喜张三在积分活动中获得100金豆；恭喜张三在积分活动中获得100元现金红包；
// 恭喜张三 完成每日任务 领取100积分；恭喜张三开通vip会员，享受独有特权；恭喜张三在金豆活动中获取100元现金红包；恭喜张三在金豆活动总获的4积分；
// 恭喜张三在金豆活动总获的4金豆；张三在金豆兑换中使用100积分换了10金豆；恭喜张三金豆提现10元现金；张三在金豆商品中获取xx商品；
//    todo
    private String getMsg() {
        String msg = "";
        String nickName = getNickName();
        int v = (int) Math.random() * 10;
        switch (v % 10) {
            case 1:
                msg = "[积分商城]:恭喜 " + nickName + " 获得10积分礼包一份";
                break;
            case 2:
                msg = "[积分商城]:恭喜 " + nickName + " 获得60积分礼包一份";
                break;
            case 3:
                msg = "[积分商城]:恭喜 " + nickName + " 获得450积分礼包一份";
                break;
            case 4:
                msg = "[积分商城]:恭喜 " + nickName + " 获得680积分礼包一份";
                break;
            case 5:
                msg = "[积分商城]:恭喜 " + nickName + " 获得1180积分礼包一份";
                break;
            case 6:
                msg = "[积分商城]:恭喜 " + nickName + " 获得1980积分礼包一份";
                break;
            case 7:
                msg = "[积分商城]:恭喜 " + nickName + " 获得3480积分礼包一份";
                break;
            case 8:
                msg = "[积分商城]:恭喜 " + nickName + " 获得6480积分礼包一份";
                break;
            // TODO: 2020/7/30 最后等所有商品指定后开发
            default:
                break;
        }
        return msg;
    }


}
