package de.codecentric.cxf.configuration;

import de.codecentric.cxf.logging.TimeLoggingFilter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class TimeLoggingConfiguration {

    @Autowired
    private BeanFactory beanFactory;

    @Bean
    @ConditionalOnProperty(name = "endpoint.autoinit", matchIfMissing = true)
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(timeLoggingFilter());
        filterRegistrationBean.setName("timeLoggingFilter");
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    private Filter timeLoggingFilter() {
        return new TimeLoggingFilter(beanFactory);
    }
}
