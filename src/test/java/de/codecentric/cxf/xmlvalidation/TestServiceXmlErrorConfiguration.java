package de.codecentric.cxf.xmlvalidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.codecentric.cxf.TestConfiguration;
import de.codecentric.cxf.common.BootStarterCxfException;
import de.codecentric.cxf.configuration.CxfAutoConfiguration;
import de.codecentric.cxf.soaprawclient.SoapRawClient;
import de.codecentric.namespace.weatherservice.WeatherService;

@Configuration
@Import(TestConfiguration.class)
public class TestServiceXmlErrorConfiguration {

    @Bean
    public SoapRawClient soapRawClient() throws BootStarterCxfException {
        System.out.println(buildUrl());
        return new SoapRawClient(buildUrl(), WeatherService.class);
    }
    
    private String buildUrl() {
        // return something like http://localhost:8084/soap-api/WeatherSoapService
        return "http://localhost:8087" + cxfAutoConfiguration.getBaseUrl() + TestConfiguration.PUBLISH_URL_ENDING;
    }
    
    @Autowired
    private CxfAutoConfiguration cxfAutoConfiguration;
}
