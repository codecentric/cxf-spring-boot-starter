package de.codecentric.cxf.xmlvalidation;

import org.springframework.context.annotation.Bean;

import de.codecentric.cxf.common.FaultType;

/**
 * Implement this Interface and configure a Spring {@link Bean} to activate extended XML schema validation and Soap-Fault behavior.
 * 
 * @author Jonas Hecht
 *
 */
public interface CustomFaultBuilder {

    public String createCustomFaultMessage(FaultType faultType);

    public Object createCustomFaultDetail(String originalFaultMessage, FaultType faultType);
}
