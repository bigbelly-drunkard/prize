package org.egg.biz;

import lombok.extern.slf4j.Slf4j;
import org.egg.model.DTO.PrizeBean;
import org.egg.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/3 10:42
 */
@Slf4j
@Component
public class PrizeBiz {
    @Autowired
    private CustomerServiceImpl customerService;
    private static final String PRIZE_PATH = "/file/prize.properties";
    private Map<String, List<PrizeBean>> prizeListCache = new HashMap<>(4);
    private Map<String, PrizeBean> prizeDefaultCache = new HashMap<>(2);

    @PostConstruct
    private void init() throws IOException {
        Resource resource = new ClassPathResource(PRIZE_PATH);
        Properties properties = PropertiesLoaderUtils.loadProperties(resource);
        String activeNames = properties.get("active.name").toString();
        String[] split = activeNames.split(";");
        for (String ss : split) {
            String aDefault = properties.get(ss + "." + "default").toString();
            ArrayList<PrizeBean> prizeBeans = new ArrayList<>();
            properties.forEach((key, value) -> {

                if (key.toString().startsWith(ss + "." + "S.")) {
                    String s = value.toString();
                    PrizeBean prizeBean = parseValue(s);
                    String substring = key.toString().substring(key.toString().indexOf("S.") + 2);
                    prizeBean.setId(Long.valueOf(substring));
                    prizeBeans.add(prizeBean);
                    if (value.equals(ss + "." + "S." + aDefault)) {
                        prizeDefaultCache.put(ss, prizeBean);
                    }
                }
            });
            prizeBeans.sort(Comparator.comparing(PrizeBean::getId));
            prizeListCache.put(ss, prizeBeans);
        }
    }

    /**
     * 抽奖
     * 1.抽奖
     * 2.check因子 如果满足返回 否则重试，重试3次 如果3次后还不通过check 降级为默认未中奖
     *
     * @param activeNo
     */
    public PrizeBean p(String activeNo, String customerId) {
        String activeName = "";
        switch (activeNo) {
            case "A":
                activeName = "A";
                break;
            case "ab":
                activeName = "xx";
                break;
            default:
                break;
        }
        for (int i = 0; i < 3; i++) {
            try {
                PrizeBean prizeBean = commonLogic(activeName);
                boolean b = customerService.checkLoadFactor(prizeBean.getFactor(), customerId, true);
                if (b) {
                    return prizeBean;
                }
            } catch (IOException e) {
                log.warn("抽奖报错 重试，e={}", e);
                continue;
            }
        }
        return prizeDefaultCache.get(activeName);
//        todo 发奖

    }

    /**
     * 加权抽奖
     * 1.vip加权
     * 2.老用户 （入金-出金）/入金 比例加权
     */
    public void pWeight() {

    }

    //================================ private ==============================

    /**
     * 公共抽奖逻辑
     *
     * @param activeName
     * @return
     * @throws IOException
     */
    private PrizeBean commonLogic(String activeName) throws IOException {
        log.info("抽奖开始，activeName={}", activeName);
        List<PrizeBean> prizeBeans = prizeListCache.get(activeName);
        double random = Math.random();
        BigDecimal bigDecimal = new BigDecimal(random + "");
        for (PrizeBean prizeBean : prizeBeans) {
            if (prizeBean.getRate().compareTo(bigDecimal) == 1) {
                return prizeBean;
            }
            bigDecimal = bigDecimal.subtract(prizeBean.getRate());
        }
//        默认
        return prizeDefaultCache.get(activeName);
    }

    private PrizeBean parseValue(String value) {
        PrizeBean prizeBean = new PrizeBean();
        String[] split = value.split("#");
        prizeBean.setName(split[0]);
        prizeBean.setFactor(new BigDecimal(split[1]));
        prizeBean.setRate(new BigDecimal(split[2]));
        prizeBean.setRandomFlag(Boolean.valueOf(split[3]));
        return prizeBean;
    }

}
