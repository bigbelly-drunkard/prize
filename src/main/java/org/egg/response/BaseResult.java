package org.egg.response;

import org.egg.enums.CommonErrorEnum;

import java.io.Serializable;

/**
 * @author cdt
 * @Description
 * @date: 2017/11/7 15:21
 */
public class BaseResult implements Serializable{

    public static BaseResult builder() {
        return new BaseResult();
    }
    public BaseResult() {
    }

    public BaseResult(String respCode, String respDesc) {
        this.respCode = respCode;
        this.respDesc = respDesc;
    }
    public BaseResult(CommonErrorEnum commonErrorEnum) {
        this.respCode = commonErrorEnum.getCode();
        this.respDesc = commonErrorEnum.getDescription();
    }

    public void setError(CommonErrorEnum commonErrorEnum) {
        this.respCode = commonErrorEnum.getCode();
        this.respDesc = commonErrorEnum.getDescription();
    }
    private static final long serialVersionUID = -8211157881330847461L;
    private boolean isSuccess = false;
    private String respCode;
    private String respDesc;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

}
