package org.egg.biz;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.egg.enums.CommonErrorEnum;
import org.egg.enums.PrizeTypeEnum;
import org.egg.enums.TableTypeEnum;
import org.egg.enums.UserStatusEnum;
import org.egg.exception.CommonException;
import org.egg.handler.Observer.PrizeSendObserver;
import org.egg.model.DO.Customer;
import org.egg.model.DTO.PrizeBean;
import org.egg.model.VO.GameTenRes;
import org.egg.model.VO.PrizeVo;
import org.egg.observer.subjects.CommonObserver;
import org.egg.response.BaseResult;
import org.egg.response.CommonSingleResult;
import org.egg.service.impl.CustomerServiceImpl;
import org.egg.service.impl.RedisServiceImpl;
import org.egg.template.BizTemplate;
import org.egg.template.TemplateCallBack;
import org.egg.utils.BeanUtil;
import org.egg.utils.IdMarkUtil;
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
    @Autowired
    private RedisServiceImpl redisService;
    @Autowired
    private PrizeSendObserver prizeSendObserver;

    private static final String PRIZE_PATH = "/file/prize.properties";
    private Map<String, PrizeBean> gameTenCache = new HashMap<>(8);

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
            String[] split1 = need.split("\\|");
            String score = split1[0];
            String gold = split1[1];
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
                    if (key.equals(ss + "." + "S." + aDefault)) {
                        prizeDefaultCache.put(ss, prizeBean);
                    }
                }
            });
            prizeBeans.sort(Comparator.comparing(PrizeBean::getId));
            prizeListCache.put(ss, prizeBeans);
        }
//        gameTenCache
    }

    private void initGameTen() {
        PrizeBean prizeBean1 = new PrizeBean();
        prizeBean1.setTypeCode(PrizeTypeEnum.RED_PACKAGE.getCode());
        prizeBean1.setFactor(new BigDecimal("5"));
        prizeBean1.setRate(new BigDecimal("0.4"));
        prizeBean1.setName("5元现金红包");
        gameTenCache.put("1", prizeBean1);
        PrizeBean prizeBean2 = new PrizeBean();
        prizeBean2.setTypeCode(PrizeTypeEnum.GOLD.getCode());
        prizeBean2.setFactor(new BigDecimal("5"));
        prizeBean2.setRate(new BigDecimal("0.4"));
        prizeBean2.setName("50金豆");
        gameTenCache.put("2", prizeBean2);
        PrizeBean prizeBean3 = new PrizeBean();
        prizeBean3.setTypeCode(PrizeTypeEnum.RED_PACKAGE.getCode());
        prizeBean3.setFactor(new BigDecimal("9.8"));
        prizeBean3.setRate(new BigDecimal("0.2"));
        prizeBean3.setName("9.8元现金红包");
        gameTenCache.put("3", prizeBean3);
        PrizeBean prizeBean4 = new PrizeBean();
        prizeBean4.setTypeCode(PrizeTypeEnum.SCORE.getCode());
        prizeBean4.setFactor(new BigDecimal("6.6"));
        prizeBean4.setRate(new BigDecimal("0.4"));
        prizeBean4.setName("66积分礼包");
        gameTenCache.put("4", prizeBean4);
        PrizeBean prizeBean5 = new PrizeBean();
        prizeBean5.setTypeCode(PrizeTypeEnum.RANDOM_RED_PACKAGE.getCode());
        prizeBean5.setFactor(new BigDecimal("8.8"));
        prizeBean5.setRate(new BigDecimal("0.2"));
        prizeBean5.setName("最高8.8元随机现金红包");
        gameTenCache.put("5", prizeBean5);
        PrizeBean prizeBean6 = new PrizeBean();
        prizeBean6.setTypeCode(PrizeTypeEnum.RED_PACKAGE.getCode());
        prizeBean6.setFactor(new BigDecimal("2"));
        prizeBean6.setRate(new BigDecimal("1"));
        prizeBean6.setName("2元现金红包");
        gameTenCache.put("6", prizeBean6);
        PrizeBean prizeBean7 = new PrizeBean();
        prizeBean7.setTypeCode(PrizeTypeEnum.RANDOM_SCORE.getCode());
        prizeBean7.setFactor(new BigDecimal("10"));
        prizeBean7.setRate(new BigDecimal("0.2"));
        prizeBean7.setName("最高100随机积分礼包");
        gameTenCache.put("7", prizeBean7);

    }

    /**
     * 抽奖
     * 1.抽奖
     * 2.check因子 如果满足返回 否则重试，重试3次 如果3次后还不通过check 降级为默认未中奖
     *
     * @param activeNo
     */
    public CommonSingleResult<PrizeVo> p(String activeNo, String customerId) {
        log.info("抽奖start,{},{}", activeNo, customerId);
        CommonSingleResult<PrizeVo> result = new CommonSingleResult<>();
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
                        if (score.compareTo(new BigDecimal("10")) == -1) {
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
                PrizeBean res = new PrizeBean();
                switch (activeNo) {
                    case "A":
                        activeName = "A";
                        break;
                    default:
                        break;
                }
                for (int i = 0; i < 1; i++) {
                    try {
                        PrizeBean prizeBean = commonLogic(activeName);
                        if (prizeBean.getPrize().compareTo(BigDecimal.ZERO) != 1) {
                            break;
                        }
//                        每次需要新对象 不能对原对象进行修改
                        boolean b = customerService.checkLoadFactor(prizeBean.getPrize(), customerId, true);
                        if (b) {
//                            res = prizeBean;
                            BeanUtil.copyProperties(prizeBean, res);
                            break;
                        }
                    } catch (IOException e) {
                        log.warn("抽奖报错 重试，e={}", e);
                        continue;
                    }
                }
                if (res.getName() == null) {
                    PrizeBean prizeBean = prizeDefaultCache.get(activeName);
                    BeanUtil.copyProperties(prizeBean, res);
                }
//         通知者  1.扣积分 2.发奖品 反参id如果为null 降级为未中奖
                res.setCid(customerId);
                getPrizeSuccObserver.notifyObserver(res);
                if (res == null || res.getId() == null) {
                    log.info("发奖失败 降级默认为未中奖");
                    res = prizeDefaultCache.get(activeName);
                }
                PrizeVo prizeVo = new PrizeVo();
                BeanUtil.copyProperties(res, prizeVo);
                result.setData(prizeVo);

            }
        });
        log.info("抽奖结果 {}", JSONObject.toJSONString(result));
        return result;
    }

    /**
     * 加权抽奖
     * 1.vip加权
     * 2.老用户 （入金-出金）/入金 比例加权
     */
    public void pWeight() {

    }

    /**
     * 是否可以命中
     * 一次10积分
     *
     * @param customerId
     * @return
     */
    public CommonSingleResult<GameTenRes> game4TenHit(String customerId) {
        CommonSingleResult<GameTenRes> result = new CommonSingleResult<>();
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
                if (score.compareTo(new BigDecimal("10")) == -1) {
                    log.error(" 用户积分不够");
                    throw new CommonException(CommonErrorEnum.SCORE_NOT_ENOUGH);
                }
            }

            @Override
            public void doAction() {
                GameTenRes gameTenRes = new GameTenRes();
//                1.中奖几率 1/3
                double random = Math.random();
                if (random < 2 / 3) {
                    log.info("此处不可中奖");
                    gameTenRes.setHitFlag(false);
                    result.setData(gameTenRes);
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                int firstDayOfWeek = calendar.getFirstDayOfWeek();
                PrizeBean prizeBean = gameTenCache.get(firstDayOfWeek + 1 + "");
                String uuid = IdMarkUtil.getUuid(TableTypeEnum.OTHER);
                redisService.setPid(customerId, uuid, prizeBean);
                gameTenRes.setHitFlag(true);
                gameTenRes.setPid(uuid);
                gameTenRes.setName(prizeBean.getName());
                result.setData(gameTenRes);
                log.info("此次中奖了，gameTenRes={}", JSONObject.toJSONString(gameTenRes));

            }
        });
        return result;
    }

    /**
     * 领取hit
     *
     * @param cid
     * @param pid
     * @return
     */
    public BaseResult confirmHit(String cid, String pid) {
        log.info("领取hit cid={},pid={}", cid, pid);
        BaseResult result = new BaseResult();
        bizTemplate.process(result, new TemplateCallBack() {
            @Override
            public void doCheck() {
                Customer customer = customerService.queryCustomerByCustomerId(cid);
                if (!UserStatusEnum.EFFECT.getCode().equals(customer.getCustomerStatus())) {
                    log.warn("用户状态不合法 customerId={}", cid);
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);
                }

            }

            @Override
            public void doAction() {
                PrizeBean prizeBean = redisService.checkPid(cid, pid);
                if (null == prizeBean) {
                    log.warn("没有pid或pid已过期");
                    throw new CommonException(CommonErrorEnum.PARAM_ERROR);
                }
                prizeSendObserver.update(prizeBean);
            }
        });
        log.info("领取hit result={}", JSONObject.toJSONString(result));
        return result;
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
                log.info("抽到奖品待确定是否可命中{}", prizeBean.getName());
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
        prizeBean.setPrize(new BigDecimal(split[4]));
        return prizeBean;
    }

}
