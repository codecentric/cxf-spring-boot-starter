package de.codecentric.cxf.configuration;

import de.codecentric.cxf.TestServiceSystemTestConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableAutoConfiguration
@Import({
        TestServiceSystemTestConfiguration.class
})
@PropertySource("classpath:application-endpoint-autoinit-false.properties")
public class JaxWsClientTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(JaxWsClientTestApplication.class, args);
    }
}
