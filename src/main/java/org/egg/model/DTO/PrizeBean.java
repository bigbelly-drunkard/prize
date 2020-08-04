package org.egg.model.DTO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/3 17:55
 */
@Data
public class PrizeBean {
    private Long id;
    private String name;
    /**
     * 价值因子
     */
    private BigDecimal factor;

    private BigDecimal rate;
    private String typeCode;
    private BigDecimal needScore;
    private BigDecimal needGold;
}
