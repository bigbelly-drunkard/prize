package org.egg.handler.Observer;

import lombok.extern.slf4j.Slf4j;
import org.egg.biz.LoadFactorBiz;
import org.egg.enums.PrizeTypeEnum;
import org.egg.model.DTO.PrizeBean;
import org.egg.observer.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author dataochen
 * @Description 收益计算
 * @date: 2020/8/4 18:28
 */
@Slf4j
@Component("loadFactorObserver")
public class LoadFactorObserver implements Observer {
    @Autowired
    private LoadFactorBiz loadFactorBiz;

    @Override
    public void update(Object obj) {
        PrizeBean obj1 = (PrizeBean) obj;

        if (null != obj1.getNeedScore() && BigDecimal.ZERO.compareTo(obj1.getNeedScore()) == -1) {
//            积分
            PrizeTypeEnum enumByCode = PrizeTypeEnum.getEnumByCode(obj1.getTypeCode());
            switch (enumByCode) {
                case RANDOM_GOLD:
                    loadFactorBiz.play4Score(obj1.getNeedScore(), obj1.getPrizeNum().divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP), obj1.getCid());
                    break;
                case RANDOM_RED_PACKAGE:
                    loadFactorBiz.play4Score(obj1.getNeedScore(), obj1.getPrizeNum(), obj1.getCid());
                    break;
                case RANDOM_SCORE:
                    loadFactorBiz.play4Score(obj1.getNeedScore(), obj1.getPrizeNum().divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP), obj1.getCid());
                    break;
                default:
                    loadFactorBiz.play4Score(obj1.getNeedScore(), obj1.getPrize(), obj1.getCid());
                    break;
            }
        }
        if (null != obj1.getNeedGold() && BigDecimal.ZERO.compareTo(obj1.getNeedGold()) == -1) {
//            金豆
            PrizeTypeEnum enumByCode = PrizeTypeEnum.getEnumByCode(obj1.getTypeCode());
            switch (enumByCode) {
                case RANDOM_GOLD:
                    loadFactorBiz.play4Gold(obj1.getNeedGold(), obj1.getPrizeNum().divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP), obj1.getCid());
                    break;
                case RANDOM_RED_PACKAGE:
                    loadFactorBiz.play4Gold(obj1.getNeedGold(), obj1.getPrizeNum(), obj1.getCid());
                    break;
                case RANDOM_SCORE:
                    loadFactorBiz.play4Gold(obj1.getNeedGold(), obj1.getPrizeNum().divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP), obj1.getCid());
                    break;
                default:
                    loadFactorBiz.play4Gold(obj1.getNeedGold(), obj1.getPrize(), obj1.getCid());
                    break;
            }
        }
    }

    @Override
    public void update() {

    }
}
