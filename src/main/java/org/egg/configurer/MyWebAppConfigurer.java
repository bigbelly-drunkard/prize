package org.egg.configurer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egg.Interceptor.MiniLoginInterceptor;
import org.egg.cache.LocalCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author cdt
 * @Description springboot 配置
 * @date: 2017/11/7 16:29
 */
@Configuration
public class MyWebAppConfigurer
        extends WebMvcConfigurationSupport {

    @Autowired
    private LocalCache localCache;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        // 多个拦截器组成一个拦截器链
//        // addPathPatterns 用于添加拦截规则
//        // excludePathPatterns 用户排除拦截
//        registry.addInterceptor(new AccessOriginInterceptor()).addPathPatterns("/**").excludePathPatterns("/css/**")
//                .excludePathPatterns("/easyui/**").excludePathPatterns("/fonts/**").excludePathPatterns("/images/**")
//                .excludePathPatterns("/js/**");
//        registry.addInterceptor(new AppInterceptor()).addPathPatterns("/**").excludePathPatterns("/css/**")
//                .excludePathPatterns("/easyui/**").excludePathPatterns("/fonts/**").excludePathPatterns("/images/**")
//                .excludePathPatterns("/js/**");
        //登录页，注册页，400，500页,引导页；登录接口，注册接口，检查登录名唯一接口，渠道回执接口
//        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/user/login")
//                .excludePathPatterns("/user/register").excludePathPatterns("/error/**").excludePathPatterns("/logOut")
//                .excludePathPatterns("/user/checkLoginName").excludePathPatterns("/publishOrder/updateStatusByPaySucc")
//                .excludePathPatterns("/showXY").excludePathPatterns("/showPOList")
//                .excludePathPatterns("/publishOrder/queryList").excludePathPatterns("/boss/**")
//                .excludePathPatterns("/user/wxLogin").excludePathPatterns("/showSpecification")
//                .excludePathPatterns("/toLogin/**").excludePathPatterns("/toRegister").excludePathPatterns("/").excludePathPatterns("/wx/**")
//                .excludePathPatterns("/showShareCommend").excludePathPatterns("/pc/**").
//                excludePathPatterns("/showAgentRecommend").excludePathPatterns("/feedBack/**").excludePathPatterns("/css/**")
//                .excludePathPatterns("/easyui/**").excludePathPatterns("/fonts/**").excludePathPatterns("/images/**")
//                .excludePathPatterns("/js/**");
        registry.addInterceptor(new MiniLoginInterceptor(localCache))
                .addPathPatterns("/**")
        ;
//        registry.addInterceptor(new BossLoginInterceptor()).addPathPatterns("/boss/**").excludePathPatterns("/boss/login")
//                .excludePathPatterns("/boss/toBossLogin").excludePathPatterns("/css/**")
//                .excludePathPatterns("/easyui/**").excludePathPatterns("/fonts/**").excludePathPatterns("/images/**")
//                .excludePathPatterns("/js/**");
    }
    //定义时间格式转换器
    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        converter.setObjectMapper(mapper);
        return converter;
    }

    //添加转换器
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //将我们定义的时间格式转换器添加到转换器列表中,
        //这样jackson格式化时候但凡遇到Date类型就会转换成我们定义的格式
        converters.add(jackson2HttpMessageConverter());
    }



}