package de.codecentric.cxf.xmlvalidation;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.codecentric.cxf.common.FaultConst;
import de.codecentric.cxf.common.XmlUtils;
import de.codecentric.cxf.logging.BaseLogger;

@Component
public class SoapFaultBuilder {

	@Autowired
	private CustomFaultDetailBuilder customFaultDetailBuilder;
	
	private static final BaseLogger LOG = BaseLogger.getLogger(SoapFaultBuilder.class);
	
	public void buildCustomFaultAndSet2SoapMessage(SoapMessage message, FaultConst faultContent) {
		Fault exceptionFault = (Fault) message.getContent(Exception.class);
		String originalFaultMessage = exceptionFault.getMessage();
		exceptionFault.setMessage(faultContent.getMessage());
		exceptionFault.setDetail(createFaultDetailWithCustomException(originalFaultMessage, faultContent));
		message.setContent(Exception.class, exceptionFault);
	}
	
	private Element createFaultDetailWithCustomException(String originalFaultMessage,  FaultConst faultContent) {
		Element weatherExceptionElementAppended = null;
		try {
			Document soapException = XmlUtils.marhallJaxbElement(customFaultDetailBuilder.createCustomFaultDetail(originalFaultMessage));
			// As the Root-Element is deleted while adding the CustomFault to the Fault-Details, we have to use a Workaround:
	    	// we append it to a new Element, which then gets deleted again
	    	weatherExceptionElementAppended = XmlUtils.appendAsChildElement2NewElement(soapException);
		} catch (Exception exception) {
			LOG.failedToBuildWeatherServiceCompliantSoapFaultDetails(exception);
			// We don´t want an Exception thrown here
		}
		return weatherExceptionElementAppended;
	}

}
