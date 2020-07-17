/* https://github.com/cdt */
package org.egg.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.egg.model.DO.WxAccessToken;
import org.egg.model.VO.WxAccessTokenQueryReq;

/**
 * 本文件由 https://github.com/cdt/mybatis-generator-core-chinese-annotation1.3.5-chinese-annotation 自动生成
 * @author cdt
 * date:2018/03/26 18:36
 */
@Mapper
public interface WxAccessTokenDAO extends CustomMapper<WxAccessTokenQueryReq, WxAccessToken, Integer> {
}