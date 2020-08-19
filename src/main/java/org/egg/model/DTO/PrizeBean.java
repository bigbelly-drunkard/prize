package org.egg.model.DTO;

import lombok.Data;
import org.egg.enums.PrizeTypeEnum;

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
    private BigDecimal prize;

    private BigDecimal rate;
    /**
     * @see PrizeTypeEnum
     */
    private String typeCode;
    private BigDecimal needScore;
    private BigDecimal needGold;
    private BigDecimal prizeNum;
    private String cid;
}
