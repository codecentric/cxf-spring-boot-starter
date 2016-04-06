package de.codecentric.cxf.logging;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;

/**
 * This filter´s purpose is to use logback´s MDC (http://logback.qos.ch/manual/mdc.html)
 * implementation to generate and handle an Identifier (UUID) declaring all Log-Messages
 * over the scope of one Consumers Webservice-Call.
 * 
 * MDC is a SLF4J-Concept, that is capable to hold information over the usage of one thread.
 * In typical Server-environments such threads are used to handle a WebService-Call.
 * 
 * As this Infrastructure is intended to handle many Calls in parallel and we log into an 
 * ELK-Stack (with Logstash, Elasticsearch and Kibana), there´s the need to correlate those
 * Log-Messages, that belong to one WebService-Call-Thread. 
 * 
 * As we also want to log the in- and out-going XML-Messages (mainly SOAP, but maybe also the 
 * corrupt non SOAP- or XML-Schema/Standard-compliant ones), which are logged by Apache CXF, we
 * have to fill the MDC before CXF is starting it´s work. Also finally after CXF has finished
 * we have to remove our Unique-Id to prevent recycling of this Id in another WebService-Call,
 * which possibly reuses the same thread.
 * For that requirement a Servlet-Filter comes in handy, because it´s ability in giving a starting
 * point before CXF and a finally-block after it. 
 */
public class LogCorrelationFilter implements Filter {

    private static final BaseLogger LOG = BaseLogger.getLogger(LogCorrelationFilter.class);
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // dead simple call-time logging
        long timeInbound = System.currentTimeMillis();
        
        // Put an unique-Logging-Id to the logback Mapped Diagnostic Context to correlate
        // against one Customer-Request, see http://logback.qos.ch/manual/mdc.html
        MDC.put(ElasticsearchField.ID_KEY.getName(), generateUUID());
        try {
            chain.doFilter(request, response);
        } finally {
            logCallTime(timeInbound);
            
            // finally remove unique-Logging-Id, so that it could´nt be accidentally
            // reused for another Consumer-Request
            MDC.remove(ElasticsearchField.ID_KEY.getName());
        }       
    }

    private void logCallTime(long timeInbound) {
        // dead simple call-time logging
        long timeOutbound = System.currentTimeMillis();
        long calltime = timeOutbound - timeInbound;
        MDC.put(ElasticsearchField.TIME_CALLTIME.getName(), String.valueOf(calltime));
        
        LOG.logCallTime(String.valueOf(calltime));
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void destroy() {}

}
