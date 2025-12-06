package org.duckdns.ahamike.rollbook.config.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
        env = context.getEnvironment();
    }

    private static ApplicationContext context;

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }

    private static Environment env;

    public static String getEnvProperty(String key) {
        return env.getProperty(key);
    }

    public static String getRequestTimeName() {
        return "requestTime";
    }

    public static String getRequestParamInfoName() {
        return "requestParamInfo";
    }
}
