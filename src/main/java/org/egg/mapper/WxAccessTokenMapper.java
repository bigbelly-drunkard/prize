package org.egg.mapper;

import org.apache.ibatis.annotations.Param;
import org.egg.model.DO.WxAccessToken;
import org.egg.model.DO.WxAccessTokenExample;

import java.util.List;

public interface WxAccessTokenMapper {
    int countByExample(WxAccessTokenExample example);

    int deleteByExample(WxAccessTokenExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WxAccessToken record);

    int insertSelective(WxAccessToken record);

    List<WxAccessToken> selectByExample(WxAccessTokenExample example);

    WxAccessToken selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WxAccessToken record, @Param("example") WxAccessTokenExample example);

    int updateByExample(@Param("record") WxAccessToken record, @Param("example") WxAccessTokenExample example);

    int updateByPrimaryKeySelective(WxAccessToken record);

    int updateByPrimaryKey(WxAccessToken record);
}