package org.egg.configurer;

import org.egg.model.DTO.EmailDto;
import org.egg.utils.EmailUtil;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.servlet.MultipartConfigElement;

/**
 * @author cdt
 * @Description
 * @date: 2017/11/14 11:39
 */
@Configuration
public class Myconfigurer {
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
            @Override
            public void customize(ConfigurableWebServerFactory factory) {
                factory.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error/404"));
                factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
                factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
                factory.addErrorPages(new ErrorPage(java.lang.Throwable.class, "/error/500"));
            }
        };
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 设置文件大小限制 ,超出设置页面会抛出异常信息，
        // 这样在文件上传的地方就需要进行异常信息的处理了;
//        factory.setMaxFileSize("256KB"); // KB,MB
        /// 设置总上传数据总大小
//        factory.setMaxRequestSize("512KB");
        // Sets the directory location where files will be stored.
//         factory.setLocation("/export/renting/upload");
        return factory.createMultipartConfig();
    }

    //    @Bean
//    public HttpMessageConverters fastJsonHttpMessageConverters() {
//        // 1、需要先定义一个converter 转换器
//        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//        // 2、添加fastJson 的配置信息，比如：是否要格式化返回的json数据
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteDateUseDateFormat);
//        // 3、在convert 中添加配置信息
//        fastConverter.setFastJsonConfig(fastJsonConfig);
//        // 4、将convert 添加到converters当中
//        HttpMessageConverter<?> converter = fastConverter;
//        return new HttpMessageConverters(converter);
//    }
    @Bean
    public ThreadPoolTaskExecutor getTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(2);
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setQueueCapacity(2000);
        threadPoolTaskExecutor.setRejectedExecutionHandler((r, executor) -> {
            EmailUtil.sendEmailFormQQ(new EmailDto("threadPoolTaskExecutor QueueCapacity >2000", "线程池对了超限了，赶紧处理吧", EmailUtil.TOEMAILACC));
        });
        threadPoolTaskExecutor.setKeepAliveSeconds(3000);
        return threadPoolTaskExecutor;
    }



}
