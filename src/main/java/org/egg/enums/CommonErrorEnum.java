package org.egg.enums;

/**
 * @author cdt
 * @Description PRO1100001 renting-参数-参数为空【PRO-11-00001】
 * PRO2200001 renting-RPC-rpc调用失败【PRO-22-00001】
 * PRO3300001 renting-通用-连接超限【PRO-33-00001】
 * PRO9900001 renting-系统-系统错误【PRO-99-00001】
 * @date: 2017/11/7 15:32
 */
public enum CommonErrorEnum {
    /**
     *
     */
    NULL("", ""),

    //成功
    SUCCESS("000000", "success"),
    /**
     * 入参为空
     */
    PARAM_NULL("PRO1100001", "param is null"),
    /**
     * 入参错误
     */
    PARAM_ERROR("PRO1100002", "param is error"),
    /**
     * 注册时密码不一致
     */
    REGISTER_INCONFORMITY_PASSWORD("PRO1100003", "The password is not consistent when registered"),
    /**
     * 登录名已存在
     */
    PARAM_LOGINNAME_EXIST("PRO1100004", "The login name already exists"),
    /**
     * 登录名不存在
     */
    PARAM_LOGINNAME_NOT_EXIST("PRO1100005", "The login name not exists"),
    /**
     * 解密错误
     */
    PARAM_ENCRY_ERROR("PRO1100006", "decryption failures"),
    /**
     * 订单不存在
     */
    PARAM_ORDER_NOT_EXIST("PRO1100007", "order not exist"),
    /**
     * 账户不存在
     */
    PARAM_LOGIN_NOT_EXIST("PRO1100008", "The acc not exists"),
    /**
     * 文件不存在
     */
    PARAM_FILE_NOT_EXIST("PRO1100009", "The file not exists"),
    /**
     * 余额不足
     */
    PARAM_BALANCE_NOT_ENOUGH("PRO1100010", "balance is not enough"),
    /**
     * 黑名单单子
     */
    ORDER_BLACK_LIST("PRO1100011", "Black list"),
    /**
     * 注册时 推荐码不对
     */
    REGISTER_AGENT_CODE_ERROR("PRO1100012", "register agent code is error"),

    /**
     * 获取微信普通access_token错误
     */
    WX_ACCESS_TOKEN_MERCHANT_ERROR("PRO2200001", "get wx common access_token is error"),
    /**
     * 获取微信网页授权access_token错误
     */
    WX_ACCESS_TOKEN_ERROR("PRO2200002", "get wx  access_token is error"),
    /**
     * 获取微信jsapi_ticket错误
     */
    WX_JSAPI_TICKET_ERROR("PRO2200003", "get wx  jsapi_ticket is error"),


    /**
     * 获取不到wx_flow_detail表的信息 wxFlowNo参数不对
     */
    WX_FLOW_DETAIL_NOT_FIND("PRO2200004", "wx flow detail not find"),

    /**
     * 幂等返回
     */
    WX_IDEMPOTENT("PRO2200005", "wx_flow_detail alread exist,idempotent return"),
    /**
     * 金额不对
     */
    WX_AMOUNT_CHECK_ERROR("PRO2200006", "wx amount check error"),
    /**
     * 微信返回不对
     */
    WX_PARAM_ERROR("PRO2200007", "wx param is error"),
    /**
     * 微信支付统一下单
     */
    WX_PAY_ORDER_ERROR("PRO2200008", "wx prepay is error"),
    /**
     * 获取不到account_flow表的信息
     */
    ACCOUNT_FLOW_NOT_FIND("PRO2200009", "account flow not find"),

    VALIDATE_CODE_CONSISTENT("PRO2200010", "校验短信验证码不一致"),


    /**
     * 账号异常
     */
    ACCOUNT_EXCEPTION("PRO3300001", "account exception"),
    /**
     * 账号密码不匹配
     */
    ACCOUNT_PASSWORD_ERROR("PRO3300002", "The account password does not match"),
    /**
     * 账号登录实现或没有登录
     */
    ACCOUNT_LOGIN_NOT("PRO3300003", "Account login implementation or not logged in"),

    /**
     * 上传文件大小超限
     */
    FILE_SIZE_MAX_LIMIT("PRO3300004", "Upload file size limit"),
    /**
     * 文件上传失败
     */
    FILE_UPLOAD_FAIL("PRO3300005", "Upload file is fail"),
    /**
     * 文件删除失败
     */
    FILE_DELETE_FAIL("PRO3300006", "Delete file is fail"),
    /**
     * 无商品信息
     */
    NO_GOODS_INFO_ERROR("PRO3300007", "No commodity information"),
    /**
     * 上传文件异常
     */
    UPLOAD_FILE_EXCEPTION("PRO3300008", "uploading file exception"),
    /**
     * 上传文件格式错误
     */
    FILE_TYPE_LIMIT("PRO3300009", "Error uploading file format"),

    /**
     * 数据库无更新异常
     */
    DB_UPDATE_VERSION_EXCEPTION("PRO3300011", "db update none"),
    /**
     * 重复支付
     */
    DOUBLE_PAYMENT_ERROR("PRO3300012", "DOUBLE_PAYMENT_ERROR"),
    /**
     * 微信企业支付未知错误
     */
    WX_COMPANY_PAY_SYSERROR("PRO3300013", "WX company pay SYSTEMERROR"),
    /**
     * 退款申请失败
     */
    WX_REFUND_FAIL("PRO3300014", "WX_REFUND_FAIL"),
    /**
     * 积分不足
     */
    SCORE_NOT_ENOUGH("PRO3300015", "SCORE_NOT_ENOUGH"),
    /**
     * 金豆不足
     */
    GOLD_NOT_ENOUGH("PRO3300016", "GOLD_NOT_ENOUGH"),


    /**
     * 系统异常
     */
    SYSTEM_EXCEPTION("PRO9900001", "system error"),
    /**
     * 不应该走的位置
     */
    WARN_ERROR_TIP("PRO9900002", "WARN_ERROR_TIP"),
    /**
     * 重复次数太多 超限
     */
    WARN_ERROR_DUPLICATE_LIMIT("PRO9900003", "WARN_ERROR_DUPLICATE_LIMIT"),;
    /**
     * 代码
     */
    private String code = null;
    /**
     * 描述
     */
    private String description = null;

    /**
     * 构造方法
     *
     * @param code
     * @param description
     */
    CommonErrorEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 根据业务码获得业务类型
     *
     * @param code 业务码
     * @return 业务类型
     */
    public static CommonErrorEnum getEnumByCode(String code) {
        CommonErrorEnum[] values = CommonErrorEnum.values();
        for (CommonErrorEnum operate : values) {
            if (operate.getCode().equals(code)) {
                return operate;
            }
        }
        return NULL;
    }
}
