package de.codecentric.cxf.logging.soapmsg;

import java.util.logging.Logger;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingMessage;
import org.slf4j.MDC;

import de.codecentric.cxf.logging.CxfLoggingSoapActionUtil;
import de.codecentric.cxf.logging.ElasticsearchField;
import de.codecentric.cxf.logging.BaseLogger;

public class LoggingInInterceptorSlf4jSoapMsgExtractor extends LoggingInInterceptor {

    private static final BaseLogger LOG = BaseLogger.getLogger(LoggingInInterceptorSlf4jSoapMsgExtractor.class);
    
    @Override
    protected void log(Logger logger, String message) {
        // just do nothing, because we don´t want CXF-Implementation to log,
        // we just want to Push the SOAP-Message to Logback -> Logstash -> Elasticsearch -> Kibana
    }
    
    @Override
    protected String formatLoggingMessage(LoggingMessage loggingMessage) {
        String headers = loggingMessage.getHeader().toString();
        
        String soapMethodName = CxfLoggingSoapActionUtil.extractSoapMethodNameFromHttpHeader(headers);
        MDC.put(ElasticsearchField.SOAP_METHOD_LOG_NAME.getName(), soapMethodName);
        
        // Only write the Payload (SOAP-Xml) to Logger
        if (loggingMessage.getPayload().length() > 0) {
            LOG.logInboundSoapMessage(loggingMessage.getPayload().toString());
        }
        
        LOG.logHttpHeader(headers);
        // This is just hook into CXF and get the SOAP-Message.
        // The returned String will never be logged somewhere.
        return ""; 
    }
}
