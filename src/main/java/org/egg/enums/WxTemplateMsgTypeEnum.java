package org.egg.enums;

/**
 * 微信模板消息类型
 * <p>
 * Created by chendatao on 2018/7/27.
 */
public enum WxTemplateMsgTypeEnum {
    PUB_SUCC("PUB_SUCC", "发布成功"),
    PUB_PAY_SUCC("PUB_PAY_SUCC", "发单支付成功"),
    ACCEPT_GAME_SUCC("ACCEPT_SUCC", "派单成功"),
    ACCEPT_PLAY_SUCC("ACCEPT_PLAY_SUCC", "接单成功"),
    RECHARGE_SUCC("RECHARGE_SUCC", "充值成功"),
    CASH_PENDING("CASH_PENDING", "提现中"),
    CASH_SUCC("CASH_SUCC", "提现到账"),
    DELAY_GAME("DELAY_GAME", "玩家延期个人/全部通知"),
    DELAY_PALY("DELAY_PALY", "代练延期个人/全部通知"),
//    FINISH_SUCC_GAME_DETAIL("FINISH_SUCC_GAME_DETAIL", "结算成功跳代练订单详情页"),
//    FINISH_SUCC_PLAY_DETAIL("FINISH_SUCC_PLAY_DETAIL", "结算成功跳玩家订单详情页"),
    FINISH_SUCC_BALANCE("FINISH_SUCC_BALANCE", "代练结算成功跳余额页"),
    RECOMMEND_REGISTER("RECOMMEND_REGISTER", "被推荐人注册成功"),
    RECOMMEND_AMOUNT("RECOMMEND_AMOUNT", "推荐人返佣奖励"),
    CHECK_FAIL("CHECK_FAIL", "审核失败"),
    TEST_RECHARGE("TEST_RECHARGE", "充值提醒"),
    //    mini
    PUB_ACCEPT_PENDING("PUB_ACCEPT_PENDING","被接单通知发单者"),
    PUB_SUCC_MINI("PUB_SUCC_MINI","发布单完成通知发布者"),
    ACCEPT_FLOW_SUCC("ACCEPT_FLOW_SUCC","接单资金完成通知接单者；"),
    ADMIN_CLOSE_MINI_PUB("ADMIN_CLOSE_MINI_PUB","订单系统关闭通知发单人"),
    ADMIN_CLOSE_MINI_ACC("ADMIN_CLOSE_MINI_ACC","订单系统关闭通知跑手"),
    SUBSCRIPTION("SUBSCRIPTION","关注成功通知"),
    ;

    private String code;
    private String desc;

    WxTemplateMsgTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
