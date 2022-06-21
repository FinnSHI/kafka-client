package com.aiit.kafkaclient.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class GetBeanUtils implements ApplicationContextAware {
    protected static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        if (applicationContext == null) {
            applicationContext = arg0;
        }
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    // 这里用来放目标对象的实例
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}