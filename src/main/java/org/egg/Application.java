package org.egg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author cdt
 * @Description 也可用@MapperScan("org.egg.mapper")+@Repository 来代替@mapper注解
 * @ Date: 2017/11/6 20:56
 */
@SpringBootApplication
@EnableScheduling
@ImportResource(locations = {"classpath:spring/spring-base.xml"})
@MapperScan(value = "org.egg.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
