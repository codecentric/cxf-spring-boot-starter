package de.codecentric.cxf.logging;

public enum ElasticsearchField {

    SOAP_METHOD_LOG_NAME("soap-method-name"),
    HTTP_HEADER_INBOUND("http-header-inbound"),
    SOAP_MESSAGE_INBOUND("soap-message-inbound"),
    SOAP_MESSAGE_OUTBOUND("soap-message-outbound"),
    SLEUTH_TRACE_ID("X-B3-TraceId");

    private String fieldname;
    
    private ElasticsearchField(String fieldname) {
       this.fieldname = fieldname; 
    }
    
    public String getName() {
        return fieldname;
    }
}
