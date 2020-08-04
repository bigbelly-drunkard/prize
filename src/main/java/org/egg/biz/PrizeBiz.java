package org.egg.biz;

import lombok.extern.slf4j.Slf4j;
import org.egg.enums.CommonErrorEnum;
import org.egg.enums.UserStatusEnum;
import org.egg.exception.CommonException;
import org.egg.model.DO.Customer;
import org.egg.model.DTO.PrizeBean;
import org.egg.observer.subjects.CommonObserver;
import org.egg.response.CommonSingleResult;
import org.egg.service.impl.CustomerServiceImpl;
import org.egg.template.BizTemplate;
import org.egg.template.TemplateCallBack;
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
    @Autowired
    private BizTemplate bizTemplate;
    @javax.annotation.Resource(name = "getPrizeSuccObserver")
    private CommonObserver getPrizeSuccObserver;
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
            String need = properties.get(ss + "." + "need").toString();
            String[] split1 = need.split("|");
            String score = split[0];
            String gold = split[1];
            ArrayList<PrizeBean> prizeBeans = new ArrayList<>();
            properties.forEach((key, value) -> {

                if (key.toString().startsWith(ss + "." + "S.")) {
                    String s = value.toString();
                    PrizeBean prizeBean = parseValue(s);
                    String substring = key.toString().substring(key.toString().indexOf("S.") + 2);
                    prizeBean.setId(Long.valueOf(substring));
                    prizeBean.setNeedScore(new BigDecimal(score));
                    prizeBean.setNeedGold(new BigDecimal(gold));
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
    public CommonSingleResult<PrizeBean> p(String activeNo, String customerId) {
        CommonSingleResult<PrizeBean> result = new CommonSingleResult<>();
        bizTemplate.process(result, new TemplateCallBack() {
            @Override
            public void doCheck() {
                //         check 积分够不够
                Customer customer = customerService.queryCustomerByCustomerId(customerId);
                if (!UserStatusEnum.EFFECT.getCode().equals(customer.getCustomerStatus())) {
                    log.warn("用户状态不合法 customerId={}", customerId);
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);
                }
                BigDecimal score = customer.getScore();
                switch (activeNo) {
                    case "A":
                        if (score.compareTo(new BigDecimal("10")) != 1) {
                            log.error("A活动 用户积分不够");
                            throw new CommonException(CommonErrorEnum.SCORE_NOT_ENOUGH);
                        }
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void doAction() {
                String activeName = "";
                PrizeBean res = null;
                switch (activeNo) {
                    case "A":
                        activeName = "A";
                        break;
                    default:
                        break;
                }
                for (int i = 0; i < 3; i++) {
                    try {
                        PrizeBean prizeBean = commonLogic(activeName);
                        boolean b = customerService.checkLoadFactor(prizeBean.getFactor(), customerId, true);
                        if (b) {
                            res = prizeBean;
                            break;
                        }
                    } catch (IOException e) {
                        log.warn("抽奖报错 重试，e={}", e);
                        continue;
                    }
                }
                res = prizeDefaultCache.get(activeName);
//         通知者  1.扣积分 2.发奖品 反参id如果为null 降级为未中奖
                getPrizeSuccObserver.notifyObserver(res);
                if (res == null || res.getId() == null) {
                    log.info("发奖失败 降级默认为未中奖");
                    res = prizeDefaultCache.get(activeName);
                }
                result.setData(res);

            }
        });
        return result;
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
        prizeBean.setTypeCode(split[3]);
        return prizeBean;
    }

}
