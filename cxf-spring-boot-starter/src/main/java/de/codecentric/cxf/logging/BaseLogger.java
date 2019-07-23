package de.codecentric.cxf.logging;

import static net.logstash.logback.marker.Markers.append;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import de.codecentric.cxf.common.FaultType;

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
	/**
	 * Puts the String it into the Slf4j MDC (Mapped Diagnostic Context, see <a href="http://logback.qos.ch/manual/mdc.html">http://logback.qos.ch/manual/mdc.html</a>} for more details)
	 * with the Key {@link ElasticsearchField#SOAP_MESSAGE_INBOUND} you can find in your Elasticsearch, when processed via logstash. 
     *
	 * @param inboundSoapMessage the inbound SOAP message to log
	 */
	public void logInboundSoapMessage(String inboundSoapMessage) {
	    // see https://github.com/logstash/logstash-logback-encoder/tree/logstash-logback-encoder-4.5#event-specific-custom-fields
	    // net.logstash.logback.marker.Markers.append() enables to directly push a field into elasticsearch, only for one message
        log2Elasticsearch(ElasticsearchField.SOAP_MESSAGE_INBOUND, inboundSoapMessage, "===[> Inbound-SoapMessage ===[> stored in ElasticsearchField '{}'");
    }
    
	/**
     * Puts the String it into the Slf4j MDC (Mapped Diagnostic Context, see <a href="http://logback.qos.ch/manual/mdc.html">http://logback.qos.ch/manual/mdc.html</a>} for more details)
     * with the Key {@link ElasticsearchField#SOAP_MESSAGE_OUTBOUND} you can find in your Elasticsearch, when processed via logstash.
     * 
     * @param outboundSoapMessage the outbound SOAP message to log
     */
    public void logOutboundSoapMessage(String outboundSoapMessage) {
        log2Elasticsearch(ElasticsearchField.SOAP_MESSAGE_OUTBOUND, outboundSoapMessage, "<]=== Outbound-SoapMessage <]=== stored in ElasticsearchField '{}'");
    }

    /**
     * Puts the String it into the Slf4j MDC (Mapped Diagnostic Context, see <a href="http://logback.qos.ch/manual/mdc.html">http://logback.qos.ch/manual/mdc.html</a>} for more details)
     * with the Key {@link ElasticsearchField#HTTP_HEADER_INBOUND} you can find in your Elasticsearch, when processed via logstash. 
     *
	 * @param headers the HTTP headers to log
	 */
	public void logHttpHeader(String headers) {
		log2Elasticsearch(ElasticsearchField.HTTP_HEADER_INBOUND, headers, "001 >>> Header in Inbound-HTTP-Message stored in Elasticsearch-Field '{}'");
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
     * Errors - 9xx
     */
    public void errorOccuredInBackendProcessing(Throwable cause) {
        logError("901", "An Error accured in backend-processing: {}", cause.getMessage());
    }
    
    public void failedToBuildServiceCompliantSoapFaultDetails(Throwable cause) {
        logError("902", "Failed to build service-compliant SoapFault-details: {}\nStacktrace: {}", cause.getMessage(), cause.getStackTrace());
    }   
    
    public void schemaValidationError(FaultType error, String faultMessage) {
        logDebug("903", error.getMessage() + ": {}", faultMessage);
    }

	
	/*
	 * Logger-Methods - only private, to use just inside this class
	 */
    private void log2Elasticsearch(ElasticsearchField elasticsearchField, String elasticsearchFieldValue, String logTemplate) {
        delegateLogger.info(append(elasticsearchField.getName(), elasticsearchFieldValue), logTemplate, elasticsearchField.getName());
    }

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
