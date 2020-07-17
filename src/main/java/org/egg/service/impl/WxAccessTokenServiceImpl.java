package org.egg.service.impl;

import org.egg.mapper.WxAccessTokenDAO;
import org.egg.model.DO.WxAccessToken;
import org.egg.model.VO.WxAccessTokenQueryReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chendatao
 */
@Service
public class WxAccessTokenServiceImpl  {
    @Autowired
    private WxAccessTokenDAO wxAccessTokenDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(WxAccessTokenServiceImpl.class);

    public int queryCount(WxAccessTokenQueryReq example) {
        int count = this.wxAccessTokenDAO.queryCount(example);
        LOGGER.debug("count: {}", count);
        return count;
    }

    public WxAccessToken selectByPrimaryKey(Integer id) {
        return this.wxAccessTokenDAO.selectByPrimaryKey(id);
    }

    public List<WxAccessToken> queryList(WxAccessTokenQueryReq example) {
        return this.wxAccessTokenDAO.queryList(example);
    }

    
    public int deleteByPrimaryKey(Integer id) {
        return this.wxAccessTokenDAO.deleteByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(WxAccessToken record) {
        return this.wxAccessTokenDAO.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(WxAccessToken record) {
        return this.wxAccessTokenDAO.updateByPrimaryKey(record);
    }

    public int insert(WxAccessToken record) {
        return this.wxAccessTokenDAO.insert(record);
    }

    public int insertSelective(WxAccessToken record) {
        return this.wxAccessTokenDAO.insertSelective(record);
    }

}