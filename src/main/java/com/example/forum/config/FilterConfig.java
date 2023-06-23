package com.example.forum.config;

import com.example.forum.filter.ViewFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final ViewFilter viewFilter;

    @Bean
    public FilterRegistrationBean<ViewFilter> loggingFilter() {
        FilterRegistrationBean<ViewFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(viewFilter);
        registrationBean.addUrlPatterns("/posts/*");
        return registrationBean;
    }
}
