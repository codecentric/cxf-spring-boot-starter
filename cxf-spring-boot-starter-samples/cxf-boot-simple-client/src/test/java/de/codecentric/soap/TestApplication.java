package de.codecentric.soap;

import de.codecentric.soap.configuration.CxfBootSimpleTestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    CxfBootSimpleApplication.class, 
    CxfBootSimpleTestConfiguration.class
})
public class TestApplication {

}
