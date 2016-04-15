package de.codecentric.cxf.logging.soapmsg;

import java.util.logging.Logger;

import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.interceptor.LoggingOutInterceptor;

import de.codecentric.cxf.logging.BaseLogger;

/**
 * This Apache CXF Logging Interceptor extracts the SoapMessage and logs it, so that the {@link BaseLogger} could put
 * it into the Slf4j MDC (Mapped Diagnostic Context, see <a href="http://logback.qos.ch/manual/mdc.html">http://logback.qos.ch/manual/mdc.html</a>} for more details)
 * with a Key directly suitable for processing with the ELK-Stack (Elasticsearch, Logstash, Kibana).  
 * 
 * @author Jonas Hecht
 *
 */
public class LoggingOutInterceptorSlf4jSoapMsgExtractor extends LoggingOutInterceptor {

    private static final BaseLogger LOG = BaseLogger.getLogger(LoggingOutInterceptorSlf4jSoapMsgExtractor.class);
    
    @Override
    protected void log(Logger logger, String message) {
        // just do nothing, because we don´t want CXF-Implementation to log,
        // we just want to Push the SOAP-Message to Logback -> Logstash -> Elasticsearch -> Kibana
    }
    
    @Override
    protected String formatLoggingMessage(LoggingMessage loggingMessage) {
        // Only write the Payload (SOAP-Xml) to Logger
        if (loggingMessage.getPayload().length() > 0) {
            LOG.logOutboundSoapMessage(loggingMessage.getPayload().toString().replaceAll("(?<=<dtypes:Data xmime:contentType=\"application/pdf\">)[^><]+(?=</dtypes:Data>)", "PdfFiltered"));
            
        }
        
        // This is just hook into CXF and get the SOAP-Message.
        // The returned String will never be logged somewhere.
        return ""; 
    }
}
