package org.egg.biz;

import org.egg.BaseTest;
import org.egg.model.VO.PrizeVo;
import org.egg.response.CommonSingleResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/19 17:57
 */
public class PrizeBizTest extends BaseTest {
    @Autowired
    private PrizeBiz prizeBiz;
    @Test
    public void p() throws Exception {
        while (true) {

        CommonSingleResult<PrizeVo> a = prizeBiz.p("A", "160963614995124224");
        if (!a.isSuccess()) {
            return;
        }
        }
    }

}