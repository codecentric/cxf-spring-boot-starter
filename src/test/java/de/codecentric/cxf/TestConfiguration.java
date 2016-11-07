package de.codecentric.cxf;

import javax.xml.ws.Endpoint;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import de.codecentric.cxf.xmlvalidation.CustomFaultBuilder;
import de.codecentric.cxf.xmlvalidation.WeatherFaultBuilder;
import de.codecentric.namespace.weatherservice.Weather;
import de.codecentric.namespace.weatherservice.WeatherService;

@Configuration
public class TestConfiguration {

    // Activating XML-Schema validation with custom Fault
    @Bean
    public CustomFaultBuilder weatherFaultBuilder() {
        return new WeatherFaultBuilder();
    }

}
