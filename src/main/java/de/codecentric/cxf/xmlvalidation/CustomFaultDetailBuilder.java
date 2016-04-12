package de.codecentric.cxf.xmlvalidation;

public interface CustomFaultDetailBuilder {

    public Object createCustomFaultDetail(String originalFaultMessage);
}
