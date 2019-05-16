package de.codecentric.cxf.configuration;

import de.codecentric.cxf.logging.soapmsg.SoapMessageLoggingInInterceptor;
import de.codecentric.cxf.logging.soapmsg.SoapMessageLoggingOutInterceptor;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.interceptor.AbstractLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Logging of SoapMessages to e.g. Console. To activate, set property soap.messages.logging=true.
 *
 * Extraction of SoapMessages, so they can be further processed, e.g. via Logstash to push to elasticsearch.
 * Activate with property soap.messages.extract=true.
 * 
 * @author Jonas Hecht
 */
@Configuration
@Conditional(SoapMessageLoggerConfiguration.SoapMessageLoggerPropertyCondition.class)
@ConditionalOnProperty(name = "endpoint.autoinit", matchIfMissing = true)
public class SoapMessageLoggerConfiguration {

	@Autowired
	private SpringBus springBus;

    @Bean
    @ConditionalOnProperty("soap.messages.logging")
    public String loggingActivatedLogger() {
        ((SoapMessageLoggingInInterceptor) logInInterceptorSoapMsgLogger()).logSoapMessage(true);
        ((SoapMessageLoggingOutInterceptor) logOutInterceptorSoapMsgLogger()).logSoapMessage(true);
        return "unused - this is just to activate Logging of SoapMessages via SpringBoot";
    }

    @Bean
    @ConditionalOnProperty("soap.messages.extract")
    @ConditionalOnResource(resources = "classpath:logback-spring.xml")
    public String extractionActivatedLogger() {
        ((SoapMessageLoggingInInterceptor) logInInterceptorSoapMsgLogger()).extractSoapMessage(true);
        ((SoapMessageLoggingOutInterceptor) logOutInterceptorSoapMsgLogger()).extractSoapMessage(true);
        return "unused - this is just to activate Extraction of SoapMessages via SpringBoot";
    }


	@PostConstruct
	public void activateLoggingFeature() {
		// Log SoapMessages to Logfile
    	springBus.getInInterceptors().add(logInInterceptorSoapMsgLogger());
    	springBus.getInFaultInterceptors().add(logInInterceptorSoapMsgLogger());
    	springBus.getOutInterceptors().add(logOutInterceptorSoapMsgLogger());
    	springBus.getOutFaultInterceptors().add(logOutInterceptorSoapMsgLogger());
	}

	@Bean
	public AbstractLoggingInterceptor logInInterceptorSoapMsgLogger() {
        SoapMessageLoggingInInterceptor logInInterceptor = new SoapMessageLoggingInInterceptor();
        logInInterceptor.setPrettyLogging(true);
        return logInInterceptor;
	}
	
	@Bean
	public AbstractLoggingInterceptor logOutInterceptorSoapMsgLogger() {
        SoapMessageLoggingOutInterceptor logOutInterceptor = new SoapMessageLoggingOutInterceptor();
		logOutInterceptor.setPrettyLogging(true);
		return logOutInterceptor; 
	}

    /*
     * This way we can provide the behavior to autoconfigure this Logger, if one of the properties
     * soap.message.logging and/or soap.message.extract are provided in application.properties of the
     * using project
     * It´s a kind of workaround, till Spring Boot supports the Repeatable @ConditionalOnProperty
     * (https://github.com/spring-projects/spring-boot/issues/2541)
     */
    static class SoapMessageLoggerPropertyCondition extends AnyNestedCondition {

        SoapMessageLoggerPropertyCondition() {
            super(ConfigurationPhase.PARSE_CONFIGURATION);
        }

        @ConditionalOnProperty("soap.messages.logging")
        static class LoggingEnabled {}

        @ConditionalOnProperty("soap.messages.extract")
        static class ElasticSearchExtractionEnabled {}
    }
}
