package org.duckdns.ahamike.rollbook.config;

import org.duckdns.ahamike.rollbook.config.logging.RequestCachingFilter;
import org.duckdns.ahamike.rollbook.config.logging.RequestTimeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<RequestTimeFilter> requestTimeFilter() {
        FilterRegistrationBean<RequestTimeFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new RequestTimeFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(1);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<RequestCachingFilter> requestCachingFilter() {
        FilterRegistrationBean<RequestCachingFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new RequestCachingFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(2);
        return bean;
    }
}
