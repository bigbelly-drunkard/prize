package org.egg.mapper;

import org.apache.ibatis.annotations.Param;
import org.egg.model.DO.FlowRecord;
import org.egg.model.DO.FlowRecordExample;

import java.util.List;

public interface FlowRecordMapper {
    int countByExample(FlowRecordExample example);

    int deleteByExample(FlowRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FlowRecord record);

    int insertSelective(FlowRecord record);

    List<FlowRecord> selectByExample(FlowRecordExample example);

    FlowRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FlowRecord record, @Param("example") FlowRecordExample example);

    int updateByExample(@Param("record") FlowRecord record, @Param("example") FlowRecordExample example);

    int updateByPrimaryKeySelective(FlowRecord record);

    int updateByPrimaryKey(FlowRecord record);
}