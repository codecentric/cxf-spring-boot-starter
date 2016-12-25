package de.codecentric.cxf.xmlvalidation;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.codecentric.cxf.common.FaultType;
import de.codecentric.cxf.common.XmlUtils;
import de.codecentric.cxf.logging.BaseLogger;

/**
 * Builds a custom SoapFault based upon a JAX-B generated custom Exception-Object, when {@link CustomFaultBuilder} is implemented
 * and configured.
 * 
 * @author Jonas Hecht
 *
 */
@Component
public class SoapFaultBuilder {

    private static final String UNKNOWN_ERROR = "Unknown Error accured. Please contact support.";
    
	@Autowired(required=false)
	private CustomFaultBuilder customFaultBuilder;
	
	private static final BaseLogger LOG = BaseLogger.getLogger(SoapFaultBuilder.class);
	
	public void buildCustomFaultAndSet2SoapMessage(SoapMessage message, FaultType faultType) {
		Fault exceptionFault = (Fault) message.getContent(Exception.class);
		// Preserve original FaultMessage for later need
		String originalFaultMessage = getMessageValueIfThere(exceptionFault);
		exceptionFault.setMessage(customFaultBuilder.createCustomFaultMessage(faultType));
		exceptionFault.setDetail(createFaultDetailWithCustomException(originalFaultMessage, faultType));
		message.setContent(Exception.class, exceptionFault);
	}

    private String getMessageValueIfThere(Fault exceptionFault) {
        if(exceptionFault.getMessage() != null) {
            return exceptionFault.getMessage();
        }
        return UNKNOWN_ERROR;
    }
	
	private Element createFaultDetailWithCustomException(String originalFaultMessage, FaultType faultContent) {
		Element exceptionElementAppended = null;
		try {
		    Object faultDetail = customFaultBuilder.createCustomFaultDetail(originalFaultMessage, faultContent);
			Document faultDetailAsDoc = XmlUtils.marhallJaxbElement(faultDetail);
			// As the Root-Element is deleted while adding the CustomFault to the Fault-Details, we have to use a Workaround:
	    	// we append it to a new Element, which then gets deleted again
	    	exceptionElementAppended = XmlUtils.appendAsChildElement2NewElement(faultDetailAsDoc);
		} catch (Exception exception) {
			LOG.failedToBuildServiceCompliantSoapFaultDetails(exception);
			// We don´t want an Exception thrown here
		}
		return exceptionElementAppended;
	}

}
