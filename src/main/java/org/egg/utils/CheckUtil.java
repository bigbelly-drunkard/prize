package org.egg.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egg.enums.CommonErrorEnum;
import org.egg.exception.CommonException;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * Created by chendatao on 2017/12/3.
 */
@Slf4j
public class CheckUtil {
    /**
     * 检查
     *
     * @param obj
     * @return
     */
    public static final void isNotBlank(String objName, String obj) {
        if (StringUtils.isBlank(obj)) {
            throw new CommonException(CommonErrorEnum.PARAM_ERROR.getCode(), new StringBuilder(objName).append(" 不能为空，为必填项").toString());
        }
    }

    public static final void isNotNull(String objName, Object obj) {
        if (null == obj) {
            throw new CommonException(CommonErrorEnum.PARAM_ERROR.getCode(), new StringBuilder(objName).append(" is null").toString());
        }
    }

    public static final void isNotEmpty(String objName, Object obj) {
        if (null == obj) {
            throw new CommonException(CommonErrorEnum.PARAM_ERROR.getCode(), new StringBuilder(objName).append(" is null").toString());
        }
        if (obj instanceof BigDecimal) {
            if (BigDecimal.ZERO.compareTo((BigDecimal) obj) > -1) {
                throw new CommonException(CommonErrorEnum.PARAM_ERROR.getCode(), new StringBuilder(objName).append(" is less than or equal to zero,is ").append((BigDecimal) obj).toString());

            }
        }
        if (obj instanceof Collection) {
            if (CollectionUtils.isEmpty((Collection) obj)) {
                throw new CommonException(CommonErrorEnum.PARAM_ERROR.getCode(), new StringBuilder(objName).append(" is empty collection").toString());

            }
        }
    }

    /**
     * 将两个经纬度坐标转化成距离（米）
     *
     * @return true:坐标点异常
     * false:坐标点正常
     */
    public static double coordinateToDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        log.info("latitude1={},longitude1={},latitude2={},longitude2={}",latitude1,longitude1,latitude2,longitude2);
        double a = latitude1 * Math.PI / 180.0 - latitude2 * Math.PI / 180.0;
        double b = longitude1 * Math.PI / 180.0 - longitude2 * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(latitude1 * Math.PI / 180.0)
                * Math.cos(latitude2 * Math.PI / 180.0)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378.137 * 1000;
        s = Math.round(s);
        return s;
    }

    public static void main(String[] args) {
        double v = coordinateToDistance(39.793216705322266,116.58599090576172, 39.79087666503906, 116.58033752441406);
        System.out.println(v);
    }


}
