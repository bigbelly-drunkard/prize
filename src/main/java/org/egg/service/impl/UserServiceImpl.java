package org.egg.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.egg.mapper.UserDAO;
import org.egg.mapper.UserExtDAO;
import org.egg.model.DO.User;
import org.egg.model.DO.UserExample;
import org.egg.model.VO.UserQueryReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "userServiceImpl")
public class UserServiceImpl extends AbstractService<User> {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserExtDAO userExtDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    public User selectByPrimaryKey(Long id) {
        return this.userDAO.selectByPrimaryKey(id);
    }

    public int queryCount(UserQueryReq example) {
        UserExample userExample = new UserExample();
        convert(example, userExample);
        return userDAO.countByExample(userExample);
    }

    public List<User> queryList(UserQueryReq example) {
        UserExample userExample = new UserExample();
        convert(example, userExample);
        List<User> userStus = userDAO.selectByExample(userExample);
        return userStus;
    }

    public List<String> queryAllMiniOpenId() {
        return userExtDAO.queryAllMiniOpenId();
    }


    public int deleteByPrimaryKey(Long id) {
        return this.userDAO.deleteByPrimaryKey(id);
    }


    public int updateByPrimaryKeySelective(User record) {
        return this.userDAO.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(User record) {
        return this.userDAO.updateByPrimaryKey(record);
    }


    public int insert(User record) {
        return this.userDAO.insert(record);
    }


    public int insertSelective(User record) {
        return this.userDAO.insertSelective(record);
    }

    @Override
    int updateService(User record) {
        return this.userDAO.updateByPrimaryKeySelective(record);
    }

    private void convert(UserQueryReq example, UserExample userExample) {
        UserExample.Criteria criteria = userExample.createCriteria();
        if (StringUtils.isNotBlank(example.getUserNo())) {
            criteria.andUserNoEqualTo(example.getUserNo());
        }
        if (example.getId() != null) {
            criteria.andIdEqualTo(example.getId());
        }
        if (StringUtils.isNotBlank(example.getRealName())) {
            criteria.andRealNameEqualTo(example.getRealName());
        }
        if (StringUtils.isNotBlank(example.getGender())) {
            criteria.andGenderEqualTo(example.getGender());
        }
        if (StringUtils.isNotBlank(example.getTelNo())) {
            criteria.andTelNoEqualTo(example.getTelNo());
        }
        if (StringUtils.isNotBlank(example.getPassword())) {
            criteria.andPasswordEqualTo(example.getPassword());
        }
        if (StringUtils.isNotBlank(example.getWxOpenId())) {
            criteria.andWxOpenIdEqualTo(example.getWxOpenId());
        }
        if (StringUtils.isNotBlank(example.getWxMiniOpenId())) {
            criteria.andWxMiniOpenIdEqualTo(example.getWxMiniOpenId());
        }
        if (StringUtils.isNotBlank(example.getSchName())) {
            criteria.andSchNameEqualTo(example.getSchName());
        }
        if (StringUtils.isNotBlank(example.getUserStatus())) {
            criteria.andUserStatusEqualTo(example.getUserStatus());
        }
        if (StringUtils.isNotBlank(example.getRealStatus())) {
            criteria.andRealStatusEqualTo(example.getRealStatus());
        }

        if (example.getCreatedDate() != null) {
            criteria.andCreatedDateEqualTo(example.getCreatedDate());
        }
        if (example.getCreatedDateStart() != null) {
            criteria.andCreatedDateGreaterThanOrEqualTo(example.getCreatedDateStart());
        }
        if (example.getCreatedDateEnd() != null) {
            criteria.andCreatedDateLessThanOrEqualTo(example.getCreatedDateStart());
        }
        if (StringUtils.isNotBlank(example.getOrderColumn())) {
            StringBuilder stringBuilder = new StringBuilder("order by ");
            stringBuilder.append(example.getOrderColumn());
            stringBuilder.append(" ");
            if (example.getAscFlag()) {
                stringBuilder.append("asc");
            } else {
                stringBuilder.append("desc");
            }

            userExample.setOrderByClause(stringBuilder.toString());
        }
        if (example.getFirst() != null) {
            userExample.setStart(example.getFirst());
            userExample.setEnd(example.getEnd());
        }
    }
}