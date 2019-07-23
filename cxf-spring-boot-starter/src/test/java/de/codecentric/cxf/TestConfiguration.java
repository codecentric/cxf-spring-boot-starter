package de.codecentric.cxf;

import de.codecentric.cxf.xmlvalidation.CustomFaultBuilder;
import de.codecentric.cxf.xmlvalidation.WeatherFaultBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    // Activating XML-Schema validation with custom Fault
    @Bean
    public CustomFaultBuilder weatherFaultBuilder() {
        return new WeatherFaultBuilder();
    }

}
