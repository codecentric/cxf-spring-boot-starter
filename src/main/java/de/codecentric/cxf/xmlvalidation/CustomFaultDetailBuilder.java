package de.codecentric.cxf.xmlvalidation;

import de.codecentric.cxf.common.FaultConst;

public interface CustomFaultDetailBuilder {

    public Object createCustomFaultDetail(String originalFaultMessage, FaultConst faultContent);
}
