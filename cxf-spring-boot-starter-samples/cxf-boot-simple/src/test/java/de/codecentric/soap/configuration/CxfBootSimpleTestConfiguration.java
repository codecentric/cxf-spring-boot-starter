package de.codecentric.soap.configuration;

import de.codecentric.cxf.logging.soapmsg.SoapMessageLoggingInInterceptor;
import de.codecentric.cxf.logging.soapmsg.SoapMessageLoggingOutInterceptor;
import org.apache.cxf.interceptor.AbstractLoggingInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import de.codecentric.cxf.common.BootStarterCxfException;
import de.codecentric.cxf.configuration.CxfAutoConfiguration;
import de.codecentric.cxf.soaprawclient.SoapRawClient;
import de.codecentric.namespace.weatherservice.WeatherService;

@Configuration
@PropertySource("classpath:dev-test.properties")
public class CxfBootSimpleTestConfiguration {

    @Value("${webservice.client.port}")
    private String clientPort;
    
    @Value("${webservice.client.host}")
    private String clientHost;
    
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
        jaxWsFactory.setAddress(buildUrl());
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
    
    /*
     * SoapRawClient for XmlErrorTests
     */    
    @Bean
    public SoapRawClient soapRawClient() throws BootStarterCxfException {
        System.out.println(buildUrl());
        return new SoapRawClient(buildUrl(), WeatherService.class);
    }
    
    public String buildUrl() {
        // return something like http://localhost:8084/soap-api/WeatherSoapService
        return "http://" + clientHost + ":" + clientPort + cxfAutoConfiguration.baseAndServiceEndingUrl();
    }
    
    @Autowired
    private CxfAutoConfiguration cxfAutoConfiguration;
}
