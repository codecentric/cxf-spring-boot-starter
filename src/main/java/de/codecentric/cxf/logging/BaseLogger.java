package de.codecentric.cxf.logging;

import static net.logstash.logback.marker.Markers.append;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

public class BaseLogger {
    
	private Logger delegateLogger;
	
	private BaseLogger() {};
	
	public static <L> BaseLogger getLogger(Class<L> class2LogFor) {
		BaseLogger frameworkLogger = new BaseLogger();
		frameworkLogger.delegateLogger = LoggerFactory.getLogger(class2LogFor);
		return frameworkLogger;
	}
	
	/*
	 * Framework - 0xx
	 */
	// see https://github.com/logstash/logstash-logback-encoder/tree/logstash-logback-encoder-4.5#event-specific-custom-fields
    // net.logstash.logback.marker.Markers.append() enables to directly push a field into elasticsearch, only for one message
    
	public void logInboundSoapMessage(String headers) {
        delegateLogger.info(append(ElasticsearchField.SOAP_MESSAGE_INBOUND.getName(), headers),
                "===[> Inbound-SoapMessage ===[>");
    }
    
    public void logOutboundSoapMessage(String headers) {
        delegateLogger.info(append(ElasticsearchField.SOAP_MESSAGE_OUTBOUND.getName(), headers),
                "<]=== Outbound-SoapMessage <]===");
    }
	
	public void logHttpHeader(String headers) {
		delegateLogger.info(append(ElasticsearchField.HTTP_HEADER_INBOUND.getName(), headers),
		        "001 >>> Header in Inbound-HTTP-Message see Elasticsearch-Field '{}'",
		        ElasticsearchField.HTTP_HEADER_INBOUND.getName());
	}
	
	public void successfullyCalledServeEndpointWithMethod(String calledServiceMethod) {
		logInfo("002", "The Serviceendpoint was called successfully with the Method '{}()' - handing over to internal processing.", calledServiceMethod);
	}
	
	public void logCallTime(String calltime) {
        logInfo("009", "Calltime: {}", calltime);
    }
	
	

	
	
	/*
	 * Facade-Mode - 5xx
	 */
	public <T> void facadeModeReturningDummyResponseWithResponseType(Class<T> responseType) {
		logDebug("501", "Facade-Mode: Returning Dummy-Response with ResponseType {}", responseType);
	}

	
	

	
	/*
	 * Logger-Methods - only private, to use just inside this class
	 */
	private String logDebugAndBuildExceptionMessage(String id, String messageTemplate, Object... parameters) {
		logDebug(id, messageTemplate, parameters);
		return exceptionMessage(id, messageTemplate, parameters);
	}
	
	private void logDebug(String id, String messageTemplate, Object... parameters) {
		String msg = formatMessage(id, messageTemplate);
		delegateLogger.debug(msg, parameters);
	}
	
	private void logInfo(String id, String messageTemplate, Object... parameters) {
		String msg = formatMessage(id, messageTemplate);
		delegateLogger.info(msg, parameters);
	}
	
	private void logError(String id, String messageTemplate, Object... parameters) {
		String msg = formatMessage(id, messageTemplate);
		delegateLogger.error(msg, parameters);
	}
	
	private String formatMessage(String id, String messageTemplate) {
		return id + " >>> " + messageTemplate;
	}
	
	private String exceptionMessage(String id, String messageTemplate, Object... parameters) {
		String message = formatMessage(id, messageTemplate);
	    if(parameters == null || parameters.length == 0) {
	      return message;
	    } else {
	      return MessageFormatter.arrayFormat(message, parameters).getMessage();
	    }
	}
	
}
