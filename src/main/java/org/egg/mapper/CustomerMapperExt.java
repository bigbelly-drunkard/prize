package org.egg.mapper;

import java.util.Map;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/28 20:49
 */
public interface CustomerMapperExt {
    public int updateScoreOrGoldByCustomer(Map<String, Object> param);
    public void updateLoadFactor(Map<String, Object> param);
}
