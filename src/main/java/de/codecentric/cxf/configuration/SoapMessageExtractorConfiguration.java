package de.codecentric.cxf.configuration;

import javax.annotation.PostConstruct;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.interceptor.AbstractLoggingInterceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.codecentric.cxf.logging.soapmsg.LoggingInInterceptorSlf4jSoapMsgExtractor;
import de.codecentric.cxf.logging.soapmsg.LoggingOutInterceptorSlf4jSoapMsgExtractor;

/**
 * Extraction of SoapMessages, so they can be further processed, e.g. via Logstash to push to elasticsearch.
 * Activate with property soap.messages.extract=true.
 * 
 * @author Jonas Hecht
 */
@Configuration
@ConditionalOnProperty("soap.messages.extract")
public class SoapMessageExtractorConfiguration {

	@Autowired
	private SpringBus springBus;
		
	@PostConstruct
	public void activateLoggingFeature() {
		// Log SoapMessages to Logfile
    	springBus.getInInterceptors().add(logInInterceptorSoapMsgExtractor());
    	springBus.getInFaultInterceptors().add(logInInterceptorSoapMsgExtractor());
    	springBus.getOutInterceptors().add(logOutInterceptorSoapMsgExtractor());
    	springBus.getOutFaultInterceptors().add(logOutInterceptorSoapMsgExtractor());
	}
	
	@Bean
    public AbstractLoggingInterceptor logInInterceptorSoapMsgExtractor() {
        LoggingInInterceptor logInInterceptor = new LoggingInInterceptorSlf4jSoapMsgExtractor();
        logInInterceptor.setPrettyLogging(true);
        return logInInterceptor; 
    }
	
	@Bean
    public AbstractLoggingInterceptor logOutInterceptorSoapMsgExtractor() {
        LoggingOutInterceptor logOutInterceptor = new LoggingOutInterceptorSlf4jSoapMsgExtractor();
        logOutInterceptor.setPrettyLogging(true);
        return logOutInterceptor; 
    }
}
