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
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
            File file = ResourceUtils.getFile("classpath:file/nickName.txt");
//            URL systemResource = ClassLoader.getSystemResource("file/nickName.txt");
//            String file1 = systemResource.getFile();
//            File file = new File(file1);
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
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
     * type 0:全部 1：积分活动
     *
     * @return
     */
    public CommonListResult<MsgRes> queryMsgList(int type) {
        log.info("queryMsgList start");
        CommonListResult<MsgRes> msgResCommonListResult = new CommonListResult<>();
        bizTemplate.process(msgResCommonListResult, new TemplateCallBack() {
            @Override
            public void doCheck() {
            }

            @Override
            public void doAction() {
                Map<String, String> msgMap = new HashMap<String, String>();
                switch (type) {
                    case 0:
                        msgMap = localCache.getMsgMap();
                        break;
                    case 1:
                        msgMap = localCache.getMsgMapPaoMaDeng();
                        break;
                    case 2:
                        msgMap = localCache.getMsgMapZhuanPan();
                        break;
                    default:
                        break;
                }
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
        int v = (int) (Math.random() * 5);
        for (int i = 0; i < v; i++) {
            String msg = getMsg();
            localCache.addMsg(msg);
            //        分类记录 1.跑马灯活动
            if (msg.startsWith("[积分活动]:")) {
                localCache.addMsgPaoMaDeng(msg.substring("[积分活动]:".length()));
            }
            if (msg.startsWith("[金豆活动]:")) {
                localCache.addMsgZhuanPan(msg.substring("[金豆活动]:".length()));
            }

        }
    }

    public void addRealMsg4PaoMaDeng(String msg) {
        localCache.addMsgPaoMaDeng(msg);
    }
public void addRealMsg4ZhuanPan(String msg) {
        localCache.addMsgZhuanPan(msg);
    }

    private String getNickName() {
        double v = Math.random() * nickNameCache.size();
        return nickNameCache.get((int) v);
    }


    /**
     * //恭喜 张三 在 积分商城 中获得120积分；恭喜张三在积分活动中获得100积分；恭喜张三在积分活动中获得100金豆；恭喜张三在积分活动中获得100元现金红包；
     * // 恭喜张三 完成每日任务 领取100积分；恭喜张三开通vip会员，享受独有特权；恭喜张三在金豆活动中获取100元现金红包；恭喜张三在金豆活动总获的4积分；
     * // 恭喜张三在金豆活动总获的4金豆；张三在金豆兑换中使用100积分换了10金豆；恭喜张三金豆提现10元现金；张三在金豆商品中获取xx商品；
     * //
     *
     * @return
     */
    private String getMsg() {
        String msg = "";
        String nickName = getNickName();
        int v = (int) (Math.random() * 35) + 1;
        switch (v) {
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
            case 9:
                msg = "[积分商城]:恭喜 " + nickName + " 获得6480积分礼包一份";
                break;
            case 10:
                msg = "[金豆活动]:恭喜 " + nickName + " 获得随机红包";
                break;
            case 11:
                msg = "[金豆活动]:恭喜 " + nickName + " 获得1元红包";
                break;
            case 12:
                msg = "[金豆活动]:恭喜 " + nickName + " 获得2元金豆";
                break;
            case 13:
                msg = "[金豆活动]:恭喜 " + nickName + " 获得5元红包";
                break;
            case 14:
                msg = "[金豆活动]:恭喜 " + nickName + " 获得100元红包";
                break;
            case 15:
                msg = "[金豆活动]:恭喜 " + nickName + " 获得200元金豆";
                break;
            case 16:
                msg = "[金豆活动]:恭喜 " + nickName + " 获得3000元红包";
                break;
            case 17:
                msg = "[金豆活动]:恭喜 " + nickName + " 获得25元随机金豆";
                break;
            case 18:
                msg = "[领取积分]:恭喜 " + nickName + " 签到成功领取10积分";
                break;
            case 19:
                msg = "[领取积分]:恭喜 " + nickName + " 分享成功领取15积分";
                break;
            case 20:
                msg = "[开通会员]:恭喜 " + nickName + " 开通vip会员，享受独有特权";
                break;
            case 21:
                msg = "[金豆兑换]:恭喜 " + nickName + " 兑换成功1金豆";
                break;
            case 22:
                msg = "[金豆兑换]:恭喜 " + nickName + " 兑换成功6金豆";
                break;
            case 23:
                msg = "[金豆兑换]:恭喜 " + nickName + " 兑换成功50金豆";
                break;
            case 24:
                msg = "[金豆兑换]:恭喜 " + nickName + " 兑换成功100金豆";
                break;
            case 25:
                msg = "[金豆兑换]:恭喜 " + nickName + " 兑换成功300金豆";
                break;
            case 26:
                msg = "[金豆兑换]:恭喜 " + nickName + " 兑换成功500金豆";
                break;
            case 27:
                msg = "[积分活动]:恭喜 " + nickName + " 获得随机金豆";
                break;
            case 28:
                msg = "[积分活动]:恭喜 " + nickName + " 获得1元金豆";
                break;
            case 29:
                msg = "[积分活动]:恭喜 " + nickName + " 获得2元积分";
                break;
            case 30:
                msg = "[积分活动]:恭喜 " + nickName + " 获得5元金豆";
                break;
            case 31:
                msg = "[积分活动]:恭喜 " + nickName + " 获得100元金豆";
                break;
            case 32:
                msg = "[积分活动]:恭喜 " + nickName + " 获得200元积分";
                break;
            case 33:
                msg = "[积分活动]:恭喜 " + nickName + " 获得3000元金豆";
                break;
            case 34:
                msg = "[积分活动]:恭喜 " + nickName + " 获得25元随机金豆";
                break;
            case 35:
                msg = "[金豆提现]:恭喜 " + nickName + " 提现" + ((int) (Math.random() * 999) + 1) + "元现金";
                break;
            default:
                break;
        }
        return msg;
    }


}
