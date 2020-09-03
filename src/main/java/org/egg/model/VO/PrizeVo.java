package org.egg.model.VO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/18 18:13
 */
@Data
public class PrizeVo {
    private Long id;
    private String name;
    private BigDecimal needScore;
    private BigDecimal needGold;
    private BigDecimal prizeNum;
    private BigDecimal nickName;
}
