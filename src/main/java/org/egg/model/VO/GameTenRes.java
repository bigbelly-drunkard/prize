package org.egg.model.VO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/6 10:30
 */
@Data
public class GameTenRes {
    private BigDecimal prize;
    private boolean hitFlag;
    private String pid;
}
