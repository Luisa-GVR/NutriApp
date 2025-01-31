package com.prueba.demo.principal;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationContext {
    private static ApplicationContext context;

    public SpringApplicationContext(ApplicationContext context) {
        SpringApplicationContext.context = context;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
}
