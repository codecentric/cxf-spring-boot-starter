package de.codecentric.cxf.configuration;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.interceptor.AbstractLoggingInterceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.codecentric.cxf.logging.LogCorrelationFilter;
import de.codecentric.cxf.logging.soapmsg.LoggingInInterceptorXmlOnly;
import de.codecentric.cxf.logging.soapmsg.LoggingOutInterceptorXmlOnly;

/**
 * Logging of SoapMessages to either Console and/or Logstash. To activate Logging, set property logging.soap.messages=true.
 * 
 * @author Jonas Hecht
 */
@Configuration
@ConditionalOnProperty("logging.soap.messages")
public class SoapMessageLoggerConfiguration {

	@Autowired
	private SpringBus springBus;
		
	@PostConstruct
	public void activateLoggingFeature() {
		// Log SoapMessages to Logfile
    	springBus.getInInterceptors().add(logInInterceptor());
    	springBus.getInFaultInterceptors().add(logInInterceptor());
    	springBus.getOutInterceptors().add(logOutInterceptor());
    	springBus.getOutFaultInterceptors().add(logOutInterceptor());
	}

	@Bean
	public AbstractLoggingInterceptor logInInterceptor() {
	    LoggingInInterceptor logInInterceptor = new LoggingInInterceptorXmlOnly(); // LoggingInInterceptorSlf4jSoapMsgExtractor();
	    logInInterceptor.setPrettyLogging(true);
		return logInInterceptor; 
	}
	
	@Bean
	public AbstractLoggingInterceptor logOutInterceptor() {
		LoggingOutInterceptor logOutInterceptor = new LoggingOutInterceptorXmlOnly(); // LoggingOutInterceptorSlf4jSoapMsgExtractor();
		logOutInterceptor.setPrettyLogging(true);
		return logOutInterceptor; 
	}
	
	// Register Filter for Correlating Logmessages from the same Service-Consumer
	@Bean
	public Filter filter() {
	    return new LogCorrelationFilter();
	}
}
