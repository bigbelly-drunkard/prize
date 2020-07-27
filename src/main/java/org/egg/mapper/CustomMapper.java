package org.egg.mapper;

import java.io.Serializable;
import java.util.List;

/**
 * 通用IMapper<E,M, ID>
 * M:实体类
 * E:Example
 * ID:主键的变量类型
 *
 * @author orange1438
 *         github: https://github.com/orange1438
 *         date: 2017-11-30 23:00:00
 */
public interface CustomMapper<E,M, ID extends Serializable> {
    /** 
     *
     * @param example 条件对象
     */
    int queryCount(E example);

    /** 
     * 根据ID删除
     * @param id 主键ID
     */
    int deleteByPrimaryKey(ID id);

    /** 
     * 添加对象所有字段
     * @param record 插入字段对象(必须含ID）
     */
    int insert(M record);

    /** 
     * 添加对象对应字段
     * @param record 插入字段对象(必须含ID）
     */
    int insertSelective(M record);

    /** 
     * 添加List集合对象所有字段
     * @param record 批量插入字段对象(必须含ID）
     */
    int insertBatch(List<M> record);

    /** 
     * 添加List集合对象对应字段
     * @param record 批量插入字段对象(必须含ID）
     */
    int insertBatchSelective(List<M> record);

    /** 
     *
     * @param example 条件对象
     */
    List<M> queryList(E example);

    /** 
     * 根据ID查询
     * @param id 主键ID
     */
    M selectByPrimaryKey(ID id);

    /** 
     * 根据ID修改对应字段
     * @param record 修改字段对象(必须含ID）
     */
    int updateByPrimaryKeySelective(M record);

    /** 
     * 根据ID修改所有字段(必须含ID）
     * @param record 修改字段对象(必须含ID）
     */
    int updateByPrimaryKey(M record);


}