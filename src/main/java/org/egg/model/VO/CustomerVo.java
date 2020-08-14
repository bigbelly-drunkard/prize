package org.egg.model.VO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/14 16:07
 */
@Data
public class CustomerVo {

    private BigDecimal score;

    private BigDecimal gold;


    private String nickName;

    private String headUrl;


    private String customerType;

    private Date memberExpire;

}
