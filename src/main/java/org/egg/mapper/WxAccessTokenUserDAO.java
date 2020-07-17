/* https://github.com/cdt */
package org.egg.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.egg.model.DO.WxAccessTokenUser;
import org.egg.model.VO.WxAccessTokenUserQueryReq;

import java.util.List;

/**
 * 本文件由 https://github.com/cdt/mybatis-generator-core-chinese-annotation1.3.5-chinese-annotation 自动生成
 * @author cdt
 * date:2018/03/26 18:36
 */
@Mapper
public interface WxAccessTokenUserDAO extends CustomMapper<WxAccessTokenUserQueryReq, WxAccessTokenUser, Integer> {
    List<WxAccessTokenUser> queryTokenNew();
    List<WxAccessTokenUser> queryTokenOld();
}