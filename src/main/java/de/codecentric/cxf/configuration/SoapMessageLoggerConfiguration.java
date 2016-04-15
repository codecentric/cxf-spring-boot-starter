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

import de.codecentric.cxf.logging.soapmsg.LoggingInInterceptorXmlOnly;
import de.codecentric.cxf.logging.soapmsg.LoggingOutInterceptorXmlOnly;

/**
 * Logging of SoapMessages to either Console and/or Logstash. To activate Logging, set property logging.soap.messages=true.
 * 
 * @author Jonas Hecht
 */
@Configuration
@ConditionalOnProperty("soap.messages.logging")
public class SoapMessageLoggerConfiguration {

	@Autowired
	private SpringBus springBus;
		
	@PostConstruct
	public void activateLoggingFeature() {
		// Log SoapMessages to Logfile
    	springBus.getInInterceptors().add(logInInterceptorSoapMsgLogger());
    	springBus.getInFaultInterceptors().add(logInInterceptorSoapMsgLogger());
    	springBus.getOutInterceptors().add(logOutInterceptor());
    	springBus.getOutFaultInterceptors().add(logOutInterceptor());
	}

	@Bean
	public AbstractLoggingInterceptor logInInterceptorSoapMsgLogger() {
	    LoggingInInterceptor logInInterceptor = new LoggingInInterceptorXmlOnly();
	    logInInterceptor.setPrettyLogging(true);
		return logInInterceptor; 
	}
	
	@Bean
	public AbstractLoggingInterceptor logOutInterceptor() {
		LoggingOutInterceptor logOutInterceptor = new LoggingOutInterceptorXmlOnly();
		logOutInterceptor.setPrettyLogging(true);
		return logOutInterceptor; 
	}
}
