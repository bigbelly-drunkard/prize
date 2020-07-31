//package org.egg.configurer;
//
//import lombok.extern.slf4j.Slf4j;
//import org.egg.cache.LocalCache;
//import org.egg.service.impl.RedisServiceImpl;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Component;
//
///**
// * @author dataochen
// * @Description
// * @date: 2019/6/12 20:57
// */
//@Component
//@Slf4j
//public class InitBean implements ApplicationContextAware {
//    @Autowired
//    private LocalCache localCache;
//    @Autowired
//    private RedisServiceImpl redisService;
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        initData();
//    }
//
//    private void initData() {
//    }
//}
