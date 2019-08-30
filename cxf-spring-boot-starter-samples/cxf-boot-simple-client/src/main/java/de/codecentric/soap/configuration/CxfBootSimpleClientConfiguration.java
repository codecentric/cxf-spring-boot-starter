package de.codecentric.soap.configuration;

import de.codecentric.cxf.logging.soapmsg.SoapMessageLoggingInInterceptor;
import de.codecentric.cxf.logging.soapmsg.SoapMessageLoggingOutInterceptor;
import de.codecentric.namespace.weatherservice.WeatherService;
import org.apache.cxf.interceptor.AbstractLoggingInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:application.properties")
public class CxfBootSimpleClientConfiguration {

    @Value("${webservice.client.url}")
    private String clientUrl;
    
    // Mind the "static"
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    /*
     * CXF JaxWs Client
     */
    @Bean
    public WeatherService weatherServiceClient() {
        JaxWsProxyFactoryBean jaxWsFactory = new JaxWsProxyFactoryBean();
        jaxWsFactory.setServiceClass(WeatherService.class);
        jaxWsFactory.setAddress(clientUrl);
        jaxWsFactory.getInInterceptors().add(logInInterceptorClientSoapMsgLogger());
        jaxWsFactory.getOutInterceptors().add(logOutInterceptorClientSoapMsgLogger());
        return (WeatherService) jaxWsFactory.create();
    }

    @Bean
    public AbstractLoggingInterceptor logInInterceptorClientSoapMsgLogger() {
        SoapMessageLoggingInInterceptor logInInterceptor = new SoapMessageLoggingInInterceptor();
        logInInterceptor.logSoapMessage(true);
        logInInterceptor.setPrettyLogging(true);
        return logInInterceptor;
    }

    @Bean
    public AbstractLoggingInterceptor logOutInterceptorClientSoapMsgLogger() {
        SoapMessageLoggingOutInterceptor logOutInterceptor = new SoapMessageLoggingOutInterceptor();
        logOutInterceptor.logSoapMessage(true);
        logOutInterceptor.setPrettyLogging(true);
        return logOutInterceptor;
    }

}
