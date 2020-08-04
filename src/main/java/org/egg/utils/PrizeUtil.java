package org.egg.utils;

import com.alibaba.fastjson.JSONObject;
import org.egg.model.DTO.PrizeBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Properties;

/**
 * @author dataochen
 * @Description *平均中奖算法  每种奖品总价格中奖概率一样**
 * 假设奖品种类数为n,奖品价值为m1,m2....mn,每种奖品中奖概率总价值一样的情况下
 * 则奖品中奖概率为：1/n*m1,1/n*m2....1/n*mn
 * 如果要保证利益率在k%，则中奖概率为（1-k%）/n*m1,（1-k%）/n*m2...（1-k%）/n*mn
 * @date: 2020/8/3 17:30
 */
public class PrizeUtil {
    private static final String PRIZE_PATH = "/file/prize.properties";

    /**
     * #奖品库
     * 名称#价值（单位：因子）#概率#随机开关
     * A.S.1=谢谢惠顾#0#40%#true
     * <p>
     * <p>
     * 计算平均概率
     */
    public void calcNum(String activeName) throws IOException {
        ArrayList<PrizeBean> prizeBeans = new ArrayList<>();
        Resource resource = new ClassPathResource(PRIZE_PATH);
        Properties properties = PropertiesLoaderUtils.loadProperties(resource);
        properties.forEach((key, value) -> {
            if (key.toString().startsWith(activeName + "." + "S.")) {
                String s = value.toString();
                PrizeBean prizeBean = parseValue(s);
                String substring = key.toString().substring(key.toString().indexOf( "S.")+2);
                prizeBean.setId(Long.valueOf(substring));
                if (BigDecimal.ZERO.compareTo(prizeBean.getFactor()) == -1) {
                    prizeBeans.add(prizeBean);
                }
            }
        });
        prizeBeans.sort(Comparator.comparing(PrizeBean::getId));
        Object o = properties.get(activeName + ".pro");
//        pro:利益率
        BigDecimal pro = new BigDecimal(o.toString()).divide(new BigDecimal(100));
        BigDecimal subtract = new BigDecimal("1").subtract(pro);
        int n = prizeBeans.size();
        prizeBeans.stream().forEach(x -> {
            BigDecimal multiply = x.getFactor().multiply(new BigDecimal(n)).setScale(10);
//            x.setRate(new BigDecimal("1").divide(multiply, 10, 4));
            x.setRate(subtract.divide(multiply, 10, 4));
        });
        System.out.println("概率平均分配" + JSONObject.toJSONString(prizeBeans));
        BigDecimal bigDecimal = prizeBeans.stream().map(x -> x.getRate()).reduce(BigDecimal::add).get();
        System.out.println("总中奖概率：" + bigDecimal);
        BigDecimal subtract1 = new BigDecimal("1").subtract(bigDecimal);
        System.out.println("谢谢惠顾中奖概率：" + subtract1);
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

    /**
     * 用于计算活动的合理概率值
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        PrizeUtil prizeUtil = new PrizeUtil();
        prizeUtil.calcNum("A");
    }
}
