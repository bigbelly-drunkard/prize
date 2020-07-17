package org.egg.service.impl;

import org.egg.mapper.WxAccessTokenUserDAO;
import org.egg.model.DO.WxAccessTokenUser;
import org.egg.model.VO.WxAccessTokenUserQueryReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chendatao
 */
@Service
public class WxAccessTokenUserServiceImpl {
    @Autowired
    private WxAccessTokenUserDAO wxAccessTokenUserDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(WxAccessTokenUserServiceImpl.class);

    
    public int queryCount(WxAccessTokenUserQueryReq example) {
        int count = this.wxAccessTokenUserDAO.queryCount(example);
        LOGGER.debug("count: {}", count);
        return count;
    }

    
    public WxAccessTokenUser selectByPrimaryKey(Integer id) {
        return this.wxAccessTokenUserDAO.selectByPrimaryKey(id);
    }

    
    public List<WxAccessTokenUser> queryList(WxAccessTokenUserQueryReq example) {
        return this.wxAccessTokenUserDAO.queryList(example);
    }

    
    public int deleteByPrimaryKey(Integer id) {
        return this.wxAccessTokenUserDAO.deleteByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(WxAccessTokenUser record) {
        return this.wxAccessTokenUserDAO.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(WxAccessTokenUser record) {
        return this.wxAccessTokenUserDAO.updateByPrimaryKey(record);
    }

    
    public int insert(WxAccessTokenUser record) {
        return this.wxAccessTokenUserDAO.insert(record);
    }



    public int insertSelective(WxAccessTokenUser record) {
        return this.wxAccessTokenUserDAO.insertSelective(record);
    }

    public List<WxAccessTokenUser> queryTokenNew() {
       return wxAccessTokenUserDAO.queryTokenNew();
    }
    public List<WxAccessTokenUser> queryTokenOld() {
        return wxAccessTokenUserDAO.queryTokenOld();
    }
}