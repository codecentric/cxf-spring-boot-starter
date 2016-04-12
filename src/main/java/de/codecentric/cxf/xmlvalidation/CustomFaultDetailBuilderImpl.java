package de.codecentric.cxf.xmlvalidation;

import org.springframework.stereotype.Component;


@Component
public class CustomFaultDetailBuilderImpl implements CustomFaultDetailBuilder {

	private CustomFaultDetailBuilderImpl() {
	 // private Constructor for Utility-Class
	};
	
	public CxfBootStarterException createCustomFaultDetail(String originalFaultMessage) {
		// Build SOAP-Fault detail
	    CxfBootStarterException cxfException = new CxfBootStarterException();
		cxfException.setExceptionDetails(originalFaultMessage);
		cxfException.setUuid("ExtremeRandomNumber");
		return cxfException;
	}

}
