package me.lyriclaw.gallery.config;

import me.lyriclaw.gallery.filter.AuthRequiredFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AuthRequiredFilter> authFilterBean(@Autowired AuthRequiredFilter filter){
        FilterRegistrationBean<AuthRequiredFilter> registrationBean
                = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/api/private/*");
        return registrationBean;
    }

}
