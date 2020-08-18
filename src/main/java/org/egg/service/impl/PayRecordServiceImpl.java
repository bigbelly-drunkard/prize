package org.egg.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.egg.enums.PayStatusEnum;
import org.egg.enums.PayTypeEnum;
import org.egg.mapper.PayRecordMapper;
import org.egg.model.DO.PayRecord;
import org.egg.model.DO.PayRecordExample;
import org.egg.observer.subjects.CommonObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/30 18:28
 */
@Component(value = "payRecordService")
@Slf4j
public class PayRecordServiceImpl {
    @Autowired
    private PayRecordMapper payRecordMapper;
    @Resource(name = "payRecordStatusUpdateObserver")
    private CommonObserver payRecordStatusUpdateObserver;


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

    public PayRecord queryDetail(String payNo) {
        PayRecordExample payRecordExample = new PayRecordExample();
        PayRecordExample.Criteria criteria = payRecordExample.createCriteria();
        criteria.andPayNoEqualTo(payNo);
        List<PayRecord> payRecords = payRecordMapper.selectByExample(payRecordExample);
        return payRecords.get(0);
    }

    public void updateStatus(PayRecord payRecord, String originStatus) {
        PayRecordExample payRecordExample = new PayRecordExample();
        PayRecordExample.Criteria criteria = payRecordExample.createCriteria();
        criteria.andPayStatusEqualTo(originStatus);
        int i = payRecordMapper.updateByPrimaryKeySelective(payRecord);
        if (i > 0) {
            log.info("更新支付单状态成功，originStatus={},payRecord={}", originStatus, JSONObject.toJSONString(payRecord));
//             支付单状态变更通知
            payRecordStatusUpdateObserver.notifyObserver(payRecord);
        }
    }

}
