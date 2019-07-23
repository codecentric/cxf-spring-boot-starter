package de.codecentric.soap;

import de.codecentric.cxf.xmlvalidation.CustomFaultBuilder;
import de.codecentric.soap.xmlvalidation.WeatherFaultBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class CxfBootSimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CxfBootSimpleApplication.class, args);
    }

    // Activating XML-Schema validation with custom Fault
    @Bean
    public CustomFaultBuilder weatherFaultBuilder() {
        return new WeatherFaultBuilder();
    }
}
