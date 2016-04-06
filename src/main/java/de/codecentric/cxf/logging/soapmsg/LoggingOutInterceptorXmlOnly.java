package de.codecentric.cxf.logging.soapmsg;

import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.interceptor.LoggingOutInterceptor;

public class LoggingOutInterceptorXmlOnly extends LoggingOutInterceptor {
    
    @Override
    protected String formatLoggingMessage(LoggingMessage loggingMessage) {
        StringBuilder buffer = new StringBuilder();
        
        // Only write the Payload (SOAP-Xml) to Logger
        if (loggingMessage.getPayload().length() > 0) {
            buffer.append("000 >>> Outbound Message:\n");
            buffer.append(loggingMessage.getPayload());
        }
        return buffer.toString();
    }
}
