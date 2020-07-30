package org.egg.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.egg.enums.PayStatusEnum;
import org.egg.enums.PayTypeEnum;
import org.egg.mapper.PayRecordMapper;
import org.egg.model.DO.PayRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/30 18:28
 */
@Component
@Slf4j
public class PayRecordServiceImpl {
    @Autowired
    private PayRecordMapper payRecordMapper;


    /**
     *
     * @param payRecord
     * @return
     */
    public void createPayNo(PayRecord payRecord,PayTypeEnum payTypeEnum) {
        payRecord.setPayStatus(PayStatusEnum.INIT.getCode());
        payRecord.setCreatedDate(new Date());
        payRecord.setPayType(payTypeEnum.getCode());
        payRecordMapper.insertSelective(payRecord);
    }

}
