package de.codecentric.cxf.logging.soapmsg;

import de.codecentric.cxf.logging.BaseLogger;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.interceptor.LoggingOutInterceptor;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * This Apache CXF Logging Interceptor extracts the SoapMessage and logs it dependent on the the setted
 * {@link #logSoapMessage(boolean)} or {@link #extractSoapMessage(boolean)}.
 * <p>
 * If {@link #logSoapMessage(boolean)} is set to true, the SoapMessage is logged to commandline respectively STOUT
 * <p>
 * If {@link #extractSoapMessage(boolean)} is set to true, the {@link BaseLogger} is used to put the SoapMessage
 * into the Slf4j MDC (Mapped Diagnostic Context, see <a href="http://logback.qos.ch/manual/mdc.html">http://logback.qos.ch/manual/mdc.html</a>} for more details)
 * with a Key directly suitable for processing with the ELK-Stack (Elasticsearch, Logstash, Kibana).
 * <p>
 * If both are set to true, the SoapMessage is logged to commandline AND put into Slf4j MDC for Elasticsearch processing.
 *
 * @author Jonas Hecht
 *
 */
public class SoapMessageLoggingOutInterceptor extends LoggingOutInterceptor {

    private static final BaseLogger LOG = BaseLogger.getLogger(SoapMessageLoggingOutInterceptor.class);

    private boolean doLogging = false;
    private boolean doExtraction = false;

    @Override
    protected void log(Logger logger, String message) {
        if(doLogging) {
            super.log(logger, message);

        } else if(doExtraction) {
            // just do nothing, because we don´t want CXF-Implementation to log,
            // we just want to Push the SOAP-Message to Logback -> Logstash -> Elasticsearch -> Kibana
        }
    }
    
    @Override
    protected String formatLoggingMessage(LoggingMessage loggingMessage) {

        if(logButDontExtract()) {
            return buildLogStatementWithSoapMessage(loggingMessage);

        } else if(extractButDontLog()) {
            return extractSoapMessageForElasticSearchProcessing(loggingMessage);

        } else if(logAndExtract()) {
            extractSoapMessageForElasticSearchProcessing(loggingMessage);
            return buildLogStatementWithSoapMessage(loggingMessage);

        } else {
            // This should never happen, as this LoggingInterceptor is only configured,
            // if either logging or extraction is activated
            return "";
        }
    }

    private String extractSoapMessageForElasticSearchProcessing(LoggingMessage loggingMessage) {
        // Only write the Payload (SOAP-Xml) to Logger
        if (loggingMessage.getPayload().length() > 0) {
            LOG.logOutboundSoapMessage(loggingMessage.getPayload().toString().replaceAll("(?<=<dtypes:Data xmime:contentType=\"application/pdf\">)[^><]+(?=</dtypes:Data>)", "PdfFiltered"));
        }

        // This is just hook into CXF and get the SOAP-Message.
        // The returned String will never be logged somewhere.
        return "";
    }

    private String buildLogStatementWithSoapMessage(LoggingMessage loggingMessage) {
        StringBuilder buffer = new StringBuilder();

        // Only write the Payload (SOAP-Xml) to Logger
        if (loggingMessage.getPayload().length() > 0) {
            buffer.append("000 >>> Outbound Message:\n");
            buffer.append(loggingMessage.getPayload());
        }
        return buffer.toString();
    }

    private boolean logAndExtract() {
        return doLogging && doExtraction;
    }

    private boolean extractButDontLog() {
        return doExtraction && !doLogging;
    }

    private boolean logButDontExtract() {
        return doLogging && !doExtraction;
    }

    public void logSoapMessage(boolean active) {
        this.doLogging = active;
    }

    public void extractSoapMessage(boolean active) {
        this.doExtraction = active;
    }
}
