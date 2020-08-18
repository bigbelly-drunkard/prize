package org.egg.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.egg.enums.CommonErrorEnum;
import org.egg.enums.FlowRecordTypeEnum;
import org.egg.exception.CommonException;
import org.egg.mapper.CustomerMapperExt;
import org.egg.mapper.FlowRecordMapper;
import org.egg.model.DO.FlowRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/28 20:32
 */
@Component
@Slf4j
public class FlowRecordServiceImpl {
    @Autowired
    private FlowRecordMapper flowRecordMapper;
    @Autowired
    private CustomerMapperExt customerMapperExt;

    /**
     * 改变积分或金豆值
     *
     * @param customerId
     * @param flowRecordTypeEnum
     * @param value              可能为正 可能为负数
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeScoreOrGold(String customerId, FlowRecordTypeEnum flowRecordTypeEnum, BigDecimal value,String reason) {
        FlowRecord flowRecord = new FlowRecord();
        flowRecord.setCustomerNo(customerId);
        flowRecord.setCreatedDate(new Date());
        flowRecord.setRecordReason(reason);
        flowRecord.setRecordType(flowRecordTypeEnum.getCode());
        flowRecord.setValue(value);
        flowRecordMapper.insertSelective(flowRecord);
//             更改积分或金豆总值
        HashMap<String, Object> stringObjectHashMap = new HashMap<>(3);
        switch (flowRecordTypeEnum) {
            case GOLD:
                stringObjectHashMap.put("addGold", value);
                stringObjectHashMap.put("addScore", 0);
                break;
            case SCORE:
                stringObjectHashMap.put("addGold", 0);
                stringObjectHashMap.put("addScore", value);
                break;
            default:
                break;
        }
        stringObjectHashMap.put("customerId", customerId);
        int i = customerMapperExt.updateScoreOrGoldByCustomer(stringObjectHashMap);
        if (i <= 0) {
            log.error("changeScoreOrGold更新数据库失败 回滚");
            throw new CommonException(CommonErrorEnum.DB_UPDATE_VERSION_EXCEPTION);
        }
    }


}
