package org.egg.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.egg.model.DO.User;
import org.egg.model.VO.UserQueryReq;

import java.util.List;

@Mapper
public interface UserExtDAO {

    int queryCount(UserQueryReq example);
    List<User> queryList(UserQueryReq example);

    public List<String> queryAllMiniOpenId();
}