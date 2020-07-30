package org.egg.utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author cdt
 * @Description 常量类
 * @date: 2017/11/12 19:21
 */
public class ConstantsUtil {
    /**
     * 降级开关
     */
    public static final Boolean WX_JOB_SWITH = true;
    /**
     * session存储用户信息key
     */
    public static final String USER_KEY = "USER";
    public static final String BOSS_USER_KEY = "BOSS_USER";
    public static final String BOSS_USER_NAME = "cdt";
    public static final String BOSS_USER_PWD = "mjj";
    public static final String APP_FLAG_COOKIE_NAME = "isApp";
    public static final String MINI_FLAG_COOKIE_NAME = "isMini";
    public static final String MINI_UINFO_COOKIE_NAME = "uinfo";
    //    js 版本号
//    public static final String JS_VERSION = String.valueOf(System.currentTimeMillis());
    public static final String JS_VERSION = "0002";
    /**
     * 推荐人信息cookie key
     */
    public static final String RECOMMEND_KEY = "REKEYCOM";
    /**
     * 用户编号cookie key
     */
    public static final String UMU = "umu";
    /**
     * 用户编号签名cookie key
     */
    public static final String SNO = "sno";
    /**
     * session存储用户OPENID key
     */
    public static final String OPEN_ID_KEY = "OPENID";
    /**
     * 文件上传最大大小1024*1024 1M
     */
    public static final Long MAX_FILE_SIZE = 1024 * 1024L;
    /**
     * 多个文件url分割符
     */
    public static final String FILE_SPLIT = ";";
    /**
     * 上传文件格式
     */
    public static final List<String> pictureType = Arrays.asList("PNG", "IMG");
    /**
     * 手续费表中的分隔符
     */
    public static final String FEE_RULE_SPLIT_TAG=";";

    /**
     * 推荐人推荐的用户注册成功奖励信誉分
     */
    public static final Integer REGISTER_SUCC_REWARD_RECOMMEND_SCORE=10;
    /**
     * 推荐人推荐的用户发单正常结算成功奖励平台手续费占比
     */
    public static final BigDecimal REGISTER_SUCC_REWARD_RECOMMEND_FEE=new BigDecimal("0.5");

    /**
     * 代理 推荐人推荐的用户发单正常结算成功奖励平台手续费占比
     */
    public static final BigDecimal REGISTER_SUCC_AGENT_REWARD_RECOMMEND_FEE=new BigDecimal("0.8");




}
