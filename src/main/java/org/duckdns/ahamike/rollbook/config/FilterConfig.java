package org.duckdns.ahamike.rollbook.config;

import org.duckdns.ahamike.rollbook.config.logging.setting.RequestCachingFilter;
import org.duckdns.ahamike.rollbook.config.logging.setting.RequestTimeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.Filter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> requestTimeFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new RequestTimeFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(10);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<Filter> requestCachingFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new RequestCachingFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(20);
        return bean;
    }
}
