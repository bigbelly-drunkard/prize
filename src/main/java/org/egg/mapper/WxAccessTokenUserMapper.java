package org.egg.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.egg.model.DO.WxAccessTokenUser;
import org.egg.model.DO.WxAccessTokenUserExample;

public interface WxAccessTokenUserMapper {
    int countByExample(WxAccessTokenUserExample example);

    int deleteByExample(WxAccessTokenUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxAccessTokenUser record);

    int insertSelective(WxAccessTokenUser record);

    List<WxAccessTokenUser> selectByExample(WxAccessTokenUserExample example);

    WxAccessTokenUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WxAccessTokenUser record, @Param("example") WxAccessTokenUserExample example);

    int updateByExample(@Param("record") WxAccessTokenUser record, @Param("example") WxAccessTokenUserExample example);

    int updateByPrimaryKeySelective(WxAccessTokenUser record);

    int updateByPrimaryKey(WxAccessTokenUser record);
}