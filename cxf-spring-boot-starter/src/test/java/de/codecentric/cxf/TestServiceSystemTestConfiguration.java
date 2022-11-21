package de.codecentric.cxf;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.codecentric.cxf.common.BootStarterCxfException;
import de.codecentric.cxf.configuration.CxfAutoConfiguration;
import de.codecentric.cxf.soaprawclient.SoapRawClient;
import de.codecentric.namespace.weatherservice.WeatherService;

@Configuration
public class TestServiceSystemTestConfiguration {

    /*
     * CXF JaxWs Client
     */
    @Bean
    public WeatherService weatherServiceClient() throws BootStarterCxfException {
        JaxWsProxyFactoryBean jaxWsFactory = new JaxWsProxyFactoryBean();
        jaxWsFactory.setServiceClass(WeatherService.class);
        jaxWsFactory.setAddress(buildUrl());
        return (WeatherService) jaxWsFactory.create();
    }

    @Bean
    public SoapRawClient soapRawClient() throws BootStarterCxfException {
        System.out.println(buildUrl());
        return new SoapRawClient(buildUrl(), WeatherService.class);
    }
    
    private String buildUrl() throws BootStarterCxfException {
        // return something like http://localhost:8084/soap-api/WeatherSoapService
        return "http://localhost:8087" + cxfAutoConfiguration.baseAndServiceEndingUrl();
    }
    
    @Autowired
    private CxfAutoConfiguration cxfAutoConfiguration;
}
