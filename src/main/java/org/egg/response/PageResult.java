package org.egg.response;

import java.io.Serializable;

/**
 * @author cdt
 * @Description
 * @date: 2017/11/14 20:17
 */
public class PageResult extends CommonListResult implements Serializable{
    private static final long serialVersionUID = -3736326803627945710L;
    private Integer total;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
